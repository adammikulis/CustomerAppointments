package dao;

import helper.ConnectionManager;
import model.User;
import report.LoginLogger;
import java.io.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for checking user login and give errors when needed
 *
 */
public class LoginDAO {

    /** Checks user login and returns true is successful
     *
     * @param inputUserName
     * @param inputPassword
     * @return true if login is successful
     * @throws SQLException
     * @throws IOException
     */
    public static boolean checkLogin(String inputUserName, String inputPassword) throws SQLException, IOException {
        boolean loginSuccess = false;
        if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
            LoginLogger.logLoginAttempt(inputUserName, inputPassword, loginSuccess);
            throw new IllegalArgumentException("Username and password are required");
        }

        String sql = "SELECT Password FROM users WHERE User_Name = ?";
        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement(sql);
        ps.setString(1, inputUserName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String storedPassword = rs.getString("Password");
            if (inputPassword.equals(storedPassword)) {
                loginSuccess = true;
                LoginLogger.logLoginAttempt(inputUserName, inputPassword, loginSuccess);
                UserDAO.setCurrentUser(UserDAO.getUserByName(inputUserName));
                return true;
            } else {
                LoginLogger.logLoginAttempt(inputUserName, inputPassword, loginSuccess);
                throw new IllegalArgumentException("Incorrect password");
            }
        } else {
            LoginLogger.logLoginAttempt(inputUserName, inputPassword, loginSuccess);
            throw new IllegalArgumentException("Username not found");
        }
    }
}