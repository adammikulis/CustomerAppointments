package model;

import helper.ClientQuery;
import java.sql.SQLException;
import java.time.LocalDateTime;

/** Class for creating a client*/
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


    /** Constructor for Client
     *
     * @param clientId      client id
     * @param clientName    client name
     * @param streetAddress client street address
     * @param postalCode    client postalCode
     * @param phone         client phone
     * @param createDate    date created
     * @param createdBy     created by
     * @param lastUpdate    last updated date and time
     * @param lastUpdatedBy last updated by
     * @param divisionId    division id of client
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


    /** Sets client id
     *
     * @param clientId client id
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /** Returns client id
     *
     * @return client id
     */
    public int getClientId() {
        return clientId;
    }


    /** Sets client name
     *
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /** Returns last name
     *
     * @return lastName
     */
    public String getClientName() {
        return clientName;
    }

    /** Sets street address
     *
     * @param streetAddress name of stree address
     */
    public void setStreetAddress (String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /** Returns street address
     *
     * @return street address
     */
    public String getStreetAddress () {
        return streetAddress;
    }

    /** Sets postal code
     *
     * @param postalCode postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Returns postal code
     *
     * @return postal code
     */
    public String getPostalCode() {
        return postalCode;
    }


    /** Sets phone number
     *
     * @param phone phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** Returns phone number
     *
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /** Returns the client's creation date.
     *
     * @return createDate as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets the client's creation date.
     *
     * @param createDate LocalDateTime to set the creation date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Returns the client's creator.
     *
     * @return createdBy as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets the client's creator.
     *
     * @param createdBy String to set the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns the client's last update date.
     *
     * @return lastUpdate as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets the client's last update date.
     *
     * @param lastUpdate LocalDateTime to set the last update date
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Returns the client's last updater.
     *
     * @return lastUpdatedBy as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets the client's last updater.
     *
     * @param lastUpdatedBy String to set the last updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns the client's division ID.
     *
     * @return divisionId as int
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Sets the client's division ID.
     *
     * @param divisionId int to set the division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Returnss the client's country by division ID
     * @return country by division ID
     */
    public String getCountry() {
        return ClientQuery.getCountryByDivisionId(getDivisionId());
    }

    /** Returns the client's division name by division ID
     *
     * @return division name
     */
    public String getDivision() {
        return ClientQuery.getDivisionByDivisionId(getDivisionId());
    }


}
