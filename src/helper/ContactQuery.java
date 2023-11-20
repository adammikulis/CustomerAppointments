package helper;

import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Class to query the database for contacts
 *
 */
public class ContactQuery {

    /** Returns a list of all contacts in the database
     *
     * @return list of all contacts
     */
    public List<Contact> getAllContacts() {
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
            e.printStackTrace(System.out);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return contacts;
    }

    /** Gets contact name based on contact ID
     *
     * @param contactId
     * @return contact name
     */
    public String getContactName(int contactId) {
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

    /** Returns a list of all contact names
     *
     * @return list of all contact names
     */
    public static List<String> getAllContactNames() {
        List<String> contactNames = new ArrayList<>();

        String query = "SELECT Contact_Name FROM contacts";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query);
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

    /** Gets contact based on contact ID
     *
     * @param contactId
     * @return contact
     * @throws SQLException
     */
    public Contact getContact(int contactId) throws SQLException {
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
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }

        }
        // Return null if no contact found with the given ID
        return null;
    }

    /** Returns contact by contact name
     *
     * @param contactName
     * @return contact
     * @throws SQLException
     */
    public Contact getContactByName(String contactName) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            String query = "SELECT * FROM contacts WHERE Contact_Name=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, contactName);

            rs = ps.executeQuery();

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
            if (ps != null) {
                ps.close();
            }
        }
        // Return null if no contact found with the given name
        return null;
    }

    /** Returns contact ID by name
     *
     * @param contactName
     * @return contact ID
     * @throws SQLException
     */
    public int getContactIdByName(String contactName) throws SQLException {
        return getContactByName(contactName).getContactId();
    }
}