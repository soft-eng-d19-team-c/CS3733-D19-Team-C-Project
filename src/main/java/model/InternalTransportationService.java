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
    private Date dateTimeResolved;
    private boolean isComplete;
    private User completedBy;
    private User requestedBy;
    private int ID;

    public InternalTransportationService (int ID, String nodeID, String nodeIDDest, String description, Date dateTimeSubmitted, Date dateTimeResolved) {
        this.nodeID = nodeID;
        this.nodeIDDest = nodeIDDest;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = dateTimeResolved != null;
        this.completedBy = null;
        this.requestedBy = null;
        this.ID = ID;
    }

    //seperate constructor
    public InternalTransportationService( String location, String destination, String description) {
        this(-1, location, destination, description,  new Date(), null);
    }

    public int getID() { return ID; }


    //Update service request once complete
    //get the user who submitted the request and resolved it?
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update SERVICEREQUESTS set NODEID = ?, NODEIDDEST = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, DATETIMERESOLVED = ? where ID = ?";
        java.sql.Date sqlCompleteDate = new java.sql.Date(dateTimeResolved.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, nodeIDDest);
            ps.setString(3, description);
            ps.setDate(4, sqlStartDate);
            ps.setDate(5, sqlCompleteDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into INTERNALTRANSPORTATION (NODEID, NODEIDDEST, DESCRIPTION, DATETIMESUBMITTED, DATETIMERESOLVED) values (?, ?, ?, ?, ?)";
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime());  //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, nodeIDDest);
            ps.setString(3, description);
            //ps.setString(4, sqlStartDate);
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
