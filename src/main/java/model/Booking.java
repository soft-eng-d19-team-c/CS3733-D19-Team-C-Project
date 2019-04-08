package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

// This class is for booking rooms in the scheduling system
public class Booking {
    private String location;
    private String description;
    private Timestamp dateTimeStart;
    private Timestamp dateTimeEnd;
    private User userCompletedBy;
    private int ID;

    public Booking(String location, String description, Timestamp dateTimeStart, Timestamp dateTimeEnd, User userCompletedBy, int ID) {
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.userCompletedBy = userCompletedBy;
        this.ID = ID;
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }
    public String getDateFrom() {
        return this.dateTimeStart.toString();
    }
    public String getDateTo() {
        return this.dateTimeEnd.toString();
    }
    public String getUsername() {
        return this.userCompletedBy.getUsername();
    }

    public static ObservableList<Booking> getCurrentBookings() {
        // TODO eventually this method should only fetch bookings that have not ended but like YOLO.
        ObservableList<Booking> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGS LEFT JOIN USERS AS E ON BOOKINGS.USERCOMPLETEDBY = E.USERNAME";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new Booking(rs.getString("LOCATION"), rs.getString("DESCRIPTION"), rs.getTimestamp("DATETIMESTART"), rs.getTimestamp("DATETIMEEND"), new User(rs.getString("USERNAME")), rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Determines duration of booking
    public Date computeTimeDiff(){

        long end = dateTimeEnd.getTime();
        long start = dateTimeStart.getTime();
        return new Date(end - start);

    }

    //Update a booking request with new information
    public boolean update(){
        boolean executed = false;

        String sqlCmd = "update BOOKINGS set LOCATION = ?, DESCRIPTION = ?, DATETIMESTART = ?, DATETIMEEND = ?, USERCOMPLETEDBY = ? where ID = ?";
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlEndDate = new java.sql.Timestamp(dateTimeEnd.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, location);
            ps.setString(2, description);
            ps.setTimestamp(3, sqlStartDate);
            ps.setTimestamp(4,sqlEndDate);
            ps.setString(5, userCompletedBy.getUsername());
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    //Insert a new booking into the database
    public boolean insert(){
        boolean executed = false;
        String sqlCmd = "insert into BOOKINGS (LOCATION, DESCRIPTION, DATETIMESTART, DATETIMEEND, USERCOMPLETEDBY) values (?,?,?,?,?)";
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlEndDate = new java.sql.Timestamp(dateTimeEnd.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, location);
            ps.setString(2, description);
            ps.setTimestamp(3, sqlStartDate);
            ps.setTimestamp(4,sqlEndDate);
            ps.setString(5, userCompletedBy.getUsername());
            executed = ps.execute(); //returns a boolean

            System.out.println(executed);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }
}
