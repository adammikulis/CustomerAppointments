package helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Manages the jdbc driver and connection with SQL database
 *
 */
public abstract class ConnectionManager {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection connection;  // Connection Interface

    /** Opens connection to database
     *
     */
    public static void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(driver); // Locate Driver
                connection = java.sql.DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
                System.out.println("Connection successful!");
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /** Closes connection to the database
     *
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed!");
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /** Returns current database connection
     *
     * @return database connection
     */
    public static Connection getConnection() {
        return connection;
    }
}
