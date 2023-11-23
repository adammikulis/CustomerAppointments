package model;

import java.time.LocalDateTime;

public class Country {

    private int countryId;
    private String country;
    private LocalDateTime createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /** Constructor for country class
     *
     * @param countryId
     * @param country
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public Country(int countryId, String country, LocalDateTime createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.country = country;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }


    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(LocalDateTime createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public String toString() {
        return country;
    }
}
