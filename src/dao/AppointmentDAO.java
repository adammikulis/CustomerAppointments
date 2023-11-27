package dao;


import helper.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import report.AppointmentHoursByCountryReport;
import report.AppointmentTypeMonthCountReport;
import model.Contact;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/** Class for querying the SQL database for appointments
 *
 */
public class AppointmentDAO {

    /** Returns a list of all appointments
     *
     * @return appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String query = "SELECT a.*, c.Contact_Name " +
                "FROM appointments a " +
                "JOIN contacts c ON a.Contact_ID = c.Contact_ID";

        try {
            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                int customerId = rs.getInt("Customer_ID");
                int contactId = rs.getInt("Contact_ID");
                int userId = rs.getInt("User_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String createdBy = rs.getString("Created_By");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

                allAppointments.add(new Appointment(appointmentId, contactId, customerId, userId, title, description, location, type, createdBy, lastUpdatedBy, start, end, createDate, lastUpdate));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return allAppointments;
    }


    /** Method for setting appointment fields for prepared statement
     *
     * @param ps prepared statement
     * @param appointment appointment to manipulate
     * @throws SQLException
     */
    private static void setAppointmentFields(PreparedStatement ps, Appointment appointment) throws SQLException {
        ps.setInt(1, appointment.getContactId());
        ps.setInt(2, appointment.getCustomerId());
        ps.setInt(3, appointment.getUserId());
        ps.setString(4, appointment.getTitle());
        ps.setString(5, appointment.getDescription());
        ps.setString(6, appointment.getLocation());
        ps.setString(7, appointment.getType());
        ps.setTimestamp(8, Timestamp.valueOf(appointment.getStartDateTime()));
        ps.setTimestamp(9, Timestamp.valueOf(appointment.getEndDateTime()));
        ps.setTimestamp(10, Timestamp.valueOf(appointment.getLastUpdate()));
        ps.setString(11, appointment.getLastUpdatedBy());
        ps.setInt(12, appointment.getUserId());
    }

    /** Method for updating a current appointment
     *
     * @param updatedAppointment appointment to update
     * @throws SQLException
     */
    public static void updateAppointment(Appointment updatedAppointment) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        String query = "UPDATE appointments SET Contact_ID = ?, Customer_ID = ?, User_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ? "
                + "WHERE Appointment_ID = ?";

        PreparedStatement ps = conn.prepareStatement(query);

        setAppointmentFields(ps, updatedAppointment);
        ps.setInt(12, updatedAppointment.getAppointmentId());

        ps.executeUpdate();
    }

    /** Method to insert new appointment into database
     *
     * @param appointment appointment to insert
     * @throws SQLException
     */
    public void insertAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (contact_id, customer_id, user_id, title, description, location, type, start, end, last_update, last_updated_by, create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(query);
            setAppointmentFields(ps, appointment); // Sets fields 1-11 common to insert and update appointment
            ps.setTimestamp(12, Timestamp.valueOf(appointment.getCreateDate()));
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
    }


    /** Deletes the chosen appointment by ID
     *
     * @param appointmentId ID of appointment to delete
     */
    public void deleteAppointment(int appointmentId) {
        String deleteSql = "DELETE FROM appointments WHERE appointment_id = ?";

        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSql);
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
    }

    /** Returns appointment by contact
     *
     * @param contact contact to look up appointments
     * @return list of appointments
     */
    public static List<Appointment> getAppointmentsByContact(Contact contact) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE Contact_ID = ?";

        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, contact.getContactId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                int contactId = rs.getInt("Contact_ID");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String createdBy = rs.getString("Created_By");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

                appointments.add(new Appointment(appointmentId, contactId, customerId, userId, title, description, location, type, createdBy, lastUpdatedBy, start, end, createDate, lastUpdate));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return appointments;
    }

    public static List<AppointmentTypeMonthCountReport> getAppointmentCountByTypeAndMonth() {
        List<AppointmentTypeMonthCountReport> reportData = new ArrayList<>();

        String query = "SELECT Type, " +
                "COUNT(CASE WHEN MONTH(Start) = 1 THEN 1 END) AS January, " +
                "COUNT(CASE WHEN MONTH(Start) = 2 THEN 1 END) AS February, " +
                "COUNT(CASE WHEN MONTH(Start) = 3 THEN 1 END) AS March, " +
                "COUNT(CASE WHEN MONTH(Start) = 4 THEN 1 END) AS April, " +
                "COUNT(CASE WHEN MONTH(Start) = 5 THEN 1 END) AS May, " +
                "COUNT(CASE WHEN MONTH(Start) = 6 THEN 1 END) AS June, " +
                "COUNT(CASE WHEN MONTH(Start) = 7 THEN 1 END) AS July, " +
                "COUNT(CASE WHEN MONTH(Start) = 8 THEN 1 END) AS August, " +
                "COUNT(CASE WHEN MONTH(Start) = 9 THEN 1 END) AS September, " +
                "COUNT(CASE WHEN MONTH(Start) = 10 THEN 1 END) AS October, " +
                "COUNT(CASE WHEN MONTH(Start) = 11 THEN 1 END) AS November, " +
                "COUNT(CASE WHEN MONTH(Start) = 12 THEN 1 END) AS December " +
                "FROM appointments " +
                "GROUP BY Type";

        try (PreparedStatement ps = ConnectionManager.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("Type");
                int janCount = rs.getInt("January");
                int febCount = rs.getInt("February");
                int marCount = rs.getInt("March");
                int aprCount = rs.getInt("April");
                int mayCount = rs.getInt("May");
                int junCount = rs.getInt("June");
                int julCount = rs.getInt("July");
                int augCount = rs.getInt("August");
                int septCount = rs.getInt("September");
                int octCount = rs.getInt("October");
                int novCount = rs.getInt("November");
                int decCount = rs.getInt("December");
                reportData.add(new AppointmentTypeMonthCountReport(type, janCount, febCount, marCount, aprCount, mayCount, junCount, julCount, augCount, septCount, octCount, novCount, decCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportData;
    }

    /**
     * Returns the total hours of appointments by country.
     *
     * @return A list of AppointmentHoursByCountryReport objects.
     */
    public static List<AppointmentHoursByCountryReport> getAppointmentHoursByCountry() {
        List<AppointmentHoursByCountryReport> reportData = new ArrayList<>();

        // SQL query to calculate total hours of appointments by country
        String query = "SELECT co.Country, SUM(TIMESTAMPDIFF(HOUR, a.Start, a.End)) AS TotalHours " +
                "FROM appointments a " +
                "JOIN customers cu ON a.Customer_ID = cu.Customer_ID " +
                "JOIN first_level_divisions d ON cu.Division_ID = d.Division_ID " +
                "JOIN countries co ON d.Country_ID = co.Country_ID " +
                "GROUP BY co.Country";

        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String country = rs.getString("Country");
                int totalHours = rs.getInt("TotalHours");

                reportData.add(new AppointmentHoursByCountryReport(country, totalHours));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportData;
    }
}