package controller;

import dao.AppointmentDAO;
import dao.ContactDAO;
import helper.AppointmentTimeChecker;
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
import report.AppointmentHoursByCountryReport;
import report.AppointmentTypeMonthCountReport;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/** Class for controlling the home screen
 *
 */
public class HomeScreenController implements Initializable {

    @FXML
    private TableView<AppointmentTypeMonthCountReport> appointmentTypeReportTableView;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, String> appointmentTypeReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> janReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> febReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> marReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> aprReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> mayReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> junReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> julReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> augReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> septReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> octReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Integer> novReportColumn;
    @FXML
    private TableColumn<AppointmentTypeMonthCountReport, Number> decReportColumn;

    @FXML
    private TableView<AppointmentHoursByCountryReport> countryHoursReportTableView;
    @FXML
    private TableColumn<AppointmentHoursByCountryReport, String> countryReportColumn;
    @FXML
    private TableColumn<AppointmentHoursByCountryReport, Long> countryHoursReportColumn;


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
    @FXML
    private Label appointmentAlertLabel;

    Stage stage;
    Parent scene;

    /** Initialization method for home screen
     * Lambda used for contact report combo box listener to trigger refresh
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshContactComboBox();
        refreshAppointmentTypeReportTableView();
        refreshAppointmentHoursByCountryReportTableView();
        refreshAppointmentAlert();

        homeContactComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                refreshHomeScheduleTableView(newSelection);
            }
        });
    }

    /** Refreshes appointment alert at top of GUI
     *
     */
    public void refreshAppointmentAlert() {
        Appointment upcomingAppointment = AppointmentTimeChecker.checkUpcomingAppointments();
        if (upcomingAppointment != null) {
            appointmentAlertLabel.setText("Upcoming appointment ID: " + upcomingAppointment.getAppointmentId() + " at: " + AppointmentTimeChecker.convertUTCToLocal(upcomingAppointment.getStartDateTime()));
        }
        else {
            appointmentAlertLabel.setText("No appointments in the next 15 minutes");
        }
    }

    /** Refreshes appointment type report tableview
     *
     */
    private void refreshAppointmentTypeReportTableView() {
        List<AppointmentTypeMonthCountReport> appointmentTypeMonthCountReportList = AppointmentDAO.getAppointmentCountByTypeAndMonth();
        ObservableList<AppointmentTypeMonthCountReport> appointmentTypeMonthReportData = FXCollections.observableArrayList(appointmentTypeMonthCountReportList);

        appointmentTypeReportColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        janReportColumn.setCellValueFactory(new PropertyValueFactory<>("janCount"));
        febReportColumn.setCellValueFactory(new PropertyValueFactory<>("febCount"));
        marReportColumn.setCellValueFactory(new PropertyValueFactory<>("marCount"));
        aprReportColumn.setCellValueFactory(new PropertyValueFactory<>("aprCount"));
        mayReportColumn.setCellValueFactory(new PropertyValueFactory<>("mayCount"));
        junReportColumn.setCellValueFactory(new PropertyValueFactory<>("junCount"));
        julReportColumn.setCellValueFactory(new PropertyValueFactory<>("julCount"));
        augReportColumn.setCellValueFactory(new PropertyValueFactory<>("augCount"));
        septReportColumn.setCellValueFactory(new PropertyValueFactory<>("septCount"));
        octReportColumn.setCellValueFactory(new PropertyValueFactory<>("octCount"));
        novReportColumn.setCellValueFactory(new PropertyValueFactory<>("novCount"));
        decReportColumn.setCellValueFactory(new PropertyValueFactory<>("decCount"));

        appointmentTypeReportTableView.setItems(appointmentTypeMonthReportData);
    }

    private void refreshAppointmentHoursByCountryReportTableView() {
        List<AppointmentHoursByCountryReport> appointmentHoursByCountryReportList = AppointmentDAO.getAppointmentHoursByCountry();
        ObservableList<AppointmentHoursByCountryReport> appointmentHoursByCountryReportData = FXCollections.observableArrayList(appointmentHoursByCountryReportList);
        countryReportColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryHoursReportColumn.setCellValueFactory(new PropertyValueFactory<>("totalHours"));

        countryHoursReportTableView.setItems(appointmentHoursByCountryReportData);
    }

    /** Refreshes home screen schedule tableview based on selected contact
     *
     * @param contact
     */
    private void refreshHomeScheduleTableView(Contact contact) {
        try {
            List<Appointment> appointments = AppointmentDAO.getAppointmentsByContact(contact);
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
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
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
            List<Contact> contacts = ContactDAO.getAllContacts();
            homeContactComboBox.setItems(FXCollections.observableArrayList(contacts));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
