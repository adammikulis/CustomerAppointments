package helper;

import model.Client;
import model.ClientList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientQuery {

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();

        String query = "SELECT * FROM customers";
        try (PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int clientId = resultSet.getInt("Customer_ID");
                String clientName = resultSet.getString("Customer_Name");
                String streetAddress = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                Integer divisionId = resultSet.getInt("Division_ID");

                clients.add(new Client(clientId, clientName, streetAddress, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return clients;
    }

    public void saveNewClient(String name, String streetAddress, String postalCode, String phone) {
        try {
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = ConnectionHelper.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, streetAddress);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, "admin");
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, "admin");
            ps.setInt(9, 1); // Replace with the correct division ID

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newClientId = rs.getInt(1);
                System.out.println("Saved new client to database with ID " + newClientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getClientDivisionsByCountry(String country) {
        List<String> divisions = new ArrayList<>();

        try (PreparedStatement ps = ConnectionHelper.getConnection().prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID = (SELECT Country_ID FROM countries WHERE Country = ?)");
        ) {
            ps.setString(1, country);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String division = resultSet.getString("Division");
                    divisions.add(division);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return divisions;
    }

    public static boolean deleteClient(Client clientToDelete) {
        try {
            // Delete all associated appointments
            PreparedStatement deleteAppointmentsStatement = ConnectionHelper.getConnection().prepareStatement("DELETE FROM appointments WHERE Customer_Id = ?");
            deleteAppointmentsStatement.setInt(1, clientToDelete.getClientId());
            deleteAppointmentsStatement.executeUpdate();
            System.out.println("Deleted all appointments associated with client with ID " + clientToDelete.getClientId());

            // Delete the selected client from the database
            PreparedStatement deleteClientStatement = ConnectionHelper.getConnection().prepareStatement("DELETE FROM customers WHERE Customer_Id = ?");
            deleteClientStatement.setInt(1, clientToDelete.getClientId());
            deleteClientStatement.executeUpdate();
            System.out.println("Deleted client with ID " + clientToDelete.getClientId());

            // Remove the selected client from the list of all clients
            ClientList.getAllClients().remove(clientToDelete);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
