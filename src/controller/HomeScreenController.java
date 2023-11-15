package controller;

import helper.AppointmentQuery;
import helper.ContactQuery;
import javafx.scene.control.*;
import model.Appointment;
import model.Contact;
import model.ContactList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {

    @FXML
    private Label homeAppointmentAlertLabel;
    @FXML
    private TableView<Appointment> homeScheduleTableView;
    @FXML
    private TableColumn<Appointment, Integer> homeScheduleAppointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> homeScheduleTitleColumn;
    @FXML
    private TableColumn<Appointment, String> homeScheduleTypeColumn;
    @FXML
    private TableColumn<Appointment, String> homeScheduleDescriptionColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> homeScheduleStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> homeScheduleEndColumn;
    @FXML
    private TableColumn<Appointment, Integer> homeScheduleCustomerId;
    @FXML
    private ComboBox<Contact> homeContactComboBox;

    Stage stage;
    Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshContactComboBox();
        homeAppointmentAlertLabel.setText("No upcoming appointments");

        homeContactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the contactId for the appointment
                Appointment selectedAppointment = homeScheduleTableView.getSelectionModel().getSelectedItem();
                if (selectedAppointment != null) {
                    refreshAppointmentTableView(newSelection);
                }
            }
        });
    }

    private void refreshAppointmentTableView(Contact contact) {
        System.out.println("Refreshing schedule");
        try {
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            List<Appointment> appointments = appointmentQuery.getAppointmentsByContact(contact);
            homeScheduleTableView.setItems(FXCollections.observableArrayList(appointments));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onViewAllAppointmentsButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onAddUpdateClientButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ClientScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onHomeScreenExitButtonPressed(ActionEvent actionEvent) throws IOException {
        System.exit(0); // Exit the JavaFX application
    }

    public void refreshContactComboBox() {
        List<Contact> contacts = null;
        try {
            ContactQuery contactQuery = new ContactQuery();
            contacts = contactQuery.getAllContacts();
            homeContactComboBox.setItems(FXCollections.observableArrayList(contacts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
