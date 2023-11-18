package helper;

import model.Appointment;
import model.AppointmentList;
import helper.AppointmentQuery;
import model.Client;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;

public class AppointmentTimeChecker {
    public static boolean businessHourChecker(LocalDateTime startUTCDateTime) {
        LocalDateTime easternTime = AppointmentList.convertUTCToLocal(startUTCDateTime);
        DayOfWeek dayOfWeek = easternTime.getDayOfWeek();
        // True if appointment is 8am-10pm (ET) Mon-Fri
        return ((easternTime.getHour() >= 8 && easternTime.getHour() <= 22) && (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY));
    }

    public static boolean overlapChecker(int clientId, LocalDateTime startUTCDateTime, LocalDateTime endUTCDateTime) {
        AppointmentQuery appointmentQuery = new AppointmentQuery();
        List<Appointment> appointmentList = appointmentQuery.getAppointmentsByClient(clientId);

        for (Appointment appointment : appointmentList) {
            LocalDateTime currentStart = appointment.getStartDateTime();
            LocalDateTime currentEnd = appointment.getEndDateTime();

            // Check for overlap with starting time
            if ((startUTCDateTime.isEqual(currentStart)) || startUTCDateTime.isAfter(currentStart) &&
                    (startUTCDateTime.isEqual(currentEnd) || startUTCDateTime.isBefore(currentEnd))) {
                return true;
            }

            // Check for overlap with end time
            if ((endUTCDateTime.isEqual(currentStart)) || endUTCDateTime.isAfter(currentStart) &&
                    (endUTCDateTime.isEqual(currentEnd) || endUTCDateTime.isBefore(currentEnd))) {
                return true;
            }
        }
        return false;
    }
}
