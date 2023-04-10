package helper;

import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentQuery {


    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        String query = "SELECT * FROM appointments";
        try (PreparedStatement preparedStatement = JDBCHelper.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                int contactId = resultSet.getInt("Contact_ID");
                int customerId = resultSet.getInt("Customer_ID");
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
}
