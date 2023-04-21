package controller;

import helper.ClientQuery;
import helper.ConnectionHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientScreenController implements Initializable {


    Stage stage;
    Parent scene;

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
    private ComboBox clientCountryComboBox;
    @FXML
    private ComboBox clientDivisionComboBox;

    @FXML
    public void onCountryComboBoxChanged(Event event) {
        String country = clientCountryComboBox.getValue().toString();
        List<String> divisions = ClientQuery.getClientDivisionsByCountry(country);
        clientDivisionComboBox.getItems().clear();
        clientDivisionComboBox.getItems().addAll(divisions);
    }



    private Client currentClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientTableView.setItems(ClientList.getAllClients());
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientCountryComboBox.getItems().addAll("U.S", "UK", "Canada");
        clientCountryComboBox.setOnAction(event -> onCountryComboBoxChanged(event));


        clientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected row: " + newValue);
                currentClient = newValue; // Set the current client

                // Populate the textfields with the selected client's data
                clientScreenNameTextField.setText(currentClient.getClientName());
                clientScreenAddressTextField.setText(currentClient.getStreetAddress());
                clientScreenPostalCodeTextField.setText(currentClient.getPostalCode());
                clientScreenPhoneTextField.setText(currentClient.getPhone());
            }
        });
    }


    public void onClientScreenDeleteClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the selected client
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this client?");
            alert.setContentText("Deleting a client will also delete all appointments associated with this client.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = ClientQuery.deleteClient(selectedClient);

                if (!isDeleted) {
                    System.out.println("Error deleting client.");
                }
            }
        } else {
            System.out.println("No client selected for deletion.");
        }
    }





    public void onUpdateCurrentClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the selected client
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Get the data from the input fields
            String name = clientScreenNameTextField.getText();
            String streetAddress = clientScreenAddressTextField.getText();
            String postalCode = clientScreenPostalCodeTextField.getText();
            String phone = clientScreenPhoneTextField.getText();

            // Update the selected client with the new data
            selectedClient.setClientName(name);
            selectedClient.setStreetAddress(streetAddress);
            selectedClient.setPostalCode(postalCode);
            selectedClient.setPhone(phone);
            selectedClient.setLastUpdate(LocalDateTime.now());
            selectedClient.setLastUpdatedBy("admin");

            // Update the client in the database
            String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_Id = ?";
            try {
                PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(updateStatement);
                preparedStatement.setString(1, selectedClient.getClientName());
                preparedStatement.setString(2, selectedClient.getStreetAddress());
                preparedStatement.setString(3, selectedClient.getPostalCode());
                preparedStatement.setString(4, selectedClient.getPhone());
                preparedStatement.setObject(5, selectedClient.getLastUpdate());
                preparedStatement.setString(6, selectedClient.getLastUpdatedBy());
                preparedStatement.setInt(7, selectedClient.getClientId());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Updated client with ID " + selectedClient.getClientId());

                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Error updating client in database: " + e.getMessage());
            }

            // Refresh the table view
            clientTableView.refresh();
        } else {
            System.out.println("No client selected for update.");
        }
    }

    public void onClientScreenSaveNewClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the data from the input fields
        String name = clientScreenNameTextField.getText();
        String streetAddress = clientScreenAddressTextField.getText();
        String postalCode = clientScreenPostalCodeTextField.getText();
        String phone = clientScreenPhoneTextField.getText();

        // Create a new Client object with the input data and the next available client ID
        int newClientId = ClientList.getNextClientId();
        Client newClient = new Client(newClientId, name, streetAddress, postalCode, phone, null, null, null, null, 0);

        // Add the new client to the list of all clients
        ClientList.addClient(newClient);

        // Clear the input fields
        String empty = "";
        clientScreenNameTextField.setText(empty);
        clientScreenAddressTextField.setText(empty);
        clientScreenPostalCodeTextField.setText(empty);
        clientScreenPhoneTextField.setText(empty);

        // Save the new client to the database using the saveNewClient method from ClientQuery
        ClientQuery clientQuery = new ClientQuery();
        clientQuery.saveNewClient(name, streetAddress, postalCode, phone);
    }




    public void onClientScreenBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
