package controller;

import helper.JDBCHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentList;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactIdColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startDateTimeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endDateTimeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    /*@FXML
    private ComboBox<AppointmentType> appointmentTypeComboBox;*/

    @FXML
    private TextField appointmentTitleTextField;

    @FXML
    private TextField appointmentDescriptionTextField;

    @FXML
    private TextField appointmentLocationTextField;

    @FXML
    private TextField appointmentContactTextField;

    @FXML
    private TextField appointmentStartDateTimeTextField;

    @FXML
    private TextField appointmentEndDateTimeTextField;

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField userIdTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTableView.setItems(AppointmentList.getAllAppointments());
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Listener for selecting a row
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the text fields to show the appointment's information
                /*appointmentTypeComboBox.setValue(newSelection.getType());*/
                appointmentTitleTextField.setText(newSelection.getTitle());
                appointmentDescriptionTextField.setText(newSelection.getDescription());
                appointmentLocationTextField.setText(newSelection.getLocation());
                appointmentContactTextField.setText(Integer.toString(newSelection.getContactId()));
                appointmentStartDateTimeTextField.setText(newSelection.getStartDateTime().toString());
                appointmentEndDateTimeTextField.setText(newSelection.getEndDateTime().toString());
                customerIdTextField.setText(Integer.toString(newSelection.getCustomerId()));
                userIdTextField.setText(Integer.toString(newSelection.getUserId()));
            }
        });
    }


    public void onAppointmentBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onSaveNewAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the values from the text fields
        int contactId = Integer.parseInt(appointmentContactTextField.getText());
        int customerId = Integer.parseInt(customerIdTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());
        String title = appointmentTitleTextField.getText();
        String description = appointmentDescriptionTextField.getText();
        String location = appointmentLocationTextField.getText();
        String type = "type"; // replace with actual value
        String createdBy = "test";
        String lastUpdatedBy = "test";
        LocalDateTime startDateTime = LocalDateTime.parse(appointmentStartDateTimeTextField.getText());
        LocalDateTime endDateTime = LocalDateTime.parse(appointmentEndDateTimeTextField.getText());
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // Create a new appointment object
        Appointment newAppointment = new Appointment(
                AppointmentList.getNextAppointmentId(),
                contactId,
                customerId,
                userId,
                title,
                description,
                location,
                type,
                createdBy,
                lastUpdatedBy,
                startDateTime,
                endDateTime,
                createDate,
                lastUpdate
        );

        // Add the new appointment to the list and refresh the table view
        AppointmentList.addAppointment(newAppointment);
        appointmentTableView.refresh();

        // Clear the text fields
        appointmentContactTextField.clear();
        customerIdTextField.clear();
        userIdTextField.clear();
        appointmentTitleTextField.clear();
        appointmentDescriptionTextField.clear();
        appointmentLocationTextField.clear();
        appointmentStartDateTimeTextField.clear();
        appointmentEndDateTimeTextField.clear();

        // Insert the new appointment into the database
        try {
            Connection connection = JDBCHelper.getConnection();

            String sql = "INSERT INTO appointments(Appointment_ID, Contact_ID, Customer_ID, User_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, newAppointment.getAppointmentId());
            ps.setInt(2, newAppointment.getContactId());
            ps.setInt(3, newAppointment.getCustomerId());
            ps.setInt(4, newAppointment.getUserId());
            ps.setString(5, newAppointment.getTitle());
            ps.setString(6, newAppointment.getDescription());
            ps.setString(7, newAppointment.getLocation());
            ps.setString(8, newAppointment.getType());
            ps.setString(9, newAppointment.getStartDateTime().toString());
            ps.setString(10, newAppointment.getEndDateTime().toString());
            ps.setString(11, newAppointment.getCreateDate().toString());
            ps.setString(12, newAppointment.getCreatedBy());
            ps.setTimestamp(13, Timestamp.valueOf(newAppointment.getLastUpdate()));
            ps.setString(14, newAppointment.getLastUpdatedBy());



            ps.executeUpdate();
            System.out.println("Created new appointment with id " + newAppointment.getAppointmentId() + " in the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    public void onDeleteAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null) {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        // Confirm the deletion with the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the appointment from the table and the database
            AppointmentList.deleteAppointment(selectedAppointment);
        }
    }

    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
    }
}
