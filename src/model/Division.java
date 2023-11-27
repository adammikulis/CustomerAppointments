package model;

import java.time.LocalDateTime;

/** class for creating a Division
 *
 */
public class Division {

    private int divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /** Constructor for Division
     *
     * @param divisionId
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param countryId
     */
    public Division(int divisionId, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /** Returns division ID
     *
     * @return division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Sets division ID
     *
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Returns division name
     *
     * @return division name
     */
    public String getDivision() {
        return division;
    }

    /** Sets division name
     *
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
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

    /** Returns created by
     *
     * @return created by
     */
    public String getCreatedBy() {
        return createdBy;
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
     * @return last updated
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets last update
     *
     * @param lastUpdate
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Gets last updated by
     *
     * @return last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets last updated by
     *
     * @param lastUpdatedBy
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
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

    /** Overrides toString to return division name
     *
     * @return division name
     */
    @Override
    public String toString() {
        return division;
    }
}
