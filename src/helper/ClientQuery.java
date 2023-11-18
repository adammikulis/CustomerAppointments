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
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query);
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

    public static int insertClient(Client client) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        String insertStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, "
                + "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, client.getClientName());
        ps.setString(2, client.getStreetAddress());
        ps.setString(3, client.getPostalCode());
        ps.setString(4, client.getPhone());
        ps.setTimestamp(5, Timestamp.valueOf(client.getCreateDate()));
        ps.setString(6, client.getCreatedBy());
        ps.setTimestamp(7, Timestamp.valueOf(client.getLastUpdate()));
        ps.setString(8, client.getLastUpdatedBy());
        ps.setInt(9, client.getDivisionId());

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating client failed, no rows affected.");
        }

        int clientId=0;
        try  {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                clientId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating client failed, no ID obtained.");
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        ps.close();
        return clientId;
    }

    public static List<String> getClientCountries() {
        List<String> countries = new ArrayList<>();

        try {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("SELECT Country FROM countries");
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String country = resultSet.getString("Country");
                    countries.add(country);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return countries;

    }

    public static List<String> getClientDivisionsByCountry(String country) {
        List<String> divisions = new ArrayList<>();

        try {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("SELECT Division FROM first_level_divisions WHERE Country_ID = (SELECT Country_ID FROM countries WHERE Country = ?)");
            ps.setString(1, country);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String division = resultSet.getString("Division");
                    divisions.add(division);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return divisions;
    }

    public boolean deleteClient(Client clientToDelete) throws SQLException {
        try {
            Connection conn = ConnectionManager.getConnection();

            // Delete all associated appointments
            PreparedStatement deleteAppointmentsStatement = conn.prepareStatement("DELETE FROM appointments WHERE Customer_Id = ?");
            deleteAppointmentsStatement.setInt(1, clientToDelete.getClientId());
            deleteAppointmentsStatement.executeUpdate();
            System.out.println("Deleted all appointments associated with client with ID " + clientToDelete.getClientId());

            // Delete the selected client from the database
            PreparedStatement deleteClientStatement = conn.prepareStatement("DELETE FROM customers WHERE Customer_Id = ?");
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

    public void updateClient(Client client) throws SQLException {
        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_Id = ?";
        PreparedStatement statement = null;
        Connection conn = null;

        try {
            conn = ConnectionManager.getConnection();
            statement = conn.prepareStatement(updateStatement);
            statement.setString(1, client.getClientName());
            statement.setString(2, client.getStreetAddress());
            statement.setString(3, client.getPostalCode());
            statement.setString(4, client.getPhone());
            statement.setObject(5, client.getLastUpdate());
            statement.setString(6, client.getLastUpdatedBy());
            statement.setInt(7, client.getClientId());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s) in customers table.");
        }
        finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public int getNextClientId() throws SQLException {
        int nextId = 0;
        try {
            Connection conn = ConnectionManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(Customer_ID) + 1 AS next_id FROM client_schedule.customers");

            if (rs.next()) {
                nextId = rs.getInt("next_id");
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return nextId;
    }

    public static int getDivisionIdByCountryAndDivision(String country, String division) {
        int divisionId = -1;

        try {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ? AND Country_ID = (SELECT Country_ID FROM countries WHERE Country = ?)");
            ps.setString(1, division);
            ps.setString(2, country);

            try {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    divisionId = resultSet.getInt("Division_ID");
                }
            }
            catch (SQLException e) {
                System.out.println("SQL Error");
                e.printStackTrace(System.out);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return divisionId;
    }

    public static String getCountryByDivisionId(int divisionId) {
        String country = null;

        try  {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("SELECT Country FROM first_level_divisions INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID WHERE Division_ID = ?");
            ps.setInt(1, divisionId);

            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    country = rs.getString("Country");
                }
            }
            catch (SQLException e) {
                System.out.println("SQL Error");
                e.printStackTrace(System.out);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return country;
    }

    public static String getDivisionByDivisionId(int divisionId) {
        String division = null;

        try {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("SELECT Division FROM first_level_divisions WHERE Division_ID = ?");
            ps.setInt(1, divisionId);

            try {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    division = resultSet.getString("Division");
                }
            }
            catch (SQLException e) {
                System.out.println("SQL Error");
                e.printStackTrace(System.out);
            }

        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return division;
    }

}