package model;

import base.Database;
import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

// This class is for booking rooms in the scheduling system
public class Booking {
    private String location;
    private String description;
    private Timestamp dateTimeStart;
    private Timestamp dateTimeEnd;
    private String userCompletedBy; //I have set this to a string so it matches w/ the database
    private int ID;
    private BookableLocation bookedLocation;

    public Booking(String location, String description, Timestamp dateTimeStart, Timestamp dateTimeEnd, String userCompletedBy, int ID) {
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.userCompletedBy = userCompletedBy;
        this.ID = ID;
        this.location = location;
    }

    public Booking(String location, String description, Timestamp dateTimeStart, Timestamp dateTimeEnd, String userCompletedBy, int ID, BookableLocation bookedLocation) {
        this.location = location;
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.userCompletedBy = userCompletedBy;
        this.ID = ID;
        this.bookedLocation = bookedLocation;
    }

    public String getLocation() {
        return this.location;
    }
    public String getDateFrom() {
        return this.dateTimeStart.toString();
    }
    public LocalDateTime getDateFromLocal() {
        return this.dateTimeStart.toLocalDateTime();
    }
    public String getDateTo() {
        return this.dateTimeEnd.toString();
    }
    public LocalDateTime getDateToLocal() {
        return this.dateTimeEnd.toLocalDateTime();
    }
    public String getUsername() {
        return this.userCompletedBy;
    }
    public String getDescription(){return this.description;}

    public BookableLocation getBookedLocation() {
        return bookedLocation;
    }

    public static ObservableList<Booking> getCurrentBookings() {
        // TODO eventually this method should only fetch bookings that have not ended but like YOLO.
        ObservableList<Booking> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGS LEFT JOIN USERS AS E ON BOOKINGS.USERCOMPLETEDBY = E.USERNAME";
        try {
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new Booking(rs.getString("LOCATION"), rs.getString("DESCRIPTION"), rs.getTimestamp("DATETIMESTART"), rs.getTimestamp("DATETIMEEND"), rs.getString("USERCOMPLETEDBY"), rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static LinkedList<Booking> getBookingsDuring(Timestamp time) {
        LinkedList<Booking> result = new LinkedList<>();
        String sqlStr = "select BOOKINGS.ID, LOCATION, DESCRIPTION, DATETIMESTART, DATETIMEEND, USERCOMPLETEDBY, B.ID AS B_ID, TYPE, TITLE, XCOORD, YCOORD from BOOKINGS LEFT JOIN BOOKINGLOCATIONS AS B ON BOOKINGS.LOCATION = B.ID where DATETIMESTART < ? and DATETIMEEND > ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ps.setTimestamp(1,time);
            ps.setTimestamp(2,time);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String location = rs.getString("LOCATION");
                String description = rs.getString("DESCRIPTION");
                Timestamp dateTimeStart = rs.getTimestamp("DATETIMESTART");
                Timestamp dateTimeEnd = rs.getTimestamp("DATETIMEEND");
                String user = rs.getString("USERCOMPLETEDBY");
                int ID = rs.getInt("ID");
                String BID = rs.getString("B_ID");
                String type = rs.getString("type");
                String title = rs.getString("title");
                int x = rs.getInt("xcoord");
                int y = rs.getInt("ycoord");
                BookableLocation bookableLocation = new BookableLocation(BID, type, title, x, y);
                result.add(new Booking(location, description, dateTimeStart, dateTimeEnd, user, ID, bookableLocation));
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
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
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
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
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
        String sqlCmd = "insert into BOOKINGS (LOCATION, DESCRIPTION, DATETIMESTART, DATETIMEEND, USERCOMPLETEDBY) values (?,?,?,?,?)";
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlEndDate = new java.sql.Timestamp(dateTimeEnd.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, location);
            ps.setString(2, description);
            ps.setTimestamp(3, sqlStartDate);
            ps.setTimestamp(4,sqlEndDate);
            ps.setString(5, userCompletedBy);
            ps.execute(); //returns a boolean
            return true;
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static ObservableList<Booking> getAllBooking() {
        ObservableList<Booking> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGS";
        try {
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new Booking(rs.getString("LOCATION"), rs.getString("DESCRIPTION"), rs.getTimestamp("DATETIMESTART"), rs.getTimestamp("DATETIMEEND"), rs.getString("USERCOMPLETEDBY"), rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
