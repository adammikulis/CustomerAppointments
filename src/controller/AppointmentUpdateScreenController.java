package controller;

import dao.*;
import helper.AppointmentTimeChecker;
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
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

/** Class for controlling for appointment screen
 *
 */
public class AppointmentUpdateScreenController implements Initializable {

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
    private ComboBox<Customer> appointmentCustomerComboBox;
    @FXML
    private ComboBox<User> appointmentUserComboBox;

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

    private ObservableList<Appointment> updatedAppointmentList;
    private Appointment updatedAppointment;

    /** Initialization method for appointment screen
     * Lambda expressions radio button event handlers
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTableView.setItems(updatedAppointmentList);
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

    }

    public void transferAppointment(Appointment transferredAppointment) {
        updatedAppointmentList = FXCollections.observableArrayList();
        updatedAppointmentList.add(transferredAppointment);
        updatedAppointment = transferredAppointment;
        appointmentTableView.setItems(updatedAppointmentList);

        /// Set the text fields to show the appointment's information
        appointmentIdTextField.setText(Integer.toString(transferredAppointment.getAppointmentId()));
        appointmentTypeTextField.setText(transferredAppointment.getType());
        appointmentTitleTextField.setText(transferredAppointment.getTitle());
        appointmentDescriptionTextField.setText(transferredAppointment.getDescription());
        appointmentLocationTextField.setText(transferredAppointment.getLocation());
        LocalDate localDate = transferredAppointment.getStartDateTime().toLocalDate();
        LocalTime localStartTime = transferredAppointment.getStartDateTime().toLocalTime();
        LocalTime localEndTime = transferredAppointment.getEndDateTime().toLocalTime();
        appointmentStartTimeTextField.setText(transferredAppointment.getStartTime().toString());
        appointmentEndTimeTextField.setText(transferredAppointment.getEndTime().toString());
        appointmentDatePicker.setValue(localDate);

        // Populate the contact combo box
        populateContactComboBox();
        setAppointmentContactComboBox(transferredAppointment);

        populateCustomerComboBox();
        setAppointmentCustomerComboBox(transferredAppointment);

        populateUserComboBox();
        setAppointmentUserComboBox(transferredAppointment);

    }

    private void setAppointmentUserComboBox(Appointment transferredAppointment) {
        // Get the contact for the appointment
        try {
            User selectedUser = UserDAO.getUser(transferredAppointment.getContactId());
            appointmentUserComboBox.getSelectionModel().select(selectedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppointmentCustomerComboBox(Appointment transferredAppointment) {
        // Get the contact for the appointment
        try {
            Customer selectedCustomer = CustomerDAO.getCustomer(transferredAppointment.getCustomerId());
            appointmentCustomerComboBox.getSelectionModel().select(selectedCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppointmentContactComboBox(Appointment transferredAppointment) {
        // Get the contact for the appointment
        try {
            Contact selectedContact = ContactDAO.getContact(transferredAppointment.getContactId());
            appointmentContactComboBox.getSelectionModel().select(selectedContact);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Sends user back to home screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAppointmentBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Gets all values from appointment fields and returns true if successful
     *
     * @return true if all fields have values
     */
    public boolean getValuesFromFields() {

        // Check if any field is empty and show an alert if so
        if (appointmentIdTextField.getText().trim().isEmpty() ||
                appointmentCustomerComboBox.getValue() == null ||
                appointmentUserComboBox.getValue() == null ||
                appointmentTitleTextField.getText().trim().isEmpty() ||
                appointmentDescriptionTextField.getText().trim().isEmpty() ||
                appointmentLocationTextField.getText().trim().isEmpty() ||
                appointmentTypeTextField.getText().trim().isEmpty() ||
                appointmentDatePicker.getValue() == null ||
                appointmentStartTimeTextField.getText().trim().isEmpty() ||
                appointmentEndTimeTextField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled.");
            alert.showAndWait();
            return false;
        }
        appointmentId = Integer.parseInt(appointmentIdTextField.getText());
        customerId = appointmentCustomerComboBox.getSelectionModel().getSelectedItem().getCustomerId();
        userId = appointmentUserComboBox.getSelectionModel().getSelectedItem().getUserId();
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        contactId = selectedContact.getContactId();
        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        location = appointmentLocationTextField.getText();
        type = appointmentTypeTextField.getText();
        lastUpdatedBy = LoginDAO.getCurrentUserName();
        createdBy = LoginDAO.getCurrentUserName();
        date = appointmentDatePicker.getValue();
        startTime = LocalTime.parse(appointmentStartTimeTextField.getText());
        endTime = LocalTime.parse(appointmentEndTimeTextField.getText());
        createDate = AppointmentTimeChecker.convertLocalToUTC(LocalDateTime.now());
        lastUpdate = AppointmentTimeChecker.convertLocalToUTC(LocalDateTime.now());

        startDateTime = LocalDateTime.of(date, startTime);
        endDateTime = LocalDateTime.of(date, endTime);

        return true;
    }

