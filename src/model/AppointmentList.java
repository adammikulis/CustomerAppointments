package model;

import helper.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/** Class for creating a list of appointments*/
public class AppointmentList {
    private static AppointmentQuery appointmentQuery = new AppointmentQuery();
    private static List<Appointment> appointments = appointmentQuery.getAppointments();

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(appointments);

    /** Returns a list of all appointments
     *
     * @return allAppointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /** Adds appointment to list
     *
     * @param appointment appointment to add
     */
    public static void addAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    /** Deletes appointment from list
     *
     * @param appointment appointment to delete
     */
    public static void deleteAppointment(Appointment appointment) {
        allAppointments.remove(appointment);
    }

    /** Returns possible appointment in the next 15 minutes
     *
     * @return upcomingAppointment
     */
    public static Appointment checkUpcomingAppointments() {

        LocalDateTime now = LocalDateTime.now();
        Appointment upcomingAppointment = null;

        for (Appointment appointment : getAllAppointments()) {
            LocalDateTime startLocal = convertUTCToLocal(appointment.getStartDateTime());
            if (startLocal.isAfter(now) && startLocal.isBefore(now.plusMinutes(15))) {
                if (upcomingAppointment == null || startLocal.isBefore(upcomingAppointment.getStartDateTime())) {
                    upcomingAppointment = appointment;
                }
            }
        }

        return upcomingAppointment;
    }

    /** Converts local time to UTC
     *
     * @param localDateTime local time to convert to UTC
     * @return UTC time
     */
    public static LocalDateTime convertLocalToUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    /** Converts UTC to local time
     *
     * @param UTCDateTime local time to convert to UTC
     * @return local time
     */
    public static LocalDateTime convertUTCToLocal(LocalDateTime UTCDateTime) {
        return UTCDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    /** Converts local time to eastern time
     *
     * @param localDateTime local time to convert to eastern time
     * @return eastern time
     */
    public static LocalDateTime convertUTCToEastern(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime();
    }

    /** Converts eastern to local time
     *
     * @param easternDateTime local time to convert to UTC
     * @return local time
     */
    public static LocalDateTime convertEasternToUTC(LocalDateTime easternDateTime) {
        return easternDateTime.atZone(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

}
