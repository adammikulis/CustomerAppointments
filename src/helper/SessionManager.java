package helper;

import java.sql.*;

public class SessionManager {
    private static SessionManager instance;
    private String currentUserName;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String userName) {
        this.currentUserName = userName;
    }

    public int getCurrentUserId()
    {
        String query = "SELECT User_ID FROM users WHERE User_Name = ?";
        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, currentUserName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("User_ID");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return 0;
    }
}
