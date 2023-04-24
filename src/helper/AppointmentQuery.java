package helper;

import model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentQuery {

    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        String query = "SELECT a.*, c.Contact_Name " +
                "FROM appointments a " +
                "JOIN contacts c ON a.Contact_ID = c.Contact_ID";

        try (PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");
                int userId = resultSet.getInt("User_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                String createdBy = resultSet.getString("Created_By");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String contactName = resultSet.getString("Contact_Name");

                appointments.add(new Appointment(appointmentId, contactId, customerId, userId, title, description, location, type, createdBy, lastUpdatedBy, start, end, createDate, lastUpdate));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return appointments;
    }

    public static void insertAppointment(Appointment appointment) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = ConnectionHelper.getConnection();
            String sql = "INSERT INTO appointments(Appointment_ID, Contact_ID, Customer_ID, User_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = ((Connection) connection).prepareStatement(sql);
            ps.setInt(1, appointment.getAppointmentId());
            ps.setInt(2, appointment.getContactId());
            ps.setInt(3, appointment.getCustomerId());
            ps.setInt(4, appointment.getUserId());
            ps.setString(5, appointment.getTitle());
            ps.setString(6, appointment.getDescription());
            ps.setString(7, appointment.getLocation());
            ps.setString(8, appointment.getType());
            ps.setString(9, appointment.getStartDateTime().toString());
            ps.setString(10, appointment.getEndDateTime().toString());
            ps.setString(11, appointment.getCreateDate().toString());
            ps.setString(12, appointment.getCreatedBy());
            ps.setTimestamp(13, Timestamp.valueOf(appointment.getLastUpdate()));
            ps.setString(14, appointment.getLastUpdatedBy());
            ps.executeUpdate();
            System.out.println("Created new appointment with id " + appointment.getAppointmentId() + " in the database.");
        } finally {
            ConnectionHelper.closeResultSet(rs);
            ConnectionHelper.closeStatement(ps);
        }
    }

    public String getContactName(int contactId) {
        String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
        try (PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, contactId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static List<String> getAllContactNames() {
        List<String> contactNames = new ArrayList<>();

        String query = "SELECT Contact_Name FROM contacts";
        try (PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String contactName = resultSet.getString("Contact_Name");
                contactNames.add(contactName);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return contactNames;
    }
}
