package helper;

import dao.AppointmentDAO;
import javafx.scene.control.Alert;
import model.Appointment;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.List;

/** Class for querying if appointment is within a valid timeframe
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
        LocalDateTime easternStartTime = convertUTCToEastern(convertLocalToUTC(localStartDateTime));
        LocalDateTime easternEndTime = convertUTCToEastern(convertLocalToUTC(localStartDateTime));
        DayOfWeek startDayOfWeek = easternStartTime.getDayOfWeek();
        DayOfWeek endDayOfWeek = easternEndTime.getDayOfWeek();

        // True if appointment start and end are 8am-10pm (ET) Mon-Fri
        return ((easternStartTime.getHour() >= 8 && easternStartTime.getHour() <= 22) && (startDayOfWeek != DayOfWeek.SATURDAY && startDayOfWeek != DayOfWeek.SUNDAY) &&
                (easternEndTime.getHour() >= 8 && easternEndTime.getHour() <= 22) && (endDayOfWeek != DayOfWeek.SATURDAY && endDayOfWeek != DayOfWeek.SUNDAY));
    }

    /** Checks for overlaps with other appointments
     *
     * @param currentAppointmentId
     * @param newAppointmentStart
     * @param newAppointmentEnd
     * @param update
     * @return true if there is no overlap
     */
    public static boolean overlapChecker(int currentAppointmentId, LocalDateTime newAppointmentStart, LocalDateTime newAppointmentEnd, boolean update) {

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointmentList = appointmentDAO.getAllAppointments();

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

    /** Calls business hours and overlap checker from one method
     *
     * @param currentAppointmentid
     * @param startUTCDateTime
     * @param endUTCDateTime
     * @param update
     * @return
     */
    public static boolean appointmentChecker(int currentAppointmentid, LocalDateTime startUTCDateTime, LocalDateTime endUTCDateTime, boolean update) {
        if (businessHourChecker(startUTCDateTime, endUTCDateTime)) {
            if (overlapChecker(currentAppointmentid, startUTCDateTime, endUTCDateTime, update)) {
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

    /** Returns possible appointment in the next 15 minutes
     *
     * @return upcomingAppointment
     */
    public static Appointment checkUpcomingAppointments() {

        LocalDateTime now = LocalDateTime.now();
        Appointment upcomingAppointment = null;

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        for (Appointment appointment : appointmentDAO.getAllAppointments()) {
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
