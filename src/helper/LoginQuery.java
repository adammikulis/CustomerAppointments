package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginQuery {

    public static boolean checkLogin(String inputUserName, String inputPassword) throws SQLException {
        if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        String sql = "SELECT Password FROM users WHERE User_Name = ?";
        PreparedStatement ps = DriverManager.getConnection().prepareStatement(sql);
        ps.setString(1, inputUserName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String storedPassword = rs.getString("Password");
            if (inputPassword.equals(storedPassword)) {
                return true;
            } else {
                throw new IllegalArgumentException("Incorrect password");
            }
        } else {
            throw new IllegalArgumentException("Username not found");
        }
    }
}
