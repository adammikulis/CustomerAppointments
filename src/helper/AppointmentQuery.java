package helper;

import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AppointmentQuery {


    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        String query = "SELECT * FROM appointments";
        try (PreparedStatement preparedStatement = JDBCHelper.getConnection().prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int appointment_ID = resultSet.getInt("Appointment_ID");
                int contact_ID = resultSet.getInt("Contact_ID");
                int customer_ID = resultSet.getInt("Customer_ID");
                int user_ID = resultSet.getInt("User_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                String created_By = resultSet.getString("Created_By");
                String last_Updated_By = resultSet.getString("Last_Updated_By");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                LocalDateTime create_Date = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                LocalDateTime last_Update = resultSet.getTimestamp("Last_Update").toLocalDateTime();

                appointments.add(new Appointment(appointment_ID, contact_ID, customer_ID, user_ID, title, description, location, type, created_By, last_Updated_By, start, end, create_Date, last_Update));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
