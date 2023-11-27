package model;

import java.time.LocalDateTime;

/** Class for creating a User */
public class User {

    private int userId;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /** Constructor for User
     *
     * @param userId
     * @param userName
     * @param password
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public User(int userId, String userName, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns user ID
     *
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }

    /** Sets user ID
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Returns user name
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /** Sets user name
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Returns password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /** Sets password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Gets create datetime
     *
     * @return create datetime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets create datetime
     *
     * @param createDate
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /** Gets created by
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

    /** Sets last updated datetime
     *
     * @param lastUpdate
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Returns last updated by
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

    /** Overrides toString to return username
     *
     * @return username
     */
    @Override
    public String toString() {
        return userName;
    }
}
