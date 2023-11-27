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

/** Class for controlling the appointment update screen
 *
 */
public class AppointmentUpdateScreenController extends AppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private ObservableList<Appointment> updatedAppointmentList;
    private Appointment updatedAppointment;

    /** Initialization method for appointment screen
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

    /** Transfers the selected appointment from the previous screen to be updated
     *
     * @param transferredAppointment
     */
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
        setAppointmentContactComboBox();

        populateCustomerComboBox();
        setAppointmentCustomerComboBox();

        populateUserComboBox();
        setAppointmentUserComboBox();

    }

    /** Sets the user combo box based on current appointment
     *
     */
    private void setAppointmentUserComboBox() {
        // Get the contact for the appointment
        try {
            User selectedUser = UserDAO.getUserById(updatedAppointment.getContactId());
            appointmentUserComboBox.getSelectionModel().select(selectedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Sets the customer combo box based on current appointment
     *
     */
    private void setAppointmentCustomerComboBox() {
        // Get the contact for the appointment
        try {
            Customer selectedCustomer = CustomerDAO.getCustomer(updatedAppointment.getCustomerId());
            appointmentCustomerComboBox.getSelectionModel().select(selectedCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Sets the contact combo box based on current appointment
     *
     */
    private void setAppointmentContactComboBox() {
        // Get the contact for the appointment
        try {
            Contact selectedContact = ContactDAO.getContact(updatedAppointment.getContactId());
            appointmentContactComboBox.getSelectionModel().select(selectedContact);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Sends user back to Appointment screen
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

    /** Updates selected appointment with values from fields and returns to Appointment screen
     *
     * @param actionEvent
     */
    public void onUpdateAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        if (getValuesFromFields()) {
            appointmentId = updatedAppointment.getAppointmentId();
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
            }
        }
    }
}
