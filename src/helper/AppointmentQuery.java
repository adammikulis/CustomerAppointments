package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static void check_login() throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int userId = rs.getInt("User_Id");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userName+ " | ");
            System.out.print(password + "\n");
        }


    }

}
