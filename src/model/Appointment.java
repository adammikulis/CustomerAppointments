package model;

import dao.ContactDAO;
import helper.AppointmentTimeChecker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

/** Class for creating Appointments*/
public class Appointment {

    private int appointmentId;
    private int contactId;
    private int customerId;
    private int userId;

    private String contactName;
    private String title;
    private String description;
    private String location;
    private String type;
    private String createdBy;
    private String lastUpdatedBy;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime localStartDateTime;
    private LocalDateTime localEndDateTime;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    /** Constructor for Appointment
     * Generates an Appointment object
     * @param appointmentId appointment id
     * @param contactId contact id
     * @param customerId customer id
     * @param userId user id
     * @param title title
     * @param description description
     * @param location location
     * @param type type
     * @param createdBy created by
     * @param lastUpdatedBy last updated by
     * @param startDateTime start
     * @param endDateTime end
     * @param createDate create date
     * @param lastUpdate last update
     */
    public Appointment(int appointmentId, int contactId, int customerId, int userId, String title, String description, String location, String type, String createdBy, String lastUpdatedBy, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createDate, LocalDateTime lastUpdate) {
        this.appointmentId = appointmentId;
        this.contactId = contactId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    /** Returns the id of the appointment
     *
     * @return appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /** Sets the id of the appointment
     * @param appointmentId id to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** Returns the contact id of the appointment
     *
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /** Sets the contact id of the appointment
     * @param contactId contact id to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Returns customer id for appointment
     *
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /** Sets customer id for appointment
     * @param customerId id of customer to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Returns user id for appointment
     *
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /** Sets user id for appointment
     * @param userId id of user to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Returns title of appointment
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /** Sets title for appointment
     * @param title title to set for appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Returns description of appointment
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /** Sets description of appointment
     * @param description description to set for appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Returns location of appointment
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /** Sets location of appointment
     * @param location location to set for appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Returns type of appointment
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /** Sets type of appointment
     * @param type type to set for appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /** Returns created by for appointment
     *
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets created by for appointment
     * @param createdBy created by to set for appointment
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns last updated by for appointment
     *
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets last updated by for appointment
     * @param lastUpdatedBy last updated by to set for appointment
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns start date and time of appointment
     *
     * @return startDateTime
     */
    public LocalDateTime getLocalStartDateTime() {
        localStartDateTime = AppointmentTimeChecker.convertUTCToLocal(startDateTime);
        return localStartDateTime;
    }

    /** Returns end date and time of appointment
     *
     * @return endDateTime
     */
    public LocalDateTime getLocalEndDateTime() {
        localEndDateTime = AppointmentTimeChecker.convertUTCToLocal(endDateTime);
        return localEndDateTime;
    }

    /** Sets end date and time for appointment
     * @param endDateTime end date and time to set for appointment
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }


    /** Returns start date and time of appointment
     *
     * @return startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /** Sets start date and time for appointment
     * @param startDateTime start date and time to set for appointment
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** Returns end date and time of appointment
     *
     * @return endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }


    /** Returns create date and time of appointment
     *
     * @return createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets create date and time for appointment
     * @param createDate create date and time for appointment
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Returns last updated date and time of appointment
     *
     * @return lastUpdate
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets last updated date and time for appointment
     * @param lastUpdate last updated date and time for appointment
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Returns contact name for appointment
     *
     * @return contactName
     */
    public String getContactName() {
        // Get the contact name for the appointment
        String contactName = null;
        try {
            contactName = ContactDAO.getContactName(contactId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactName;
    }

    /** Sets contact name for appointment
     * @param contactName contact name for appointment
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Converts startDateTime to a LocalDate
     *
     * @return startDate
     */
    public LocalDate getDate() {
        return startDateTime.toLocalDate();
    }

    /** Converts startDateTime to a LocalTime
     *
     * @return startTime
     */
    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    /** Converts endDateTime to a LocalTime
     *
     * @return endTime
     */
    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }
}
