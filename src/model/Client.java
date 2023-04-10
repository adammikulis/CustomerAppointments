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
}
