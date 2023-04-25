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

        try (PreparedStatement preparedStatement = DriverManager.getConnection().prepareStatement(query);
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

    public int insertAppointment(Appointment appointment) throws SQLException {
        String insertSql = "INSERT INTO appointments (customer_id, contact_id, user_id, title, description, location, type, start, end, created_by, last_updated_by, create_date, last_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DriverManager.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, appointment.getCustomerId());
            preparedStatement.setInt(2, appointment.getContactId());
            preparedStatement.setInt(3, appointment.getUserId());
            preparedStatement.setString(4, appointment.getTitle());
            preparedStatement.setString(5, appointment.getDescription());
            preparedStatement.setString(6, appointment.getLocation());
            preparedStatement.setString(7, appointment.getType());
            preparedStatement.setObject(8, appointment.getStartDateTime());
            preparedStatement.setObject(9, appointment.getEndDateTime());
            preparedStatement.setString(10, appointment.getCreatedBy());
            preparedStatement.setString(11, appointment.getLastUpdatedBy());
            preparedStatement.setObject(12, appointment.getCreateDate());
            preparedStatement.setObject(13, appointment.getLastUpdate());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating appointment failed, no ID obtained.");
                }
            }
        }
    }

    public void deleteAppointment(int appointmentId) {
        String deleteSql = "DELETE FROM appointments WHERE appointment_id = ?";

        Connection conn = DriverManager.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(deleteSql)) {
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
    }






}
