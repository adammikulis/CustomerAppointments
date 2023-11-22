package model;

import dao.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

/** Class for creating a list of customers*/
public class CustomerList {

    private static CustomerDAO customerDAO = new CustomerDAO();
    private static List<Customer> customers = customerDAO.getClients();

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList(customers);
    private static ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    /** Returns observable list of all customers
     *
     * @return allCustomers
     */
    public static ObservableList<Customer> getAllClients() {
        return allCustomers;
    }


    /**
     * Inserts a new customer into the database.
     *
     * @param customer The customer to be inserted
     */
    public static void addClient(Customer customer) throws SQLException {
        int clientId = customerDAO.getNextClientId();
        customer.setClientId(clientId);
        customerDAO.insertClient(customer);
        allCustomers.add(customer);
    }
}
