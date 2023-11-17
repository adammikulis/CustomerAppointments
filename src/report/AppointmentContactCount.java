package report;

public class AppointmentContactCount {

    private String contact;
    private int count;

    public AppointmentContactCount(String contact, int count) {
        this.contact = contact;
        this.count = count;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
