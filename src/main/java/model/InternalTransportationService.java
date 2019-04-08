package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class InternalTransportationService {
    private String nodeID; //location of request
    private String nodeIDDest; //location of transportation destination
    private String description; //what is the reason for transport
    private Date dateTimeSubmitted;
    private Date pickUpTime;
    private User completedBy;
    private User requestedBy;
    private int ID;

    public InternalTransportationService (int ID, String nodeID, String nodeIDDest, String description, Date dateTimeSubmitted, Date pickUpTime) {
        this.nodeID = nodeID;
        this.nodeIDDest = nodeIDDest;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.pickUpTime = pickUpTime;
        this.completedBy = null;
        this.requestedBy = null;
        this.ID = ID;
    }

    //seperate constructor
    public InternalTransportationService( String location, String destination, String description, Date pickUpTime) {
        this(-1, location, destination, description, new Timestamp(System.currentTimeMillis()), pickUpTime);
    }

    public int getID() { return ID; }


    //Update service request once complete
    //get the user who submitted the request and resolved it?
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update INTERNALTRANSPORTATION set NODEID = ?, NODEIDDEST = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, PICKUPTIME = ? where ID = ?";
       // java.sql.Date sqlStartDate = new java.sql.Date(this.dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlpickup = new java.sql.Date(this.pickUpTime.getTime());
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.nodeIDDest);
            ps.setString(3, this.description);
            ps.setTimestamp(1, ts);
            //ps.setDate(3, sqlStartDate);
            ps.setDate(4, sqlpickup);

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into INTERNALTRANSPORTATION (NODEID, NODEIDDEST, DESCRIPTION, DATETIMESUBMITTED, PICKUPTIME) values (?, ?, ?, ?, ?)";
        java.sql.Date sqlPickup = new java.sql.Date(this.pickUpTime.getTime());
        Timestamp ts = new Timestamp(System.currentTimeMillis());


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.nodeIDDest);
            ps.setString(3, this.description);
            ps.setTimestamp(1, ts);
            ps.setDate(4, sqlPickup);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    public boolean resolve() {
        String str = "UPDATE INTERNALTRANSPORTATION SET DATETIMECOMPLETED = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(1, ts);
            ps.setInt(2, this.getID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
