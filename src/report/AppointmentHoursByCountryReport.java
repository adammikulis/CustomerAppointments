package report;

public class AppointmentHoursByCountryReport {
    private String country;
    private long totalHours;

    /**
    /**
     * Constructor for AppointmentHoursByCountryReport.
     *
     * @param country  name of the country.
     * @param totalHours  total hours of appointments in this country.
     */
    public AppointmentHoursByCountryReport(String country, long totalHours) {
        this.country = country;
        this.totalHours = totalHours;
    }

    /**
     * Returns the name of the country.
     *
     * @return  name of the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country.
     *
     * @param country  name of the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the total hours of appointments in this country.
     *
     * @return  total hours of appointments.
     */
    public long getTotalHours() {
        return totalHours;
    }

    /**
     * Sets the total hours of appointments in this country.
     *
     * @param totalHours  total hours of appointments to set.
     */
    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }
}
