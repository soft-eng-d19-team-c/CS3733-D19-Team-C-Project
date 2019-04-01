package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Booking {
    private String nodeID;
    private String description;
    private Date dateTimeStart;
    private Date dateTimeEnd;
    private User completedBy; //Type User or String? ???
    private int ID;

    public Booking(String nodeID, String description, Date dateTimeStart, Date dateTimeEnd, User completedBy, int ID) {

        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.completedBy = completedBy;
        this.ID = ID;
    }

    //Determines duration of booking

    public Date computeTimeDiff(){

        long end = dateTimeEnd.getTime();
        long start = dateTimeStart.getTime();
        return new Date(end - start);

    }

    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update BOOKINGS set NODEID = ?, LOCATION = ?, DESCRIPTION = ?, DATETIMESTART = ?, DATETIMEEND = ?, USERCOMPLETEDBY = ? where ID = this.ID";
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setDate(3, sqlStartDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into BOOKINGS (NODEID, DESCRIPTION, DATETIMESTART, DATETIMEEND, USERCOMPLETEDBY) values (?,?,?,?,?)";
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeStart.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setDate(3, sqlStartDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }
}
