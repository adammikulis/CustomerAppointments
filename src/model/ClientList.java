package model;

import helper.ClientQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

/** Class for creating a list of clients*/
public class ClientList {

    private static ClientQuery clientQuery = new ClientQuery();
    private static List<Client> clients = clientQuery.getClients();

    private static ObservableList<Client> allClients = FXCollections.observableArrayList(clients);
    private static ObservableList<Client> filteredClients = FXCollections.observableArrayList();

    public static ObservableList<Client> getAllClients() {
        return allClients;
    }


    /**
     * Insert a new client into the database.
     *
     * @param client The client to be inserted
     */
    public static void addClient(Client client) throws SQLException {
        int clientId = clientQuery.getNextClientId();
        client.setClientId(clientId);
        clientQuery.insertClient(client);
        allClients.add(client);
    }
}
