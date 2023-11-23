package dao;

import helper.ConnectionManager;
import model.Country;
import model.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DivisionDAO {

    /** Returns a list of all countries in the database
     *
     * @return list of all countries
     */
    public static List<Division> getAllDivisions() {
        List<Division> allDivisions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM first_level_divisions";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryId = rs.getInt("Country_ID");
                allDivisions.add(new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out); }
        return allDivisions;
    }

    /** Returns list of divisions a client can live in based on their country
     *
     * @param country
     * @return list of divisions
     */
    public static List<Division> getDivisionsByCountry(Country country) {
        List<Division> divisionsByCountry = new ArrayList<>();

        try {
            Connection conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, country.getCountryId());

            try {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int divisionId = rs.getInt("Division_ID");
                    String division = rs.getString("Division");
                    LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                    String createdBy = rs.getString("Created_By");
                    LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                    String lastUpdatedBy = rs.getString("Last_Updated_By");
                    int countryId = rs.getInt("Country_ID");
                    divisionsByCountry.add(new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId));
                }
            } catch (SQLException e) {
                System.out.println("SQL Error");
                e.printStackTrace(System.out);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return divisionsByCountry;
    }

    public static Division getDivisionByDivisionId(int divisionId) {
        Division divisionByDivisionId;

        try {
            Connection conn = ConnectionManager.getConnection();
            String query = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, divisionId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String division = rs.getString("Division");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryId = rs.getInt("Country_ID");
                divisionByDivisionId = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
                return divisionByDivisionId;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace(System.out);
        }

        return null;
    }


}
