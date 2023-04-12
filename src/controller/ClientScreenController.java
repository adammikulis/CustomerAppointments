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
    }

    public void onClientScreenDeleteClientButtonPressed(ActionEvent actionEvent) throws IOException {
        System.out.println("Deleted client");
    }

    public void onClientScreenSaveCopyClientButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Copied client");
    }

    public void onClientScreenSaveNewClientButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Saved new client");
    }
}
