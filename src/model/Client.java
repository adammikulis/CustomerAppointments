package model;

import java.time.LocalDateTime;

public class Client {

    private int clientId;
    private String clientName;
    private String streetAddress;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;


    /**
     * Constructor for Client
     *
     * @param clientId      client id
     * @param clientName    client name
     * @param streetAddress client street address
     * @param postalCode    client postalCode

     * @param phone         client phone
     */
    public Client(int clientId, String clientName, String streetAddress, String postalCode, String phone, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;

    }


    /**
     * /** Set client id
     *
     * @param clientId client id
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Return client id
     *
     * @return client id
     */
    public int getClientId() {
        return clientId;
    }


    /**
     * Set client name
     *
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Return last name
     *
     * @return lastName
     */
    public String getClientName() {
        return clientName;
    }

    public void setStreetAddress (String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress () {
        return streetAddress;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * Get the client's creation date.
     *
     * @return createDate as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Set the client's creation date.
     *
     * @param createDate LocalDateTime to set the creation date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Get the client's creator.
     *
     * @return createdBy as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set the client's creator.
     *
     * @param createdBy String to set the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the client's last update date.
     *
     * @return lastUpdate as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Set the client's last update date.
     *
     * @param lastUpdate LocalDateTime to set the last update date
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Get the client's last updater.
     *
     * @return lastUpdatedBy as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Set the client's last updater.
     *
     * @param lastUpdatedBy String to set the last updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Get the client's division ID.
     *
     * @return divisionId as int
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Set the client's division ID.
     *
     * @param divisionId int to set the division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

}