    /** Updates selected appointment with values from fields
     *
     * @param actionEvent
     */
    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        if (getValuesFromFields()) {
            if (AppointmentTimeChecker.appointmentChecker(appointmentId, startDateTime, endDateTime, true)) {
                // Update the selected appointment
                updatedAppointment.setCustomerId(customerId);
                updatedAppointment.setContactId(contactId);
                updatedAppointment.setUserId(userId);
                updatedAppointment.setTitle(title);
                updatedAppointment.setDescription(description);
                updatedAppointment.setLocation(location);
                updatedAppointment.setType(type);
                updatedAppointment.setStartDateTime(startDateTime);
                updatedAppointment.setEndDateTime(endDateTime);
                updatedAppointment.setLastUpdatedBy(lastUpdatedBy);
                updatedAppointment.setLastUpdate(LocalDateTime.now());

                // Save the changes to the database
                try {
                    AppointmentDAO.updateAppointment(updatedAppointment);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/AppointmentScreen.fxml"));
                    loader.load();
                    stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                    scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                refreshAppointmentListAndView();
                clearFields();
            }
        }

    }

    /** Clears appointment contact combo box
     *
     */
    private void clearAppointmentContactComboBox() {
        appointmentContactComboBox.getItems().clear();
        appointmentContactComboBox.setValue(null);
    }

    /** Clears all fields
     *
     */
    private void clearFields() {

        appointmentTableView.getSelectionModel().clearSelection();

        // Clear the input fields
        appointmentIdTextField.clear();
        appointmentTitleTextField.clear();
        appointmentDescriptionTextField.clear();
        appointmentTypeTextField.clear();
        appointmentLocationTextField.clear();
        appointmentDatePicker.getEditor().clear();
        appointmentDatePicker.setValue(null);
        appointmentStartTimeTextField.clear();
        appointmentEndTimeTextField.clear();
        clearAppointmentContactComboBox();
        clearAppointmentCustomerComboBox();
        clearAppointmentUserComboBox();
    }

    private void clearAppointmentUserComboBox() {
    }

    private void clearAppointmentCustomerComboBox() {
    }

    /** Refreshes contact combo box
     *
     */
    public void populateContactComboBox() {
        List<Contact> contacts = null;
        try {
            contacts = ContactDAO.getAllContacts();
            appointmentContactComboBox.setItems(FXCollections.observableArrayList(contacts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Refreshes customer combo box
     *
     */
    public void populateCustomerComboBox() {
        List<Customer> customers = null;
        try {
            customers = CustomerDAO.getAllCustomers();
            appointmentCustomerComboBox.setItems(FXCollections.observableArrayList(customers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Refreshes user combo box
     *
     */
    public void populateUserComboBox() {
        List<User> users = null;
        try {
            users = UserDAO.getAllUsers();
            appointmentUserComboBox.setItems(FXCollections.observableArrayList(users));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Applies current filter and refreshes table
     *
     */
    private void refreshAppointmentListAndView() {
        populateContactComboBox();
        populateCustomerComboBox();
        populateUserComboBox();
        appointmentTableView.refresh();
    }
}
