package helper;

import javafx.scene.control.Alert;
import model.Appointment;
import model.AppointmentList;
import helper.AppointmentQuery;
import model.Client;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;

public class AppointmentTimeChecker {
    public static boolean businessHourChecker(LocalDateTime localStartDateTime, LocalDateTime localEndDateTime) {
        LocalDateTime easternStartTime = AppointmentList.convertUTCToEastern(AppointmentList.convertLocalToUTC(localStartDateTime));
        LocalDateTime easternEndTime = AppointmentList.convertUTCToEastern(AppointmentList.convertLocalToUTC(localStartDateTime));
        DayOfWeek startDayOfWeek = easternStartTime.getDayOfWeek();
        DayOfWeek endDayOfWeek = easternEndTime.getDayOfWeek();

        // True if appointment start and end are 8am-10pm (ET) Mon-Fri
        return ((easternStartTime.getHour() >= 8 && easternStartTime.getHour() <= 22) && (startDayOfWeek != DayOfWeek.SATURDAY && startDayOfWeek != DayOfWeek.SUNDAY) &&
                (easternEndTime.getHour() >= 8 && easternEndTime.getHour() <= 22) && (endDayOfWeek != DayOfWeek.SATURDAY && endDayOfWeek != DayOfWeek.SUNDAY));
    }

    public static boolean overlapChecker(int currentAppointmentId, int clientId, LocalDateTime startUTCDateTime, LocalDateTime endUTCDateTime, boolean update) {
        AppointmentQuery appointmentQuery = new AppointmentQuery();
        List<Appointment> appointmentList = appointmentQuery.getAppointmentsByClient(clientId);

        for (Appointment appointment : appointmentList) {
            if (update) {
                // Skip the current appointment
                if (appointment.getAppointmentId() == currentAppointmentId) {
                    continue;
                }
            }

            LocalDateTime currentStart = appointment.getStartDateTime();
            LocalDateTime currentEnd = appointment.getEndDateTime();

            // Check for overlap with starting time
            if ((startUTCDateTime.isEqual(currentStart) || startUTCDateTime.isAfter(currentStart)) &&
                    (startUTCDateTime.isBefore(currentEnd))) {
                return true;
            }

            // Check for overlap with end time
            if ((endUTCDateTime.isEqual(currentStart) || endUTCDateTime.isAfter(currentStart)) &&
                    (endUTCDateTime.isBefore(currentEnd))) {
                return true;
            }
        }
        return false;
    }


    public static boolean appointmentChecker(int currentAppointmentid, int clientId, LocalDateTime startUTCDateTime, LocalDateTime endUTCDateTime, boolean update) {
        if (businessHourChecker(startUTCDateTime, endUTCDateTime)) {
            if (!overlapChecker(currentAppointmentid, clientId, startUTCDateTime, endUTCDateTime, update)) {
                return true;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Time overlaps existing appointment.");
                alert.showAndWait();
                return false;
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Schedule during regular hours: 8am-10pm (ET) Mon-Fri.");
            alert.showAndWait();
            return false;
        }
    }
}
