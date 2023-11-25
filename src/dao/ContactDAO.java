package dao;

import helper.ConnectionManager;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Class for querying the database for contacts
 *
 */
public class ContactDAO {

    /** Returns a list of all contacts in the database
     *
     * @return list of all contacts
     */
    public static List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM contacts";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contacts.add(new Contact(contactId, contactName, email));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out); }
        return contacts;
    }

    /** Gets contact name based on contact ID
     *
     * @param contactId
     * @return contact name
     */
    public static String getContactName(int contactId) {
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, contactId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return null;
    }

    /** Gets contact based on contact ID
     *
     * @param contactId
     * @return contact
     * @throws SQLException
     */
    public static Contact getContact(int contactId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            String query = "SELECT * FROM contacts WHERE Contact_ID=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, contactId);

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                return new Contact(id, name, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return null if no contact found with the given ID
        return null;
    }
}