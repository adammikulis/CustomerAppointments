package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientList {
    private static ObservableList<Client> allClients = FXCollections.observableArrayList();
    private static ObservableList<Client> filteredClients = FXCollections.observableArrayList();


    public static ObservableList<Client> getAllClients() {
        return allClients;
    }
}
