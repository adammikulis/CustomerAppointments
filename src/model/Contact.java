package model;

/** Class for creating a contact*/
public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    /** Constructor for contact
     *
     * @param contactId id for the contact
     * @param contactName name of the contact
     * @param email email of the contact
     */
    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /** Returns contact id
     *
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /** Sets the contact id
     *
     * @param contactId id to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Returns contact name
     *
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /** Sets contact name
     *
     * @param contactName name to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Returns contact email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /** Sets contact email
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Override to return contact name*/
    @Override
    public String toString() {
        return contactName;
    }
}
