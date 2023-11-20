package helper;

import javafx.scene.control.Alert;
import model.Appointment;
import model.AppointmentList;
import helper.AppointmentQuery;
import model.Client;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;

/** Class to check if appointment is within a valid timeframe
 *
 */
public class AppointmentTimeChecker {

    /** Checks if appointment is within business hours
     *
     * @param localStartDateTime
     * @param localEndDateTime
     * @return true if appointment is within business hours
     */
    public static boolean businessHourChecker(LocalDateTime localStartDateTime, LocalDateTime localEndDateTime) {
        LocalDateTime easternStartTime = AppointmentList.convertUTCToEastern(AppointmentList.convertLocalToUTC(localStartDateTime));
        LocalDateTime easternEndTime = AppointmentList.convertUTCToEastern(AppointmentList.convertLocalToUTC(localStartDateTime));
        DayOfWeek startDayOfWeek = easternStartTime.getDayOfWeek();
        DayOfWeek endDayOfWeek = easternEndTime.getDayOfWeek();

        // True if appointment start and end are 8am-10pm (ET) Mon-Fri
        return ((easternStartTime.getHour() >= 8 && easternStartTime.getHour() <= 22) && (startDayOfWeek != DayOfWeek.SATURDAY && startDayOfWeek != DayOfWeek.SUNDAY) &&
                (easternEndTime.getHour() >= 8 && easternEndTime.getHour() <= 22) && (endDayOfWeek != DayOfWeek.SATURDAY && endDayOfWeek != DayOfWeek.SUNDAY));
    }

    /** Checks for overlaps with other appointments
     *
     * @param currentAppointmentId
     * @param clientId
     * @param newAppointmentStart
     * @param newAppointmentEnd
     * @param update
     * @return true if there is no overlap
     */
    public static boolean overlapChecker(int currentAppointmentId, int clientId, LocalDateTime newAppointmentStart, LocalDateTime newAppointmentEnd, boolean update) {

        AppointmentQuery appointmentQuery = new AppointmentQuery();
        List<Appointment> appointmentList = appointmentQuery.getAppointmentsByClient(clientId);

        for (Appointment appointment : appointmentList) {
            if (update && appointment.getAppointmentId() == currentAppointmentId) {
                continue;
            }

            LocalDateTime currentAppointmentStart = appointment.getStartDateTime();
            LocalDateTime currentAppointmentEnd = appointment.getEndDateTime();

            // Check if the new appointment overlaps current appointment
            if (((newAppointmentStart.isEqual(currentAppointmentStart) || newAppointmentStart.isAfter(currentAppointmentStart)) && newAppointmentStart.isBefore(currentAppointmentEnd)) ||
                    ((newAppointmentEnd.isAfter(currentAppointmentStart)) && (newAppointmentEnd.isBefore(currentAppointmentEnd) || newAppointmentEnd.isEqual(currentAppointmentEnd))) ||
                    (newAppointmentStart.isBefore(currentAppointmentStart) && newAppointmentEnd.isAfter(currentAppointmentEnd))) {
                return false;
            }
        }
        return true;
    }


    public static boolean appointmentChecker(int currentAppointmentid, int clientId, LocalDateTime startUTCDateTime, LocalDateTime endUTCDateTime, boolean update) {
        if (businessHourChecker(startUTCDateTime, endUTCDateTime)) {
            if (overlapChecker(currentAppointmentid, clientId, startUTCDateTime, endUTCDateTime, update)) {
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
