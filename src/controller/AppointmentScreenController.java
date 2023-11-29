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
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for controlling the appointment screen
 *
 */
public class AppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;
    
    @FXML
    protected RadioButton noFilterRadioButton;
    @FXML
    protected RadioButton viewByWeekRadioButton;
    @FXML
    protected RadioButton viewByMonthRadioButton;


    @FXML
    protected TableView<Appointment> appointmentTableView;
    @FXML
    protected TableColumn<Appointment, String> contactColumn;
    @FXML
    protected TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    protected TableColumn<Appointment, String> titleColumn;
    @FXML
    protected TableColumn<Appointment, String> descriptionColumn;
    @FXML
    protected TableColumn<Appointment, String> locationColumn;
    @FXML
    protected TableColumn<Appointment, String> typeColumn;
    @FXML
    protected TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    protected TableColumn<Appointment, String> startTimeColumn;
    @FXML
    protected TableColumn<Appointment, String> endTimeColumn;
    @FXML
    protected TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    protected TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    protected TextField appointmentIdTextField;
    @FXML
    protected TextField appointmentTitleTextField;
    @FXML
    protected TextField appointmentDescriptionTextField;
    @FXML
    protected TextField appointmentLocationTextField;
    @FXML
    protected ComboBox<Contact> appointmentContactComboBox;
    @FXML
    protected TextField appointmentTypeTextField;
    @FXML
    protected DatePicker appointmentDatePicker;
    @FXML
    protected TextField appointmentStartTimeTextField;
    @FXML
    protected TextField appointmentEndTimeTextField;
    @FXML
    protected ComboBox<Customer> appointmentCustomerComboBox;
    @FXML
    protected ComboBox<User> appointmentUserComboBox;

    protected int appointmentId;
    protected int customerId;
    protected Contact selectedContact;
    protected int contactId;
    protected int userId;
    protected String title;
    protected String description;
    protected String location;
    protected String type;
    protected String lastUpdatedBy;
    protected String createdBy;
    protected LocalDate date;
    protected LocalTime startTime;
    protected LocalTime endTime;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected LocalDateTime createDate;
    protected LocalDateTime lastUpdate;
    protected Appointment selectedAppointment;

    protected ObservableList<Appointment> filteredAppointments;
    protected ObservableList<Appointment> allAppointments;

    /** Initialization method for appointment screen
     * Lambda expressions for radio button event handlers
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allAppointments = AppointmentDAO.getAllAppointments();
        filteredAppointments = allAppointments;
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

        populateCustomerComboBox();
        populateContactComboBox();
        populateUserComboBox();

        // Listeners for radio buttons (lambda)
        noFilterRadioButton.setOnAction(event -> showAllAppointments());
        viewByWeekRadioButton.setOnAction(event -> filterAppointmentsByWeek());
        viewByMonthRadioButton.setOnAction(event -> filterAppointmentsByMonth());
    }

    /** Displays every appointment with no filter
     *
     */
    protected void showAllAppointments() {
        filteredAppointments.clear();
        allAppointments = AppointmentDAO.getAllAppointments();
        filteredAppointments.addAll(allAppointments);
    }

    /** Displays all appointments this week (MON-SUN)
     *
     */
    protected void filterAppointmentsByWeek() {

        allAppointments = AppointmentDAO.getAllAppointments();
        filteredAppointments.clear();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        for (Appointment appointment : allAppointments) {
            LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
            if ((appointmentDate.isEqual(startOfWeek) || appointmentDate.isAfter(startOfWeek)) &&
                    (appointmentDate.isEqual(endOfWeek) || appointmentDate.isBefore(endOfWeek))) {
                filteredAppointments.add(appointment);
            }
        }
    }

    /** Displays all appointments this calendar month
     *
     */
    protected void filterAppointmentsByMonth() {
        allAppointments = AppointmentDAO.getAllAppointments();
        filteredAppointments.clear();
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        for (Appointment appointment : allAppointments) {
            LocalDate appointmentDate = appointment.getStartDateTime().toLocalDate();
            if (!appointmentDate.isBefore(startOfMonth) && !appointmentDate.isAfter(endOfMonth)) {
                filteredAppointments.add(appointment);
            }
        }
    }

    /** Applies the current radio button selection as a filter
     *
     */
    protected void applyCurrentFilter() {
        if (noFilterRadioButton.isSelected()) {
            showAllAppointments();
        }
        else if (viewByWeekRadioButton.isSelected()) {
            filterAppointmentsByWeek();
        }
        else if (viewByMonthRadioButton.isSelected()) {
            filterAppointmentsByMonth();
        }
    }

    /** Sends user back to home screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAppointmentBackButtonPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Deletes selected appointment after a confirmation
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onDeleteAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null) {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        // Confirm the deletion with the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm delete -- Appt ID: " + selectedAppointment.getAppointmentId() +" Type: " + selectedAppointment.getType());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            // Delete the appointment from the database
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            appointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId());
            refreshAppointmentListAndView();
        }
    }

    /** Creates new appointment if there are no conflicts and all
     *
     * @param actionEvent
     */
    public void onCreateNewAppointmentButtonPressed(ActionEvent actionEvent) {
        // Get the values from the text fields
        if (getValuesFromFields()) {
            if (AppointmentTimeChecker.appointmentChecker(appointmentId, startDateTime, endDateTime, false)) {
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
                    AppointmentDAO appointmentDAO = new AppointmentDAO();
                    appointmentDAO.insertAppointment(newAppointment);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Add the new appointment to the list and refresh the table view
                refreshAppointmentListAndView();
            }
        }
    }

    /** Gets all values from appointment fields and returns true if successful
     *
     * @return true if all fields have values
     */
    public boolean getValuesFromFields() {

        // Check if any field is empty and show an alert if so
        if (    appointmentCustomerComboBox.getValue() == null ||
                appointmentContactComboBox.getValue() == null ||
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
        customerId = appointmentCustomerComboBox.getSelectionModel().getSelectedItem().getCustomerId();
        userId = appointmentUserComboBox.getSelectionModel().getSelectedItem().getUserId();
        selectedContact = appointmentContactComboBox.getSelectionModel().getSelectedItem();
        contactId = selectedContact.getContactId();
        title = appointmentTitleTextField.getText();
        description = appointmentDescriptionTextField.getText();
        location = appointmentLocationTextField.getText();
        type = appointmentTypeTextField.getText();
        lastUpdatedBy = UserDAO.getCurrentUser().getUserName();
        createdBy = UserDAO.getCurrentUser().getUserName();
        date = appointmentDatePicker.getValue();
        startTime = LocalTime.parse(appointmentStartTimeTextField.getText());
        endTime = LocalTime.parse(appointmentEndTimeTextField.getText());
        createDate = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();

        startDateTime = LocalDateTime.of(date, startTime);
        endDateTime = LocalDateTime.of(date, endTime);

        return true;
    }

    /** Updates selected appointment with values from fields
     *
     * @param actionEvent
     */
    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {

        try {
            selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentUpdateScreen.fxml"));
            loader.load();

            AppointmentUpdateScreenController ASController = loader.getController();
            ASController.transferAppointment(selectedAppointment);

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e)
        {
            // No appointment selected, show error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

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
    protected void refreshAppointmentListAndView() {
        applyCurrentFilter();
        populateContactComboBox();
        populateCustomerComboBox();
        populateUserComboBox();
        appointmentTableView.refresh();
    }
}
