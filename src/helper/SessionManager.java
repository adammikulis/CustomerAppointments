package helper;

import java.sql.*;

/** Class to track current session/instance for recording current user logged in
 *
 */
public class SessionManager {
    private static SessionManager instance;
    private String currentUserName;

    /** Empty constructor
     *
     */
    private SessionManager() {
    }

    /** Returns session manager instance or creates new one if null
     *
     * @return session manager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /** Returns current username with a fallback
     *
     * @return current username
     */
    public String getCurrentUserName() {
        if (currentUserName != null) {
            return currentUserName;}
        else {
            return "test"; //Fallback if login screen is skipped for testing
        }
    }

    /** Sets current username
     *
     * @param userName
     */
    public void setCurrentUserName(String userName) {
        this.currentUserName = userName;
    }

    /** Returns user ID based on current user name
     *
     * @return current user ID
     */
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
