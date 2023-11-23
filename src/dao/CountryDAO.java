package dao;

import helper.ConnectionManager;
import model.Contact;
import model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {

    /** Returns a list of all countries in the database
     *
     * @return list of all countries
     */
    public static List<Country> getAllCountries() {
        List<Country> allCountries = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM countries";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                allCountries.add(new Country(countryId, country, createDate, createdBy, lastUpdate, lastUpdatedBy));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out); }
        return allCountries;
    }

    public static Country getCountryByDivisionId(int divisionId) {
        Country countryByDivisionId;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM countries";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

           if (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                countryByDivisionId = new Country(countryId, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
                return countryByDivisionId;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out); }
        return null;
    }


}
