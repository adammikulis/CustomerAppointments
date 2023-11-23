package model;

import dao.CountryDAO;
import dao.CustomerDAO;

import java.time.LocalDateTime;

/** Class for creating a customer*/
public class Customer {

    private int customerId;
    private String customerName;
    private String streetAddress;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;


    /** Constructor for Customer
     *
     * @param customerId      customer id
     * @param customerName    customer name
     * @param streetAddress customer street address
     * @param postalCode    customer postalCode
     * @param phone         customer phone
     * @param createDate    date created
     * @param createdBy     created by
     * @param lastUpdate    last updated date and time
     * @param lastUpdatedBy last updated by
     * @param divisionId    division id of customer
     */
    public Customer(int customerId, String customerName, String streetAddress, String postalCode, String phone, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;

    }


    /** Sets customer id
     *
     * @param customerId customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Returns customer id
     *
     * @return customer id
     */
    public int getCustomerId() {
        return customerId;
    }


    /** Sets customer name
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Returns last name
     *
     * @return lastName
     */
    public String getCustomerName() {
        return customerName;
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

    /** Returns the customer's creation date.
     *
     * @return createDate as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets the customer's creation date.
     *
     * @param createDate LocalDateTime to set the creation date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Returns the customer's creator.
     *
     * @return createdBy as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets the customer's creator.
     *
     * @param createdBy String to set the creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns the customer's last update date.
     *
     * @return lastUpdate as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets the customer's last update date.
     *
     * @param lastUpdate LocalDateTime to set the last update date
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Returns the customer's last updater.
     *
     * @return lastUpdatedBy as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets the customer's last updater.
     *
     * @param lastUpdatedBy String to set the last updater
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns the customer's division ID.
     *
     * @return divisionId as int
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Sets the customer's division ID.
     *
     * @param divisionId int to set the division ID
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Returnss the customer's country by division ID
     * @return country by division ID
     */
    public Country getCountry() {
        return CountryDAO.getCountryByDivisionId(getDivisionId());
    }
}
