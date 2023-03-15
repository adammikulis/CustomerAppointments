package helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AppointmentQuery {

    public static int insert_user(String userName, String password) throws SQLException {
        String sql = "INSERT INTO users (User_Name, Password) VALUES(?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

}
