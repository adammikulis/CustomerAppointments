package controller;

import helper.AppointmentQuery;
import helper.AppointmentTimeChecker;
import helper.ContactQuery;
import helper.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {


    Stage stage;
    Parent scene;

    @FXML
    private ToggleGroup ViewByGroup;
    @FXML
    private RadioButton noFilterRadioButton;
    @FXML
    private RadioButton viewByWeekRadioButton;
    @FXML
    private RadioButton viewByMonthRadioButton;
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
    private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TextField appointmentIdTextField;
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
    private DatePicker appointmentDatePicker;
    @FXML
    private TextField appointmentStartTimeTextField;
    @FXML
    private TextField appointmentEndTimeTextField;
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField userIdTextField;

    private ObservableList<Appointment> allAppointments;
    private ObservableList<Appointment> filteredAppointments;
    private int appointmentId;
    private int customerId;
    private Contact selectedContact;
    private int contactId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String lastUpdatedBy;
    private String createdBy;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allAppointments = AppointmentList.getAllAppointments();
        filteredAppointments = allAppointments;
        appointmentAlertLabel.setText("<Appointment Alert>");
        appointmentTableView.setItems(filteredAppointments);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // Listener for changes in the selected item of the contact combo box
        appointmentContactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the contactId for the appointment
                Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            }
        });

        // Listener for selecting a row
        appointmentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the text fields to show the appointment's information
                appointmentIdTextField.setText(Integer.toString(newSelection.getAppointmentId()));
                appointmentTypeTextField.setText(newSelection.getType());
                appointmentTitleTextField.setText(newSelection.getTitle());
                appointmentDescriptionTextField.setText(newSelection.getDescription());
                appointmentLocationTextField.setText(newSelection.getLocation());
                LocalDate localDate = newSelection.getStartDateTime().toLocalDate();
                LocalTime localStartTime = newSelection.getStartDateTime().toLocalTime();
                LocalTime localEndTime = newSelection.getEndDateTime().toLocalTime();

                appointmentDatePicker.setValue(localDate);
                appointmentStartTimeTextField.setText(localStartTime.toString());
                appointmentEndTimeTextField.setText(localEndTime.toString());

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

                appointmentStartTimeTextField.setText(newSelection.getStartTime().toString());
                appointmentEndTimeTextField.setText(newSelection.getEndTime().toString());
                customerIdTextField.setText(Integer.toString(newSelection.getCustomerId()));
                userIdTextField.setText(Integer.toString(newSelection.getUserId()));
            }
        });

        // Listeners for radio button
        noFilterRadioButton.setOnAction(event -> showAllAppointments());
        viewByWeekRadioButton.setOnAction(event -> filterAppointmentsByWeek());
        viewByMonthRadioButton.setOnAction(event -> filterAppointmentsByMonth());

        showAllAppointments();
    }

    private void showAllAppointments() {
        filteredAppointments = AppointmentList.getAllAppointments();
        clearFieldsAndRefresh();
    }
    private void filterAppointmentsByWeek() {
        LocalDate today = LocalDate.now();


        clearFieldsAndRefresh();
    }

    private void filterAppointmentsByMonth() {

        clearFieldsAndRefresh();
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
            clearFieldsAndRefresh();
        }
    }

    public void onAppointmentClearAllFieldsButtonPressed(ActionEvent actionEvent) {
        clearFieldsAndRefresh();
    }

    public void onCreateNewAppointmentButtonPressed(ActionEvent actionEvent) {
        // Get the values from the text fields
        getValuesFromFields();
        if (AppointmentTimeChecker.appointmentChecker(appointmentId, customerId, startDateTime, endDateTime, false)) {
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
        }
    }

    public void getValuesFromFields() {
        appointmentId = Integer.parseInt(appointmentIdTextField.getText());
        customerId = Integer.parseInt(customerIdTextField.getText());
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        contactId = selectedContact.getContactId();
        userId = Integer.parseInt(userIdTextField.getText());
        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        location = appointmentLocationTextField.getText();
        type = appointmentTypeTextField.getText();
        lastUpdatedBy = SessionManager.getInstance().getCurrentUserName();
        createdBy = SessionManager.getInstance().getCurrentUserName();
        date = appointmentDatePicker.getValue();
        startTime = LocalTime.parse(appointmentStartTimeTextField.getText());
        endTime = LocalTime.parse(appointmentEndTimeTextField.getText());
        createDate = AppointmentList.convertLocalToUTC(LocalDateTime.now());
        lastUpdate = AppointmentList.convertLocalToUTC(LocalDateTime.now());

        startDateTime = LocalDateTime.of(date, startTime);
        endDateTime = LocalDateTime.of(date, endTime);
    }

    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        getValuesFromFields();

        if (AppointmentTimeChecker.appointmentChecker(appointmentId, customerId, startDateTime, endDateTime, true)) {
            // Update the selected appointment
            selectedAppointment.setCustomerId(customerId);
            selectedAppointment.setContactId(contactId);
            selectedAppointment.setUserId(userId);
            selectedAppointment.setTitle(title);
            selectedAppointment.setDescription(description);
            selectedAppointment.setLocation(location);
            selectedAppointment.setType(type);
            selectedAppointment.setStartDateTime(startDateTime);
            selectedAppointment.setEndDateTime(endDateTime);
            selectedAppointment.setLastUpdatedBy(lastUpdatedBy);
            selectedAppointment.setLastUpdate(LocalDateTime.now());

            // Save the changes to the database
            try {
                AppointmentQuery appointmentQuery = new AppointmentQuery();
                appointmentQuery.updateAppointment(selectedAppointment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            clearFieldsAndRefresh();
        }
    }

    private void clearAppointmentContactComboBox() {
        appointmentContactComboBox.getItems().clear();
        appointmentContactComboBox.setValue(null);
    }
    private void clearFieldsAndRefresh() {

        appointmentTableView.getSelectionModel().clearSelection();

        // Clear the input fields
        appointmentIdTextField.clear();
        customerIdTextField.clear();
        userIdTextField.clear();
        appointmentTitleTextField.clear();
        appointmentDescriptionTextField.clear();
        appointmentTypeTextField.clear();
        appointmentLocationTextField.clear();
        appointmentDatePicker.getEditor().clear();
        appointmentStartTimeTextField.clear();
        appointmentEndTimeTextField.clear();
        clearAppointmentContactComboBox();

        // Refresh table view
        refreshContactComboBox();
        refreshAppointmentTable();
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

    public void refreshAppointmentTable() {
        Appointment upcomingAppointment = AppointmentList.checkUpcomingAppointments();
        if (upcomingAppointment != null) {
            appointmentAlertLabel.setText("Upcoming appointment ID: " + upcomingAppointment.getAppointmentId() + " at: " + AppointmentList.convertUTCToLocal(upcomingAppointment.getStartDateTime()));
        }
        else {
            appointmentAlertLabel.setText("No appointments in the next 15 minutes");
        }
        appointmentTableView.setItems(FXCollections.observableArrayList(filteredAppointments));
        appointmentTableView.refresh();
    }
}
