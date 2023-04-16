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

    /**
     * Add a new client to the list of clients.
     *
     * @param newClient The client to be added
     */
    public static void addClient(Client newClient) {
        allClients.add(newClient);
    }

    /**
     * Generates the next client ID.
     *
     * @return the next available client ID as an integer
     */
    public static int getNextClientId() {
        int maxId = 0;
        for (Client client : allClients) {
            if (client.getClientId() > maxId) {
                maxId = client.getClientId();
            }
        }
        return maxId + 1;
    }
}
