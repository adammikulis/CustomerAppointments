package controller;

import dao.CountryDAO;
import dao.CustomerDAO;
import helper.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private String country;
    private String division;
    private int divisionId;

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
    private ComboBox<String> customerDivisionComboBox;

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

        refreshCountryComboBox();

        customerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected row: " + newValue);
                currentCustomer = newValue; // Set the current customer

                // Populate the textfields with the selected customer's data
                customerScreenNameTextField.setText(currentCustomer.getCustomerName());
                customerScreenAddressTextField.setText(currentCustomer.getStreetAddress());
                customerScreenPostalCodeTextField.setText(currentCustomer.getPostalCode());
                customerScreenPhoneTextField.setText(currentCustomer.getPhone());

                // Populate the combo boxes with the selected customer's country and division
                //customerCountryComboBox.getSelectionModel().select(currentCustomer.getCountry());
                //List<String> divisions = CustomerDAO.getCustomerDivisionsByCountry(currentCustomer.getCountry());
                //customerDivisionComboBox.getItems().clear();
                //customerDivisionComboBox.getItems().addAll(divisions);
                //customerDivisionComboBox.getSelectionModel().select(currentCustomer.getDivision());
            }
        });
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
        clearFieldsAndRefresh();
    }

    /** Updates currently selected customer in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCurrentCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the selected customer
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            if (validateCustomerInputs()) {

                // Get the Division_ID using the CustomerDAO method
                CustomerDAO customerDAO = new CustomerDAO();
                divisionId = customerDAO.getDivisionIdByCountryAndDivision(country, division);

                // Update the selected customer with the new data
                selectedCustomer.setCustomerName(name);
                selectedCustomer.setStreetAddress(streetAddress);
                selectedCustomer.setPostalCode(postalCode);
                selectedCustomer.setPhone(phone);
                selectedCustomer.setLastUpdate(LocalDateTime.now());
                selectedCustomer.setLastUpdatedBy(SessionManager.getInstance().getCurrentUserName());
                selectedCustomer.setDivisionId(divisionId);

                // Update the customer in the database using the updateCustomer method from CustomerDAO
                try {
                    customerDAO.updateCustomer(selectedCustomer);
                } catch (SQLException e) {
                    System.out.println("Error updating customer.");
                    e.printStackTrace();
                }
                clearFieldsAndRefresh();
            }


        } else {
            noCustomerSelectedAlert();
        }
    }

    /** Gets data from input fields to create new or update customer
     *
     */
    private void getCustomerDataFromInputFields() {
        // Get the data from the input fields
        name = customerScreenNameTextField.getText();
        streetAddress = customerScreenAddressTextField.getText();
        postalCode = customerScreenPostalCodeTextField.getText();
        phone = customerScreenPhoneTextField.getText();

        //String countryValue = (String) customerCountryComboBox.getValue();
        //country = (countryValue != null) ? countryValue : "";

        //String divisionValue = (String) customerDivisionComboBox.getValue();
        //division = (divisionValue != null) ? divisionValue : "";

    }

    /** Creates new customer in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomerScreenSaveNewCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the data from the input fields and check for empty
        if (validateCustomerInputs()) {

            // Get the Division_ID using the CustomerDAO method
            divisionId = CustomerDAO.getDivisionIdByCountryAndDivision(country, division);

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
                    SessionManager.getInstance().getCurrentUserName(),
                    now,
                    SessionManager.getInstance().getCurrentUserName(),
                    divisionId
            );
            try {
                CustomerDAO.insertCustomer(newCustomer);
            } catch (SQLException e) {
                System.out.println("Error adding new customer to list.");
                e.printStackTrace();
            }
            clearFieldsAndRefresh();
        }
    }

    /** Clears input fields and refreshes table view
     *
     */
    private void clearFieldsAndRefresh() {
        // Clear the input fields
        customerScreenNameTextField.clear();
        customerScreenAddressTextField.clear();
        customerScreenPostalCodeTextField.clear();
        customerScreenPhoneTextField.clear();
        clearCountryComboBox();
        clearDivisionComboBox();

        // Refresh table view and clear selection
        customerTableView.setItems(CustomerDAO.getAllCustomers());
        customerTableView.refresh();
        customerTableView.getSelectionModel().clearSelection();
        refreshCountryComboBox();
    }
    @FXML
    private void onCountryComboBoxChanged(ActionEvent event) {
        String country = customerCountryComboBox.getValue() != null ? customerCountryComboBox.getValue().toString() : null;
        if (country != null) {
            populateDivisionComboBox(country);
        }
        else {
            clearDivisionComboBox();
        }
    }


    /** Fills division combo box based on country
     *
     * @param country
     */
    private void populateDivisionComboBox(String country) {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        List<String> divisionsList = CustomerDAO.getCustomerDivisionsByCountry(country);

        divisions.addAll(divisionsList);
        customerDivisionComboBox.setItems(divisions);
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
        customerCountryComboBox.setValue(null);
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
        clearFieldsAndRefresh();
    }

    /** Makes sure that no inputs are empty
     *
     * @return true if no fields are empty
     */
    private boolean validateCustomerInputs() {
        getCustomerDataFromInputFields();
        if (name.trim().isEmpty() || streetAddress.trim().isEmpty() || postalCode.trim().isEmpty() || phone.trim().isEmpty() || division.isEmpty()) {
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

    public void refreshCountryComboBox() {
        List<Country> countries = null;
        try {
            countries = CountryDAO.getAllCountries();
            customerCountryComboBox.setItems(FXCollections.observableArrayList(countries));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}