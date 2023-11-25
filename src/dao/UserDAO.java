package dao;

import helper.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;

/** Class for querying users in the database
 *
 */
public class UserDAO {

    static User currentUser;

    public static void setCurrentUser(User inputUser){
        currentUser = inputUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    /** Returns a list of all users
     *
     * @return customer list
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String query = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                allUsers.add(new User(userId, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return allUsers;
    }

    public static User getUser(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            String query = "SELECT * FROM users WHERE User_ID=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            if (rs.next()) {
                int user = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                return new User(user, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return null if no user found with the given ID
        return null;
    }

    public static User getUserByName(String userName) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            String query = "SELECT * FROM users WHERE User_Name=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, userName);

            rs = ps.executeQuery();

            if (rs.next()) {
                int user = rs.getInt("User_ID");
                userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                return new User(user, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return null if no user found with the given ID
        return null;
    }
}