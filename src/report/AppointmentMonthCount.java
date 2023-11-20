package report;

/** This class is used to generate a report for total appointments by Month
 *
 */
public class AppointmentMonthCount {

    private String month;
    private int count;

    /** Constructor for AppointmentMonthCount
     *
     * @param month name of month
     * @param count accumulated count
     */
    public AppointmentMonthCount(String month, int count) {
        this.month = month;
        this.count = count;
    }

    /** Returns name of month
     *
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /** Sets name of month
     *
     * @param month name of month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /** Returns accumulated count
     *
     * @return count
     */
    public int getCount() {
        return count;
    }

    /** Sets accumulated count
     *
     * @param count accumulated count
     */
    public void setCount(int count) {
        this.count = count;
    }
}
