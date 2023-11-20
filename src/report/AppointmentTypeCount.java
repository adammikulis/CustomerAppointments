package report;

/** This class is used to generate a report for total appointments by Type
 *
 */
public class AppointmentTypeCount {
    private String type;
    private int count;

    /** Constructor for AppointmentTypeCount
     *
     * @param type name of appointment
     * @param count
     */
    public AppointmentTypeCount(String type, int count) {
        this.type = type;
        this.count = count;
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
}
