package model;

import helper.AppointmentQuery;

import java.sql.SQLException;
import java.time.LocalDateTime;

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
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    /** Constructor for Appointment
     *
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

    /**
     *
     * @return appointment id
     */
    public int getAppointmentId() {
        return appointmentId;
    }


    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     *
     * @return contact id
     */
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     *
     * @return customer id
     */
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return user id
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getContactName() {
        // Get the contact name for the appointment
        String contactName = null;
        try {
            AppointmentQuery appointmentQuery = new AppointmentQuery();
            contactName = appointmentQuery.getContactName(contactId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactName;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

}
