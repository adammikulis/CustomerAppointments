package controller;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
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

        // Listener for selecting what row
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected: " + newSelection.getAppointmentId());
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
    }

    public void onCopyAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to copy.");
            alert.showAndWait();
            return;
        }

        int appointmentId = AppointmentList.getNextAppointmentId();
        int contactId = selectedAppointment.getContactId();
        int customerId = selectedAppointment.getCustomerId();
        int userId = selectedAppointment.getUserId();
        String title = selectedAppointment.getTitle() + " (Copy)";
        String description = selectedAppointment.getDescription();
        String location = selectedAppointment.getLocation();
        String type = selectedAppointment.getType();
        String createdBy = "test";
        String lastUpdatedBy = "test";
        LocalDateTime startDateTime = selectedAppointment.getStartDateTime();
        LocalDateTime endDateTime = selectedAppointment.getEndDateTime();
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        Appointment newAppointment = new Appointment(appointmentId, contactId, customerId, userId, title, description, location, type, createdBy, lastUpdatedBy, startDateTime, endDateTime, createDate, lastUpdate);

        AppointmentList.addAppointment(newAppointment);

        appointmentTableView.refresh();

        System.out.println("Copied appointment with id " + appointmentId);
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

}
