package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Client;
import model.ClientList;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        clientTableView.setItems(ClientList.getAllClients());
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        streetAddressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected row: " + newValue);
            }
        });
    }

    public void onClientScreenDeleteClientButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the selected client
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            // Remove the selected client from the list of all clients
            ClientList.getAllClients().remove(selectedClient);
            System.out.println("Deleted client with ID " + selectedClient.getClientId());
        } else {
            System.out.println("No client selected for deletion.");
        }
    }


    public void onClientScreenSaveCopyClientButton(ActionEvent actionEvent) throws IOException {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            int newClientId = selectedClient.getClientId();
            String clientName = selectedClient.getClientName();
            String streetAddress = selectedClient.getStreetAddress();
            String postalCode = selectedClient.getPostalCode();
            String phone = selectedClient.getPhone();
            LocalDateTime createDate = selectedClient.getCreateDate();
            String createdBy = selectedClient.getCreatedBy();
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = selectedClient.getLastUpdatedBy();
            int divisionId = selectedClient.getDivisionId();

            Client copiedClient = new Client(newClientId, clientName, streetAddress, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            ClientList.addClient(copiedClient);

            System.out.println("Copied client: " + copiedClient);
        } else {
            System.out.println("No client selected");
        }
    }


    public void onClientScreenSaveNewClientButton(ActionEvent actionEvent) throws IOException {
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
        clientScreenNameTextField.clear();
        clientScreenAddressTextField.clear();
        clientScreenPostalCodeTextField.clear();
        clientScreenPhoneTextField.clear();

        System.out.println("Saved new client with ID " + newClientId);
    }


}
