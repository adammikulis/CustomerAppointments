package controller;

import helper.AppointmentQuery;
import helper.AppointmentTimeChecker;
import helper.ContactQuery;
import helper.SessionManager;
import javafx.collections.FXCollections;
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
import model.Contact;
import model.ContactList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label appointmentAlertLabel;

    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
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

    @FXML
    private TextField appointmentTitleTextField;
    @FXML
    private TextField appointmentDescriptionTextField;
    @FXML
    private TextField appointmentLocationTextField;
    @FXML
    private ComboBox<Contact> appointmentContactComboBox;
    @FXML
    private TextField appointmentTypeTextField;
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
        appointmentAlertLabel.setText("No appointments in the next 15 minutes");
        appointmentTableView.setItems(AppointmentList.getAllAppointments());
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Listener for changes in the selected item of the contact combo box
        appointmentContactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the contactId for the appointment
                Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
                if (selectedAppointment != null) {
                    selectedAppointment.setContactId(newSelection.getContactId());
                }
            }
        });

        // Listener for selecting a row
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the text fields to show the appointment's information
                appointmentTypeTextField.setText(newSelection.getType());
                appointmentTitleTextField.setText(newSelection.getTitle());
                appointmentDescriptionTextField.setText(newSelection.getDescription());
                appointmentLocationTextField.setText(newSelection.getLocation());

                // Populate the contact combo box
                refreshContactComboBox();

                // Get the contact for the appointment
                ContactQuery contactQuery = new ContactQuery();
                try {
                    Contact selectedContact = contactQuery.getContact(newSelection.getContactId());
                    appointmentContactComboBox.getSelectionModel().select(selectedContact);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                appointmentStartDateTimeTextField.setText(AppointmentList.convertUTCToLocal(newSelection.getStartDateTime()).toString());
                appointmentEndDateTimeTextField.setText(AppointmentList.convertUTCToLocal(newSelection.getEndDateTime()).toString());
                customerIdTextField.setText(Integer.toString(newSelection.getCustomerId()));
                userIdTextField.setText(Integer.toString(newSelection.getUserId()));
            }
        });

        refreshAppointmentTable();
        // Populate the contact combo box with Contact objects
        refreshContactComboBox();
    }


    public void onAppointmentBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete appointment " + selectedAppointment.getAppointmentId() +"?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the appointment from the table
            AppointmentList.deleteAppointment(selectedAppointment);

            // Delete the appointment from the database
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            appointmentQuery.deleteAppointment(selectedAppointment.getAppointmentId());
            System.out.println("Deleted appointment with id " + selectedAppointment.getAppointmentId() + " from the database.");
            clearFieldsAndRefresh();
        }
    }

    public void refreshContactComboBox() {
        List<Contact> contacts = null;
        try {
            ContactQuery contactQuery = new ContactQuery();
            contacts = contactQuery.getAllContacts();
            appointmentContactComboBox.setItems(FXCollections.observableArrayList(contacts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onAppointmentClearAllFieldsButtonPressed(ActionEvent actionEvent) {
        clearFieldsAndRefresh();
    }

    public void onCreateNewAppointmentButtonPressed(ActionEvent actionEvent) {
        // Get the values from the text fields
        int customerId = Integer.parseInt(customerIdTextField.getText());
        Contact selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        int contactId = selectedContact.getContactId();
        int userId = Integer.parseInt(userIdTextField.getText());
        String title = appointmentTitleTextField.getText();
        String description = appointmentDescriptionTextField.getText();
        String location = appointmentLocationTextField.getText();
        String type = appointmentTypeTextField.getText();
        String lastUpdatedBy = SessionManager.getInstance().getCurrentUserName();
        String createdBy = SessionManager.getInstance().getCurrentUserName();
        LocalDateTime startDateTime = AppointmentList.convertLocalToUTC(LocalDateTime.parse(appointmentStartDateTimeTextField.getText()));
        LocalDateTime endDateTime = AppointmentList.convertLocalToUTC(LocalDateTime.parse(appointmentEndDateTimeTextField.getText()));
        LocalDateTime createDate = AppointmentList.convertLocalToUTC(LocalDateTime.now());
        LocalDateTime lastUpdate = AppointmentList.convertLocalToUTC(LocalDateTime.now());
        if (AppointmentTimeChecker.businessHourChecker(startDateTime)) {
            if (!AppointmentTimeChecker.overlapChecker(customerId, startDateTime, endDateTime)) {
                // Create a new appointment object
                Appointment newAppointment = new Appointment(
                        -1,
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
                // Insert the new appointment into the database
                try {
                    AppointmentQuery appointmentQuery = new AppointmentQuery();
                    int generatedId = appointmentQuery.insertAppointment(newAppointment);
                    newAppointment.setAppointmentId(generatedId); // Update the appointment object with the generated ID
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Add the new appointment to the list and refresh the table view
                AppointmentList.addAppointment(newAppointment);
                clearFieldsAndRefresh();
                refreshContactComboBox();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please schedule during business hours (8am-10pm ET Mon-Fri)");
            alert.showAndWait();
            return;
        }
    }

    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null) {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            alert.showAndWait();
            return;
        }
        // Get the values from the text fields
        int customerId = Integer.parseInt(customerIdTextField.getText());
        Contact selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        int contactId = selectedContact.getContactId();
        int userId = Integer.parseInt(userIdTextField.getText());
        String title = appointmentTitleTextField.getText();
        String description = appointmentDescriptionTextField.getText();
        String location = appointmentLocationTextField.getText();
        String type = appointmentTypeTextField.getText();
        String lastUpdatedBy = SessionManager.getInstance().getCurrentUserName();
        LocalDateTime startDateTime = LocalDateTime.parse(appointmentStartDateTimeTextField.getText());
        LocalDateTime endDateTime = LocalDateTime.parse(appointmentEndDateTimeTextField.getText());
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // Update the selected appointment
        selectedAppointment.setCustomerId(customerId);
        selectedAppointment.setContactId(contactId);
        selectedAppointment.setUserId(userId);
        selectedAppointment.setTitle(title);
        selectedAppointment.setDescription(description);
        selectedAppointment.setLocation(location);
        selectedAppointment.setType(type);
        selectedAppointment.setStartDateTime(AppointmentList.convertLocalToUTC(startDateTime));
        selectedAppointment.setEndDateTime(AppointmentList.convertLocalToUTC(endDateTime));
        selectedAppointment.setLastUpdatedBy(lastUpdatedBy);
        selectedAppointment.setLastUpdate(AppointmentList.convertLocalToUTC(LocalDateTime.now()));

        // Save the changes to the database
        try {
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            appointmentQuery.updateAppointment(selectedAppointment);
            System.out.println("Updated appointment with id " + selectedAppointment.getAppointmentId() + " in the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearFieldsAndRefresh();
        refreshContactComboBox();
    }

    private void clearAppointmentContactComboBox() {
        appointmentContactComboBox.getItems().clear();
        appointmentContactComboBox.setValue(null);
    }
    private void clearFieldsAndRefresh() {
        // Clear the input fields
        clearAppointmentContactComboBox();
        customerIdTextField.clear();
        userIdTextField.clear();
        appointmentTitleTextField.clear();
        appointmentDescriptionTextField.clear();
        appointmentTypeTextField.clear();
        appointmentLocationTextField.clear();
        appointmentStartDateTimeTextField.clear();
        appointmentEndDateTimeTextField.clear();

        // Refresh table view and clear selection
        refreshAppointmentTable();
        refreshContactComboBox();
        appointmentTableView.getSelectionModel().clearSelection();
    }

    public void refreshAppointmentTable() {
        Appointment upcomingAppointment = AppointmentList.checkUpcomingAppointments();
        if (upcomingAppointment != null) {
            appointmentAlertLabel.setText("Upcoming appointment ID: " + upcomingAppointment.getAppointmentId() + " at: " + AppointmentList.convertUTCToLocal(upcomingAppointment.getStartDateTime()));
        }
        else {
            appointmentAlertLabel.setText("No appointments in the next 15 minutes");
        }
        appointmentTableView.setItems(FXCollections.observableArrayList(AppointmentList.getAllAppointments()));
        appointmentTableView.refresh();
    }


}
