package model;

import base.Main;

import java.sql.*;
import java.util.Date;

//Create service request, get service request, and have it talk to database

public class ServiceRequest {

    private String type;
    private String nodeID;
    private String description;
    private Date dateTimeSubmitted;
    private Date dateTimeResolved;
    private boolean isComplete;
    private User completedBy;
    private User requestedBy;
    private int ID;

    public ServiceRequest(String type, String nodeID, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy, int ID) {
        this.type = type;
        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = isComplete;
        this.completedBy = completedBy;
        this.requestedBy = requestedBy;
        this.ID = ID;
    }

    //Determines amount of time task was completed in

    public Date computeTimeDiff(){

        long end = dateTimeResolved.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);

    }

    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update SERVICEREQUESTS set NODEID = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, ISCOMPLETE = ? where SERVICEREQUESTID = this.ID";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setBoolean(3, isComplete);
            ps.setDate(4, sqlSubmitDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into SERVICEREQUESTS (NODEID, DESCRIPTION, DATETIMESUBMITTED) values (?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setDate(3, sqlSubmitDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

}
