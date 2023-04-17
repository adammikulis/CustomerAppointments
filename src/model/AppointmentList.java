package model;

import helper.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        int maxId = 0;
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentId() > maxId) {
                maxId = appointment.getAppointmentId();
            }
        }
        return maxId + 1;
    }
}
