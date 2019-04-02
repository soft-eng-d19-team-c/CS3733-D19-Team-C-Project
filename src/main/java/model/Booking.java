package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Booking {
    private String bookingLocation;
    private String description;
    private Date dateTimeStart;
    private Date dateTimeEnd;
    private User completedBy; //Type User or String? ???
    private int ID;

    public Booking(String bookingLocationID, String description, Date dateTimeStart, Date dateTimeEnd, User completedBy, int ID) {

        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.completedBy = completedBy;
        this.ID = ID;
        this.bookingLocation = bookingLocation;
    }

    //Determines duration of booking

    public Date computeTimeDiff(){

        long end = dateTimeEnd.getTime();
        long start = dateTimeStart.getTime();
        return new Date(end - start);

    }

    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update BOOKINGS set LOCATION = ?, DESCRIPTION = ?, DATETIMESTART = ?, DATETIMEEND = ?, USERCOMPLETEDBY = ? where ID = ?";
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlEndDate = new java.sql.Date(dateTimeEnd.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, bookingLocation);
            ps.setString(2, description);
            ps.setDate(3, sqlStartDate);
            ps.setDate(4,sqlEndDate);
            ps.setString(5, completedBy.getUsername());
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into BOOKINGS (LOCATION, DESCRIPTION, DATETIMESTART, DATETIMEEND, USERCOMPLETEDBY) values (?,?,?,?,?)";
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlEndDate = new java.sql.Date(dateTimeEnd.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, bookingLocation);
            ps.setString(2, description);
            ps.setDate(3, sqlStartDate);
            ps.setDate(4,sqlEndDate);
            ps.setString(5, completedBy.getUsername());

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }
}
