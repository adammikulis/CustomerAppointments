package helper;

import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientQuery {

    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();

        String query = "SELECT * FROM customers";
        try (PreparedStatement preparedStatement = JDBCHelper.getConnection().prepareStatement(query);
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
}
