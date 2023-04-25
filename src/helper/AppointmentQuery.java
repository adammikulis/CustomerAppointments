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

                appointments.add(new Appointment(appointmentId, contactId, customerId, userId, title, description, location, type, createdBy, lastUpdatedBy, start, end, createDate, lastUpdate));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }
        return appointments;
    }

    private void setAppointmentFields(PreparedStatement preparedStatement, Appointment appointment) throws SQLException {
        preparedStatement.setInt(1, appointment.getContactId());
        preparedStatement.setInt(2, appointment.getCustomerId());
        preparedStatement.setInt(3, appointment.getUserId());
        preparedStatement.setString(4, appointment.getTitle());
        preparedStatement.setString(5, appointment.getDescription());
        preparedStatement.setString(6, appointment.getLocation());
        preparedStatement.setString(7, appointment.getType());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(appointment.getStartDateTime()));
        preparedStatement.setTimestamp(9, Timestamp.valueOf(appointment.getEndDateTime()));
        preparedStatement.setTimestamp(10, Timestamp.valueOf(appointment.getLastUpdate()));
        preparedStatement.setString(11, appointment.getLastUpdatedBy());
    }

    public void updateAppointment(Appointment updatedAppointment) throws SQLException {
        Connection conn = DriverManager.getConnection();
        String updateStatement = "UPDATE appointments SET Contact_ID = ?, Customer_ID = ?, User_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ? "
                + "WHERE Appointment_ID = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);

        setAppointmentFields(preparedStatement, updatedAppointment);
        preparedStatement.setInt(12, updatedAppointment.getAppointmentId());

        preparedStatement.executeUpdate();
    }

    public int insertAppointment(Appointment appointment) throws SQLException {
        String insertSql = "INSERT INTO appointments (contact_id, customer_id, user_id, title, description, location, type, start, end, last_update, last_updated_by, create_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DriverManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            preparedStatement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

            setAppointmentFields(preparedStatement, appointment);
            preparedStatement.setTimestamp(12, Timestamp.valueOf(appointment.getCreateDate()));

            preparedStatement.executeUpdate();

            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating appointment failed, no ID obtained.");
            }
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
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
