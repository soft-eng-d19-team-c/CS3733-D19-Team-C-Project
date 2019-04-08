package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

// This class is for booking rooms in the scheduling system
public class Booking {
    private String location;
    private String description;
    private Timestamp dateTimeStart;
    private Timestamp dateTimeEnd;
    private String userCompletedBy; //I have set this to a string so it matches w/ the database
    private int ID;

    public Booking(String location, String description, Timestamp dateTimeStart, Timestamp dateTimeEnd, String userCompletedBy, int ID) {
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
        return this.userCompletedBy;
    }

    public static ObservableList<Booking> getCurrentBookings() {
        // TODO eventually this method should only fetch bookings that have not ended but like YOLO.
        ObservableList<Booking> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGS LEFT JOIN USERS AS E ON BOOKINGS.USERCOMPLETEDBY = E.USERNAME";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new Booking(rs.getString("LOCATION"), rs.getString("DESCRIPTION"), rs.getTimestamp("DATETIMESTART"), rs.getTimestamp("DATETIMEEND"), rs.getString("USERCOMPLETEDBY"), rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param bookableLocationTitle
     * @return LinkedList</Booking> list of bookings for a given location
     * @author Fay Whittall
     */
    private LinkedList<Booking> getBookingsForLocation(String bookableLocationTitle){
        LinkedList<Booking> result = new LinkedList<>();
        String str = "SELECT * FROM BOOKINGS WHERE LOCATION = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            ps.setString(1,bookableLocationTitle);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Booking(rs.getString("LOCATION"), rs.getString("DESCRIPTION"), rs.getTimestamp("DATETIMESTART"), rs.getTimestamp("DATETIMEEND"), rs.getString("USERCOMPLETEDBY"), rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @return boolean true if the booking has a time conflict with another booking, false otherwise
     * @author Fay Whittall
     */
    public boolean hasConflicts(){
        LinkedList<Booking> bookingsFromRoom = getBookingsForLocation(this.location);
        for(Booking b: bookingsFromRoom){
            long bStartTime = b.dateTimeStart.getTime();
            long bEndTime = b.dateTimeEnd.getTime();
            //checks if either the start time or end time falls within the start and end time of the current booking
            if(((this.dateTimeStart.getTime() > bStartTime) && (this.dateTimeStart.getTime() < bEndTime)) || ((this.dateTimeEnd.getTime() > bStartTime) && (this.dateTimeEnd.getTime() < bEndTime))) {
                return true;
            }
        }
        return false;
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
            ps.setString(5, userCompletedBy);
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
            ps.setString(5, userCompletedBy);
            executed = ps.execute(); //returns a boolean

            System.out.println(executed);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }
}
