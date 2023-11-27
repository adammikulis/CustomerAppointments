package report;

/** Class for generating a report for total appointments by Type
 *
 */
public class AppointmentTypeMonthCountReport {
    private String type;
    private int janCount, febCount, marCount, aprCount, mayCount, junCount, julCount, augCount, septCount, octCount, novCount, decCount;

    public AppointmentTypeMonthCountReport(String type, int janCount, int febCount, int marCount, int aprCount, int mayCount, int junCount, int julCount, int augCount, int septCount, int octCount, int novCount, int decCount) {
        this.type = type;
        this.janCount = janCount;
        this.febCount = febCount;
        this.marCount = marCount;
        this.aprCount = aprCount;
        this.mayCount = mayCount;
        this.junCount = junCount;
        this.julCount = julCount;
        this.augCount = augCount;
        this.septCount = septCount;
        this.octCount = octCount;
        this.novCount = novCount;
        this.decCount = decCount;
    }

    /** Returns type of appointment
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /** Sets type of appointment
     *
     * @param type type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the count for January.
     *
     * @return The count of appointments in January.
     */
    public int getJanCount() {
        return janCount;
    }

    /**
     * Returns the count for February.
     *
     * @return The count of appointments in February.
     */
    public int getFebCount() {
        return febCount;
    }

    /**
     * Returns the count for March.
     *
     * @return The count of appointments in March.
     */
    public int getMarCount() {
        return marCount;
    }

    /**
     * Returns the count for April.
     *
     * @return The count of appointments in April.
     */
    public int getAprCount() {
        return aprCount;
    }

    /**
     * Returns the count for May.
     *
     * @return The count of appointments in May.
     */
    public int getMayCount() {
        return mayCount;
    }

    /**
     * Returns the count for June.
     *
     * @return The count of appointments in June.
     */
    public int getJunCount() {
        return junCount;
    }

    /**
     * Returns the count for July.
     *
     * @return The count of appointments in July.
     */
    public int getJulCount() {
        return julCount;
    }

    /**
     * Returns the count for August.
     *
     * @return The count of appointments in August.
     */
    public int getAugCount() {
        return augCount;
    }

    /**
     * Returns the count for September.
     *
     * @return The count of appointments in September.
     */
    public int getSeptCount() {
        return septCount;
    }

    /**
     * Returns the count for October.
     *
     * @return The count of appointments in October.
     */
    public int getOctCount() {
        return octCount;
    }

    /**
     * Returns the count for November.
     *
     * @return The count of appointments in November.
     */
    public int getNovCount() {
        return novCount;
    }

    /**
     * Returns the count for December.
     *
     * @return The count of appointments in December.
     */
    public int getDecCount() {
        return decCount;
    }

}

