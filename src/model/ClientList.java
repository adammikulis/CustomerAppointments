package model;

import helper.ClientQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

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
     * @return The newly inserted client object
     */
    public static void addClient(Client client) throws SQLException {
        int clientId = clientQuery.getNextClientId();
        client.setClientId(clientId);
        clientQuery.insertClient(client);
        allClients.add(client);
    }

    /**
     * Generates the next client ID.
     *
     * @return the next available client ID as an integer
     */
    public static int getNextClientId() throws SQLException {
        return clientQuery.getNextClientId();
    }
}
