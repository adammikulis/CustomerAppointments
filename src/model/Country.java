package model;

import java.time.LocalDateTime;

public class Country {

    private int countryId;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /** Constructor for country class
     *
     * @param countryId
     * @param country
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public Country(int countryId, String country, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns country ID
     *
     * @return country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /** Sets country ID
     *
     * @param countryId
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** Returns country name
     *
     * @return country name
     */
    public String getCountry() {
        return country;
    }

    /** Sets country name
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /** Returns created by
     *
     * @return created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Returns create date
     *
     * @return create date
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets create date
     *
     * @param createDate
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Sets created by
     *
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns last updated datetime
     *
     * @return last update
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets last updated datetime
     *
     * @param lastUpdate
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Gets last updated by name
     *
     * @return last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets last updated by name
     *
     * @param lastUpdatedBy
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Overrides toString to return country name
     *
     * @return country name
     */
    @Override
    public String toString() {
        return country;
    }
}
