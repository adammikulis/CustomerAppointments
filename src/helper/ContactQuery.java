package helper;

import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactQuery {

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection();
            String query = "SELECT * FROM contacts";
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");
                contacts.add(new Contact(contactId, contactName, email));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return contacts;
    }



    public String getContactName(int contactId) {
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        try (PreparedStatement preparedStatement = DriverManager.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static List<String> getAllContactNames() {
        List<String> contactNames = new ArrayList<>();

        String query = "SELECT Contact_Name FROM contacts";
        try (PreparedStatement preparedStatement = DriverManager.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String contactName = resultSet.getString("Contact_Name");
                contactNames.add(contactName);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return contactNames;
    }

    public Contact getContact(int contactId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection();

            String query = "SELECT * FROM contacts WHERE Contact_ID=?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, contactId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                return new Contact(id, name, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }

        }
        // Return null if no contact found with the given ID
        return null;
    }

    public Contact getContactByName(String contactName) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection();

            String query = "SELECT * FROM contacts WHERE Contact_Name=?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, contactName);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                return new Contact(id, name, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        // Return null if no contact found with the given name
        return null;
    }
}