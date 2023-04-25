package controller;

import helper.AppointmentQuery;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    

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

    private String loggedInUserName;

    // Get the values from the text fields
    int customerId;
    Contact selectedContact;
    int contactId;
    int userId;
    String title;
    String description;
    String location;
    String type;
    String createdBy;
    String lastUpdatedBy;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    LocalDateTime createDate;
    LocalDateTime lastUpdate;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInUserName = SessionManager.getInstance().getCurrentUserName();

        populateAppointmentTableView();

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

                // Get the contact name for the appointment
                String contactName = null;
                List<String> contactNames = null;
                ContactQuery contactQuery = new ContactQuery();
                try {
                    contactNames = contactQuery.getAllContactNames();
                    Contact selectedContact = contactQuery.getContact(newSelection.getContactId());
                    appointmentContactComboBox.getSelectionModel().select(selectedContact);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<Contact> contacts = ContactList.getContactsByNames(contactNames);
                appointmentContactComboBox.setItems(FXCollections.observableArrayList(contacts));

                appointmentStartDateTimeTextField.setText(newSelection.getStartDateTime().toString());
                appointmentEndDateTimeTextField.setText(newSelection.getEndDateTime().toString());
                customerIdTextField.setText(Integer.toString(newSelection.getCustomerId()));
                userIdTextField.setText(Integer.toString(newSelection.getUserId()));
            }
        });


        // Populate the contact combo box with Contact objects
        List<String> contactNames = null;
        try {
            ContactQuery contactQuery = new ContactQuery();
            contactNames = contactQuery.getAllContactNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Contact> contacts = ContactList.getContactsByNames(contactNames);
        appointmentContactComboBox.setItems(FXCollections.observableArrayList(contacts));
    }

    private void populateAppointmentTableView() {
        appointmentTableView.getItems().clear();
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

        appointmentTableView.getSortOrder().clear(); // Clear previous sort order
        appointmentTableView.getSortOrder().add(appointmentIdColumn);
        appointmentTableView.sort();
    }






    public void onAppointmentBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onSaveNewAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        // Get the values from the text fields
        getAllFields();

        // Create a new appointment object without an appointment ID
        Appointment newAppointment = new Appointment(
                -1, // temporary invalid appointment ID
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
            int generatedAppointmentId = appointmentQuery.insertAppointment(newAppointment);
            System.out.println("Created new appointment with id " + generatedAppointmentId + " in the database.");
            newAppointment.setAppointmentId(generatedAppointmentId); // Set the actual appointment ID
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Add the new appointment to the list
        AppointmentList.addAppointment(newAppointment);
        clearFieldsAndRefresh();
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
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            appointmentQuery.deleteAppointment(selectedAppointment.getAppointmentId());
            AppointmentList.deleteAppointment(selectedAppointment);
        }
    }


    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        // Get the values from the text fields
        getAllFields();

        // Update the selected appointment object with the new values
        selectedAppointment.setContactId(contactId);
        selectedAppointment.setCustomerId(customerId);
        selectedAppointment.setUserId(userId);
        selectedAppointment.setTitle(title);
        selectedAppointment.setDescription(description);
        selectedAppointment.setLocation(location);
        selectedAppointment.setType(type);
        selectedAppointment.setStartDateTime(startDateTime);
        selectedAppointment.setEndDateTime(endDateTime);
        selectedAppointment.setLastUpdate(lastUpdate);
        selectedAppointment.setLastUpdatedBy(lastUpdatedBy);

        // Update the appointment in the database
        try {
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            appointmentQuery.updateAppointment(selectedAppointment);
            System.out.println("Updated appointment with id " + selectedAppointment.getAppointmentId() + " in the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearFieldsAndRefresh();
    }



    public void onAppointmentClearAllFieldsButtonPressed(ActionEvent actionEvent) {
        clearFieldsAndRefresh();
    }

    private void getAllFields() {
        // Get the values from the text fields
        customerId = Integer.parseInt(customerIdTextField.getText());
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        contactId = selectedContact.getContactId();
        userId = Integer.parseInt(userIdTextField.getText());
        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        location = appointmentLocationTextField.getText();
        type = appointmentTypeTextField.getText();
        createdBy = loggedInUserName;
        lastUpdatedBy = loggedInUserName;
        startDateTime = LocalDateTime.parse(appointmentStartDateTimeTextField.getText());
        endDateTime = LocalDateTime.parse(appointmentEndDateTimeTextField.getText());
        createDate = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();
    }

    private void clearFieldsAndRefresh() {
        // Clear the text fields
        clearAppointmentContactComboBox();
        customerIdTextField.clear();
        userIdTextField.clear();
        appointmentTitleTextField.clear();
        appointmentDescriptionTextField.clear();
        appointmentLocationTextField.clear();
        appointmentStartDateTimeTextField.clear();
        appointmentEndDateTimeTextField.clear();
        appointmentTypeTextField.clear();

        appointmentTableView.refresh();

        appointmentTableView.getSelectionModel().clearSelection();
    }

    private void clearAppointmentContactComboBox() {
        appointmentContactComboBox.getItems().clear();
        appointmentContactComboBox.setValue(null);
    }
}
