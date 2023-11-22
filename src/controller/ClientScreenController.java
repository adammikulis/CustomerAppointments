package controller;

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
import model.Customer;
import model.CustomerList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for controlling the client screen
 *
 */
public class ClientScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private Customer currentCustomer;
    private CustomerDAO customerDAO = new CustomerDAO();
    private String name;
    private String streetAddress;
    private String postalCode;
    private String phone;
    private String country;
    private String division;
    private int divisionId;

    @FXML
    private TableView<Customer> clientTableView;

    @FXML
    private TableColumn<Customer, Integer> clientIdColumn;
    @FXML
    private TableColumn<Customer, String> clientNameColumn;
    @FXML
    private TableColumn<Customer, String> streetAddressColumn;
    @FXML
    private TableColumn<Customer, Integer> postalCodeColumn;
    @FXML
    private TableColumn<Customer, Integer> phoneColumn;

    @FXML
    private TextField clientScreenNameTextField;
    @FXML
    private TextField clientScreenAddressTextField;
    @FXML
    private TextField clientScreenPostalCodeTextField;
    @FXML
    private TextField clientScreenPhoneTextField;

    @FXML
    private ComboBox<String> clientCountryComboBox;
    @FXML
    private ComboBox<String> clientDivisionComboBox;

    /** Initialization for client screen
     * Lambda expressions used for listeners
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientTableView.setItems(CustomerList.getAllClients());
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientCountryComboBox.getItems().addAll(CustomerDAO.getClientCountries());

        clientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected row: " + newValue);
                currentCustomer = newValue; // Set the current client

                // Populate the textfields with the selected client's data
                clientScreenNameTextField.setText(currentCustomer.getClientName());
                clientScreenAddressTextField.setText(currentCustomer.getStreetAddress());
                clientScreenPostalCodeTextField.setText(currentCustomer.getPostalCode());
                clientScreenPhoneTextField.setText(currentCustomer.getPhone());

                // Populate the combo boxes with the selected client's country and division
                clientCountryComboBox.getSelectionModel().select(currentCustomer.getCountry());
                List<String> divisions = CustomerDAO.getClientDivisionsByCountry(currentCustomer.getCountry());
                clientDivisionComboBox.getItems().clear();
                clientDivisionComboBox.getItems().addAll(divisions);
                clientDivisionComboBox.getSelectionModel().select(currentCustomer.getDivision());

            }
        });
    }

    /** Deletes currently selected client from the database
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onClientScreenDeleteClientButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {
        // Get the selected client
        Customer selectedCustomer = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this client?");
            alert.setContentText("Deleting a client will also delete all appointments associated with this client.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean isDeleted = customerDAO.deleteClient(selectedCustomer);

                    if (!isDeleted) {
                        System.out.println("Error deleting client.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error deleting client.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No client selected for deletion.");
        }
        clearFieldsAndRefresh();
    }

    /** Updates currently selected client in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCurrentClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the selected client
        Customer selectedCustomer = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            if (validateClientInputs()) {

                // Get the Division_ID using the CustomerDAO method
                CustomerDAO customerDAO = new CustomerDAO();
                divisionId = customerDAO.getDivisionIdByCountryAndDivision(country, division);

                // Update the selected client with the new data
                selectedCustomer.setClientName(name);
                selectedCustomer.setStreetAddress(streetAddress);
                selectedCustomer.setPostalCode(postalCode);
                selectedCustomer.setPhone(phone);
                selectedCustomer.setLastUpdate(LocalDateTime.now());
                selectedCustomer.setLastUpdatedBy(SessionManager.getInstance().getCurrentUserName());
                selectedCustomer.setDivisionId(divisionId);

                // Update the client in the database using the updateClient method from CustomerDAO
                try {
                    customerDAO.updateClient(selectedCustomer);
                } catch (SQLException e) {
                    System.out.println("Error updating client.");
                    e.printStackTrace();
                }
                clearFieldsAndRefresh();
            }


        } else {
            noClientSelectedAlert();
        }
    }

    /** Gets data from input fields to create new or update client
     *
     */
    private void getClientDataFromInputFields() {
        // Get the data from the input fields
        name = clientScreenNameTextField.getText();
        streetAddress = clientScreenAddressTextField.getText();
        postalCode = clientScreenPostalCodeTextField.getText();
        phone = clientScreenPhoneTextField.getText();

        String countryValue = (String) clientCountryComboBox.getValue();
        country = (countryValue != null) ? countryValue : "";

        String divisionValue = (String) clientDivisionComboBox.getValue();
        division = (divisionValue != null) ? divisionValue : "";

    }

    /** Creates new client in the database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onClientScreenSaveNewClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the data from the input fields and check for empty
        if (validateClientInputs()) {

            // Get the Division_ID using the CustomerDAO method
            CustomerDAO customerDAO = new CustomerDAO();
            divisionId = customerDAO.getDivisionIdByCountryAndDivision(country, division);

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
                CustomerList.addClient(newCustomer);
            } catch (SQLException e) {
                System.out.println("Error adding new client to list.");
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
        clientScreenNameTextField.clear();
        clientScreenAddressTextField.clear();
        clientScreenPostalCodeTextField.clear();
        clientScreenPhoneTextField.clear();
        clearCountryComboBox();
        clientCountryComboBox.getItems().addAll(CustomerDAO.getClientCountries());
        clearDivisionComboBox();

        // Refresh table view and clear selection
        clientTableView.refresh();
        clientTableView.getSelectionModel().clearSelection();
    }
    @FXML
    private void onCountryComboBoxChanged(ActionEvent event) {
        String country = clientCountryComboBox.getValue() != null ? clientCountryComboBox.getValue().toString() : null;
        if (country != null) {
            populateDivisionComboBox(country);
        }
        else {
            clearDivisionComboBox();
        }
    }

    /** Fills country combo box
     *
     */
    private void populateCountryComboBox() {
        ObservableList<String> countries = FXCollections.observableArrayList();

    }

    /** Fills division combo box based on country
     *
     * @param country
     */
    private void populateDivisionComboBox(String country) {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        List<String> divisionsList = customerDAO.getClientDivisionsByCountry(country);

        divisions.addAll(divisionsList);
        clientDivisionComboBox.setItems(divisions);
    }

    /** Clears country combo box
     *
     */
    private void clearCountryComboBox() {
        clientCountryComboBox.getItems().clear();
        clientCountryComboBox.setValue(null);
    }


    /** Clears division combo box
     *
     */
    private void clearDivisionComboBox() {
        clientDivisionComboBox.getItems().clear();
        clientCountryComboBox.setValue(null);
    }

    /** Takes user back to home screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onClientScreenBackButtonPressed(ActionEvent actionEvent) throws IOException {
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
    private void onClientScreenClearSelectionButtonPressed(ActionEvent actionEvent) {
        clearFieldsAndRefresh();
    }

    /** Makes sure that no inputs are empty
     *
     * @return true if no fields are empty
     */
    private boolean validateClientInputs() {
        getClientDataFromInputFields();
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

    /** Shows alert if no client is selected
     *
     */
    private void noClientSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No client selected");
        alert.setHeaderText("Please select a client before attempting to update");
        alert.showAndWait();
    }
}