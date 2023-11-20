package controller;

import helper.AppointmentQuery;
import helper.ContactQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import report.AppointmentContactCount;
import report.AppointmentMonthCount;
import report.AppointmentTypeCount;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/** Controller for Home Screen
 *
 */
public class HomeScreenController implements Initializable {


    @FXML
    private TableView<AppointmentTypeCount> appointmentTypeReportTableView;
    @FXML
    private TableColumn<AppointmentTypeCount, String> appointmentTypeReportColumn;
    @FXML
    private TableColumn<AppointmentTypeCount, Integer> appointmentTypeTotalReportColumn;
    @FXML
    private TableView<AppointmentMonthCount> appointmentMonthTotalReportTableView;
    @FXML
    private TableColumn<AppointmentMonthCount, String> appointmentMonthReportColumn;
    @FXML
    private TableColumn<AppointmentMonthCount, Integer> appointmentMonthTotalReportColumn;
    @FXML
    private TableView<AppointmentContactCount> appointmentContactTotalReportTableView;
    @FXML
    private TableColumn<AppointmentContactCount, String> appointmentContactReportColumn;
    @FXML
    private TableColumn<AppointmentContactCount, Integer> appointmentContactTotalReportColumn;

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

    /** Initialization method for home screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshContactComboBox();
        refreshAppointmentTypeReportTableView();
        refreshAppointmentMonthReportTableView();
        refreshAppointmentContactReportTableView();

        homeContactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                refreshHomeScheduleTableView(newSelection);
            }
        });
    }

    /** Refreshes appointment type report tableview
     *
     */
    private void refreshAppointmentTypeReportTableView() {
        List<AppointmentTypeCount> appointmentTypeCount = AppointmentQuery.getAppointmentCountByType();
        ObservableList<AppointmentTypeCount> appointmentTypeCountList = FXCollections.observableArrayList(appointmentTypeCount);

        appointmentTypeReportColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentTypeTotalReportColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        appointmentTypeReportTableView.setItems(appointmentTypeCountList);
    }
    /** Refreshes appointment month report tableview
     *
     */
    private void refreshAppointmentMonthReportTableView() {
        List<AppointmentMonthCount> appointmentMonthCount = AppointmentQuery.getAppointmentCountByMonth();
        ObservableList<AppointmentMonthCount> appointmentMonthCountList = FXCollections.observableArrayList(appointmentMonthCount);

        appointmentMonthReportColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        appointmentMonthTotalReportColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        appointmentMonthTotalReportTableView.setItems(appointmentMonthCountList);
    }

    /** Refreshes appointment contact report tableview
     *
     */
    private void refreshAppointmentContactReportTableView() {
        List<AppointmentContactCount> appointmentContactCount = AppointmentQuery.getAppointmentCountByContact();
        ObservableList<AppointmentContactCount> appointmentContactCountList = FXCollections.observableArrayList(appointmentContactCount);

        appointmentContactReportColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentContactTotalReportColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        appointmentContactTotalReportTableView.setItems(appointmentContactCountList);
    }


    /** Refreshes home screen schedule tableview based on selected contact
     *
     * @param contact
     */
    private void refreshHomeScheduleTableView(Contact contact) {
        try {
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            List<Appointment> appointments = appointmentQuery.getAppointmentsByContact(contact);
            homeScheduleTableView.setItems(FXCollections.observableArrayList(appointments));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        homeScheduleAppointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        homeScheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        homeScheduleDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        homeScheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        homeScheduleStartColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        homeScheduleEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        homeScheduleCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /** Takes user to appointment screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onViewAllAppointmentsButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Takes user to client screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAddUpdateClientButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ClientScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Exits program
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onHomeScreenExitButtonPressed(ActionEvent actionEvent) throws IOException {
        System.exit(0); // Exit the JavaFX application
    }

    /** Refreshes contact combo box
     *
     */
    private void refreshContactComboBox() {
        try {
            ContactQuery contactQuery = new ContactQuery();
            List<Contact> contacts = contactQuery.getAllContacts();
            homeContactComboBox.setItems(FXCollections.observableArrayList(contacts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
