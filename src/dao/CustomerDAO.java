package dao;

import helper.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Class for querying customers in the database
 *
 */
public class CustomerDAO {

    /** Returns a list of all customers
     *
     * @return customer list
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        String query = "SELECT * FROM customers";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String streetAddress = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                Integer divisionId = resultSet.getInt("Division_ID");

                allCustomers.add(new Customer(customerId, customerName, streetAddress, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return allCustomers;
    }

    /**
     * Inserts new customer into database
     *
     * @param newcustomer
     * @throws SQLException
     */
    public static void insertCustomer(Customer newcustomer) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        String insertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, "
                + "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, newcustomer.getCustomerName());
        ps.setString(2, newcustomer.getStreetAddress());
        ps.setString(3, newcustomer.getPostalCode());
        ps.setString(4, newcustomer.getPhone());
        ps.setTimestamp(5, Timestamp.valueOf(newcustomer.getCreateDate()));
        ps.setString(6, newcustomer.getCreatedBy());
        ps.setTimestamp(7, Timestamp.valueOf(newcustomer.getLastUpdate()));
        ps.setString(8, newcustomer.getLastUpdatedBy());
        ps.setInt(9, newcustomer.getDivisionId());

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating newcustomer failed, no rows affected.");
        }

        ps.close();
    }

    /** Deletes customer from database
     *
     * @param customerToDelete
     * @return true if customer is deleted
     * @throws SQLException
     */
    public static boolean deleteCustomer(Customer customerToDelete) throws SQLException {
        try {
            Connection conn = ConnectionManager.getConnection();

            // Delete all associated appointments
            PreparedStatement ps = conn.prepareStatement("DELETE FROM appointments WHERE Customer_Id = ?");
            ps.setInt(1, customerToDelete.getCustomerId());
            ps.executeUpdate();
            System.out.println("Deleted all appointments associated with customer with ID " + customerToDelete.getCustomerId());

            // Delete the selected customer from the database
            ps = conn.prepareStatement("DELETE FROM customers WHERE Customer_Id = ?");
            ps.setInt(1, customerToDelete.getCustomerId());
            ps.executeUpdate();
            System.out.println("Deleted customer with ID " + customerToDelete.getCustomerId());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Updates existing customer in database
     *
     * @param currentCustomer
     * @throws SQLException
     */
    public static void updateCustomer(Customer currentCustomer) throws SQLException {
        String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_Id = ?";
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);

        try {
            ps.setString(1, currentCustomer.getCustomerName());
            ps.setString(2, currentCustomer.getStreetAddress());
            ps.setString(3, currentCustomer.getPostalCode());
            ps.setString(4, currentCustomer.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(currentCustomer.getLastUpdate()));
            ps.setString(6, currentCustomer.getLastUpdatedBy());
            ps.setInt(7, currentCustomer.getCustomerId());

            int rowsAffected = ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
    }
}