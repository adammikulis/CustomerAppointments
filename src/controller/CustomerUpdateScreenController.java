package controller;

import dao.*;
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
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/** Class for controlling the customer update screen
 *
 */
public class CustomerUpdateScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private Customer updatedCustomer;
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
    private ObservableList<Customer> updatedCustomerList;

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
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    /** Enables customer from previous screen to be transferred over to update screen
     *
     * @param transferredCustomer
     */
    public void transferCustomer(Customer transferredCustomer) {
        updatedCustomerList = FXCollections.observableArrayList();
        updatedCustomerList.add(transferredCustomer);
        updatedCustomer = transferredCustomer;
        customerTableView.setItems(updatedCustomerList);

        // Update text fields
        customerScreenNameTextField.setText(transferredCustomer.getCustomerName());
        customerScreenAddressTextField.setText(transferredCustomer.getStreetAddress());
        customerScreenPostalCodeTextField.setText(transferredCustomer.getPostalCode());
        customerScreenPhoneTextField.setText(transferredCustomer.getPhone());

        updateCustomerCountryAndDivision();
    }

    /** Updates country and division for the customer
     *
     */
    private void updateCustomerCountryAndDivision() {

        populateCountryComboBox();
        populateDivisionComboBox(updatedCustomer.getCountry());

        // Set the selection in combo boxes
        customerCountryComboBox.getSelectionModel().select(updatedCustomer.getCountry());
        customerDivisionComboBox.getSelectionModel().select(updatedCustomer.getDivision());

        // Forcefully refresh the combo boxes
        customerCountryComboBox.setVisibleRowCount(customerCountryComboBox.getItems().size());
        customerDivisionComboBox.setVisibleRowCount(customerDivisionComboBox.getItems().size());
    }

    /** Updates currently selected customer in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        
        // Takes all field input and checks for empty/null values
        if (validateCustomerInputs()) {

            // Update the selected customer with the new data
            updatedCustomer.setCustomerName(name);
            updatedCustomer.setStreetAddress(streetAddress);
            updatedCustomer.setPostalCode(postalCode);
            updatedCustomer.setPhone(phone);
            updatedCustomer.setLastUpdate(LocalDateTime.now());
            updatedCustomer.setLastUpdatedBy(UserDAO.getCurrentUser().getUserName());
            updatedCustomer.setDivisionId(divisionId);

            // Update the customer in the database using the updateCustomer method from CustomerDAO
            try {
                CustomerDAO.updateCustomer(updatedCustomer);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CustomerScreen.fxml"));
                loader.load();
                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            } catch (SQLException e) {
                System.out.println("Error updating customer.");
                e.printStackTrace();
            }
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
    public void onCustomerUpdateScreenBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
}