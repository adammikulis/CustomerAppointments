package model;

public class Client {

    private int clientId;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String country;

    /**
     * Constructor for Client
     *
     * @param clientId      client id
     * @param firstName     client name
     * @param lastName      client name
     * @param streetAddress client street address
     * @param city          client city
     * @param country       client country
     */
    public Client(int clientId, String firstName, String lastName, String streetAddress, String city, String country) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    /**
     * Returns next auto-generated product id
     *
     * @return increments and returns next product id based on last product on list
     */
    public static int getNextClientId() {
        int newClientId = 0;
        // Check if there are any products present and return id greater than current
        try {
            for (Client client : ClientList.getAllClients()) {
                if (client.getClientId() > newClientId)
                    newClientId = client.getClientId() + 1;
            }
            return newClientId;
        }
        // Returns id as 1 if part list is empty
        catch (Exception e) {
            return 1;
        }
    }

    /**
     * /** Set client id
     *
     * @param clientId client id
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Return client id
     *
     * @return client id
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Set client first name
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Return first name
     *
     * @return firstName
     */
    public String getFirstName() { return firstName;}

    /**
     * Set last name
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Return last name
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    public void setStreetAddress (String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress () {
        return streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
