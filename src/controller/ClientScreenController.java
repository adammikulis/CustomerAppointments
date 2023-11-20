package controller;

import helper.ClientQuery;
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
import model.Client;
import model.ClientList;

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

    private Client currentClient;
    private ClientQuery clientQuery = new ClientQuery();
    private String name;
    private String streetAddress;
    private String postalCode;
    private String phone;
    private String country;
    private String division;
    private int divisionId;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, Integer> clientIdColumn;
    @FXML
    private TableColumn<Client, String> clientNameColumn;
    @FXML
    private TableColumn<Client, String> streetAddressColumn;
    @FXML
    private TableColumn<Client, Integer> postalCodeColumn;
    @FXML
    private TableColumn<Client, Integer> phoneColumn;

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
        clientTableView.setItems(ClientList.getAllClients());
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientCountryComboBox.getItems().addAll(ClientQuery.getClientCountries());

        clientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected row: " + newValue);
                currentClient = newValue; // Set the current client

                // Populate the textfields with the selected client's data
                clientScreenNameTextField.setText(currentClient.getClientName());
                clientScreenAddressTextField.setText(currentClient.getStreetAddress());
                clientScreenPostalCodeTextField.setText(currentClient.getPostalCode());
                clientScreenPhoneTextField.setText(currentClient.getPhone());

                // Populate the combo boxes with the selected client's country and division
                clientCountryComboBox.getSelectionModel().select(currentClient.getCountry());
                List<String> divisions = ClientQuery.getClientDivisionsByCountry(currentClient.getCountry());
                clientDivisionComboBox.getItems().clear();
                clientDivisionComboBox.getItems().addAll(divisions);
                clientDivisionComboBox.getSelectionModel().select(currentClient.getDivision());

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
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this client?");
            alert.setContentText("Deleting a client will also delete all appointments associated with this client.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean isDeleted = clientQuery.deleteClient(selectedClient);

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
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            if (validateClientInputs()) {

                // Get the Division_ID using the ClientQuery method
                ClientQuery clientQuery = new ClientQuery();
                divisionId = clientQuery.getDivisionIdByCountryAndDivision(country, division);

                // Update the selected client with the new data
                selectedClient.setClientName(name);
                selectedClient.setStreetAddress(streetAddress);
                selectedClient.setPostalCode(postalCode);
                selectedClient.setPhone(phone);
                selectedClient.setLastUpdate(LocalDateTime.now());
                selectedClient.setLastUpdatedBy(SessionManager.getInstance().getCurrentUserName());
                selectedClient.setDivisionId(divisionId);

                // Update the client in the database using the updateClient method from ClientQuery
                try {
                    clientQuery.updateClient(selectedClient);
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

            // Get the Division_ID using the ClientQuery method
            ClientQuery clientQuery = new ClientQuery();
            divisionId = clientQuery.getDivisionIdByCountryAndDivision(country, division);

            // Set the create and last update times to the current time
            LocalDateTime now = LocalDateTime.now();

            // Create a new Client object with the input data and current time and user for create and last update info
            Client newClient = new Client(
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
                ClientList.addClient(newClient);
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
        clientCountryComboBox.getItems().addAll(ClientQuery.getClientCountries());
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

        List<String> divisionsList = clientQuery.getClientDivisionsByCountry(country);

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