package model;

import java.time.LocalDateTime;

public class Appointment {

    private int Appointment_ID;
    private int Contact_ID;
    private int Customer_ID;
    private int User_ID;

    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private String Created_By;
    private String Last_Updated_By;

    private LocalDateTime Start;
    private LocalDateTime End;
    private LocalDateTime Create_Date;
    private LocalDateTime Last_Update;

    /**
     *
     * @param appointmentId
     * @param contactId
     * @param customerId
     * @param userId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param createdBy
     * @param lastUpdatedBy
     * @param start
     * @param end
     * @param createDate
     * @param lastUpdate
     */
    public Appointment(int appointmentId, int contactId, int customerId, int userId, String title, String description, String location, String type, String createdBy, String lastUpdatedBy, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, LocalDateTime lastUpdate) {
        Appointment_ID = appointmentId;
        Contact_ID = contactId;
        Customer_ID = customerId;
        User_ID = userId;
        Title = title;
        Description = description;
        Location = location;
        Type = type;
        Created_By = createdBy;
        Last_Updated_By = lastUpdatedBy;
        Start = start;
        End = end;
        Create_Date = createDate;
        Last_Update = lastUpdate;
    }

    /**
     *
     * @return
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }


    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /**
     *
     * @return
     */
    public int getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     *
     * @return
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     *
     * @return
     */
    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    /**
     *
     * @return
     */
    public String getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(String created_By) {
        Created_By = created_By;
    }

    /**
     *
     * @return
     */
    public String getLast_Updated_By() {
        return Last_Updated_By;
    }

    public void setLast_Updated_By(String last_Updated_By) {
        Last_Updated_By = last_Updated_By;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getStart() {
        return Start;
    }

    public void setStart(LocalDateTime start) {
        Start = start;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getEnd() {
        return End;
    }

    public void setEnd(LocalDateTime end) {
        End = end;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getCreate_Date() {
        return Create_Date;
    }

    public void setCreate_Date(LocalDateTime create_Date) {
        Create_Date = create_Date;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getLast_Update() {
        return Last_Update;
    }

    public void setLast_Update(LocalDateTime last_Update) {
        Last_Update = last_Update;
    }


}
