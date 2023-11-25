package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for controlling the customer screen
 *
 */
public class CustomerScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private Customer currentCustomer;
    private String name;
    private String streetAddress;
    private String postalCode;
    private String phone;
    private int divisionId;
    private Country selectedCountry;
    private Division selectedDivision;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> streetAddressColumn;
    @FXML
    private TableColumn<Customer, Integer> postalCodeColumn;
    @FXML
    private TableColumn<Customer, Integer> phoneColumn;
    @FXML
    private TableColumn<Customer, Country> countryColumn;
    @FXML
    private TableColumn<Customer, Division> divisionColumn;

    @FXML
    private TextField customerScreenNameTextField;
    @FXML
    private TextField customerScreenAddressTextField;
    @FXML
    private TextField customerScreenPostalCodeTextField;
    @FXML
    private TextField customerScreenPhoneTextField;

    @FXML
    private ComboBox<Country> customerCountryComboBox;
    @FXML
    private ComboBox<Division> customerDivisionComboBox;


    /** Initialization for customer screen
     * Lambda expressions used for listeners
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTableView.setItems(CustomerDAO.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));


        // Listener for selected customer
        customerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentCustomer = newValue;
                clearFields();
                populateCountryComboBox();
            }
        });

        // Listener for country combo box
        customerCountryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateDivisionComboBox(newVal);
            }
        });
    }

    /** Creates new customer in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomerScreenSaveNewCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the data from the input fields and check for empty
        if (validateCustomerInputs()) {

            // Set the create and last update times to the current time
            LocalDateTime now = LocalDateTime.now();

            // Create a new Customer object with the input data and current time and user for create and last update info
            Customer newCustomer = new Customer(
                    0,
                    name,
                    streetAddress,
                    postalCode,
                    phone,
                    now,
                    UserDAO.getCurrentUser().getUserName(),
                    now,
                    UserDAO.getCurrentUser().getUserName(),
                    divisionId
            );
            try {
                CustomerDAO.insertCustomer(newCustomer);
            } catch (SQLException e) {
                System.out.println("Error adding new customer to list.");
                e.printStackTrace();
            }
            clearFields();
            refreshTableView();
            customerTableView.getSelectionModel().clearSelection();
        }
    }

    /** Deletes currently selected customer from the database
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onCustomerScreenDeleteCustomerButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {
        // Get the selected customer
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this customer?");
            alert.setContentText("Deleting a customer will also delete all appointments associated with this customer.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean isDeleted = CustomerDAO.deleteCustomer(selectedCustomer);

                    if (!isDeleted) {
                        System.out.println("Error deleting customer.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error deleting customer.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No customer selected for deletion.");
        }
        clearFields();
        refreshTableView();
        customerTableView.getSelectionModel().clearSelection();
    }

    /** Updates currently selected customer in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCurrentCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CustomerUpdateScreen.fxml"));
            loader.load();

            CustomerUpdateScreenController CSController = loader.getController();
            CSController.transferCustomer(customerTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e)
        {
            noCustomerSelectedAlert();
        }


    }

    /** Gets data from input fields to create new or update customer
     *
     */
    private void getValuesFromFields() {
        // Get the data from the input fields
        name = customerScreenNameTextField.getText();
        streetAddress = customerScreenAddressTextField.getText();
        postalCode = customerScreenPostalCodeTextField.getText();
        phone = customerScreenPhoneTextField.getText();

        selectedCountry = customerCountryComboBox.getSelectionModel().getSelectedItem();
        selectedDivision = customerDivisionComboBox.getSelectionModel().getSelectedItem();
        if (selectedDivision != null) {
            divisionId = selectedDivision.getDivisionId();
        }

    }



    /** Clears input fields and refreshes table view
     *
     */
    private void clearFields() {
        // Clear the input fields
        customerScreenNameTextField.clear();
        customerScreenAddressTextField.clear();
        customerScreenPostalCodeTextField.clear();
        customerScreenPhoneTextField.clear();
        clearCountryComboBox();
        clearDivisionComboBox();
    }

    private void refreshTableView() {
        customerTableView.setItems(CustomerDAO.getAllCustomers());
        customerTableView.refresh();
    }
    @FXML
    private void onCountryComboBoxChanged(ActionEvent event) {
        Country country = customerCountryComboBox.getValue();
        if (country != null) {
            populateDivisionComboBox(country);
        }
        else {
            clearDivisionComboBox();
        }
    }

    public void populateCountryComboBox() {
        clearCountryComboBox();
        List<Country> countries = CountryDAO.getAllCountries();
        customerCountryComboBox.setItems(FXCollections.observableArrayList(countries));
    }

    /** Fills division combo box based on country
     *
     * @param country
     */
    private void populateDivisionComboBox(Country country) {
        clearDivisionComboBox();
        List<Division> divisions = DivisionDAO.getDivisionsByCountry(country);
        customerDivisionComboBox.setItems(FXCollections.observableArrayList(divisions));
    }


    /** Clears country combo box
     *
     */
    private void clearCountryComboBox() {
        customerCountryComboBox.getItems().clear();
        customerCountryComboBox.setValue(null);
    }


    /** Clears division combo box
     *
     */
    private void clearDivisionComboBox() {
        customerDivisionComboBox.getItems().clear();
        customerDivisionComboBox.setValue(null);
    }

    /** Takes user back to home screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomerScreenBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Clears selection and refreshes table
     *
     * @param actionEvent
     */
    @FXML
    private void onCustomerScreenClearSelectionButtonPressed(ActionEvent actionEvent) {
        clearFields();
        customerTableView.getSelectionModel().clearSelection();
    }

    /** Makes sure that no inputs are empty
     *
     * @return true if no fields are empty
     */
    private boolean validateCustomerInputs() {
        getValuesFromFields();
        if (name.trim().isEmpty() || streetAddress.trim().isEmpty() || postalCode.trim().isEmpty() || phone.trim().isEmpty() || customerCountryComboBox.getSelectionModel().getSelectedItem() == null || customerDivisionComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please fill in all the required fields.");
            alert.setContentText("Name, street address, postal code, phone number, and country/division must not be empty.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /** Shows alert if no customer is selected
     *
     */
    private void noCustomerSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No customer selected");
        alert.setHeaderText("Please select a customer before attempting to update");
        alert.showAndWait();
    }
}