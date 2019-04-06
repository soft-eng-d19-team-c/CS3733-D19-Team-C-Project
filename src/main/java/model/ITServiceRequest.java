package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ITServiceRequest {

    private int ID;
    private String type;
    private String description;
    private Date dateTimeSubmitted;
    private Date dateTimeCompleted;
    private boolean isComplete;
    private User userAssignedTo;
    private User userRequestedBy;

    public ITServiceRequest(int ID, String type, String description, Date dateTimeSubmitted, Date dateTimeCompleted, boolean isComplete, User userAssignedTo, User userRequestedBy) {
        this.ID = ID;
        this.type = type;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = isComplete;
        this.userAssignedTo = userAssignedTo;
        this.userRequestedBy = userRequestedBy;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    //Determines amount of time task was completed in
    public Date computeTimeDiff(){

        long end = dateTimeCompleted.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);

    }

    //Update the information in a SanitationRequest
    public boolean update(){

        boolean executed = false;
        //are these fields correct? How do we deal with assignment without completion?
        String sqlCmd = "update ITSERVICEREQUESTS set USERASSIGNEDTO = ?, DESCRIPTION = ?, TYPE = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ? where ID = ?";
        java.sql.Date sqlCompleteDate = new java.sql.Date(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, userAssignedTo.getUsername());
            ps.setString(2, description);
            ps.setString(3, type);
            ps.setDate(4, sqlStartDate);
            ps.setDate(5, sqlCompleteDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    //Insert a new SanitationRequest into the database
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into SERVICEREQUESTS (DESCRIPTION, TYPE, DATETIMESUBMITTED, USERSUBMITTEDBY) values (?,?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, description);
            ps.setString(2, type);
            ps.setDate(3, sqlSubmitDate);
            ps.setString(4, userRequestedBy.getUsername());

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

}
