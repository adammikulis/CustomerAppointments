package report;

/** Class for generating a report for total appointments by Contact
 *
 */
public class AppointmentContactCount {

    private String contact;
    private int count;

    /** Constructor for AppointmentContactCount
     *
     * @param contact name of contact
     * @param count accumulated count
     */
    public AppointmentContactCount(String contact, int count) {
        this.contact = contact;
        this.count = count;
    }

    /** Returns contact name
     *
     * @return contact
     */
    public String getContact() {
        return contact;
    }

    /** Sets contact name
     *
     * @param contact name of contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
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
     * @param count amount to set accumulator to
     */
    public void setCount(int count) {
        this.count = count;
    }
}
