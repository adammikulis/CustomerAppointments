package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHelper {

    public static boolean check_login(String inputUserName, String inputPassword) throws SQLException {
        String sql = "SELECT Password FROM users WHERE User_Name = ?";
        PreparedStatement ps = ConnectionHelper.getConnection().prepareStatement(sql);
        ps.setString(1, inputUserName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String storedPassword = rs.getString("Password");
            return inputPassword.equals(storedPassword);
        }
        return false;
    }
}
