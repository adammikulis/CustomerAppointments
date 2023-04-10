package model;

import helper.ClientQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClientList {

    private static ClientQuery clientQuery = new ClientQuery();
    private static List<Client> clients = clientQuery.getClients();


    private static ObservableList<Client> allClients = FXCollections.observableArrayList(clients);
    private static ObservableList<Client> filteredClients = FXCollections.observableArrayList();


    public static ObservableList<Client> getAllClients() {
        return allClients;
    }
}
