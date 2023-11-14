package model;

import helper.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class AppointmentList {
    private static AppointmentQuery appointmentQuery = new AppointmentQuery();
    private static List<Appointment> appointments = appointmentQuery.getAppointments();

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(appointments);
    private static ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static ObservableList<Appointment> getFilteredAppointments() {
        return filteredAppointments;
    }

    public static void addAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    public static void deleteAppointment(Appointment appointment) {
        allAppointments.remove(appointment);
    }

    public static int getNextAppointmentId() {
        return -1;
    }

    public static Appointment checkUpcomingAppointments(List<Appointment> appointments) {

        LocalDateTime now = LocalDateTime.now();
        Appointment upcomingAppointment = null;

        for (Appointment appointment : appointments) {
            LocalDateTime startLocal = convertUTCToLocal(appointment.getStartDateTime());
            LocalDateTime endLocal = convertUTCToLocal(appointment.getEndDateTime());
            appointment.setStartDateTime(startLocal);
            appointment.setEndDateTime(endLocal);
            if (startLocal.isAfter(now) && startLocal.isBefore(now.plusMinutes(15))) {
                if (upcomingAppointment == null || startLocal.isBefore(upcomingAppointment.getStartDateTime())) {
                    upcomingAppointment = appointment;
                }
            }
        }

        return upcomingAppointment;
    }

    public static LocalDateTime convertLocalToUTC(LocalDateTime currentDateTime) {
        return currentDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime convertUTCToLocal(LocalDateTime UTCDateTime) {
        return UTCDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

}
