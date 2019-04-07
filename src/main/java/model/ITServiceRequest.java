package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

/**
 * ITServiceRequest for requesting assistance from IT
 * @author Matt Burd
 */

public class ITServiceRequest {

    private int ID;
    private String type;
    private String description;
    private Date dateTimeSubmitted;
    private Date dateTimeCompleted;
    private boolean isComplete;
    private User userAssignedTo;
    private User userRequestedBy;
    private String nodeID;

    public ITServiceRequest(String type, String description, String nodeID, Date dateTimeSubmitted, Date dateTimeCompleted, boolean isComplete, User userAssignedTo, User userRequestedBy, int ID) {
        this.ID = ID;
        this.type = type;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = isComplete;
        this.userAssignedTo = userAssignedTo;
        this.userRequestedBy = userRequestedBy;
        this.nodeID = nodeID;
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

    public String getNodeID() {
        return nodeID;
    }

    //Determines amount of time task was completed in
    public Date computeTimeDiff(){

        long end = dateTimeCompleted.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);

    }

    //Update the information in a ITServiceRequest
    public boolean update(){

        boolean executed = false;
        //are these fields correct? How do we deal with assignment without completion?
        String sqlCmd = "update ITSERVICEREQUESTS set USERASSIGNEDTO = ?, DESCRIPTION = ?, TYPE = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ?, NODEID = ? where ID = ?";
        java.sql.Date sqlCompleteDate = new java.sql.Date(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, userAssignedTo.getUsername());
            ps.setString(2, description);
            ps.setString(3, type);
            ps.setDate(4, sqlStartDate);
            ps.setDate(5, sqlCompleteDate);
            ps.setString(6, nodeID);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    //Insert a new ITServiceRequest into the database
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into SERVICEREQUESTS (DESCRIPTION, TYPE, DATETIMESUBMITTED, USERSUBMITTEDBY, NODEID, ID) values (?,?,?,?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, description);
            ps.setString(2, type);
            ps.setDate(3, sqlSubmitDate);
            ps.setString(4, userRequestedBy.getUsername());
            ps.setString(5, nodeID);
            ps.setInt(6, ID);

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    //Returns an observable list of all ITServiceRequests for JavaFX's sake
    public static ObservableList<ITServiceRequest> getAllITServiceRequests() {

        ObservableList<ITServiceRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM ITSERVICEREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                String type = rs.getString("type");
                Date dateTimeSubmitted = rs.getDate("dateTimeSubmitted");
                Date dateTimeResolved = rs.getDate("dateTimeCompleted");
                String nodeID = rs.getString("nodeID");
                String userAssignedTo = rs.getString("userAssignedTo");
                String userRequestedBy = rs.getString("userRequestedBy");
                Boolean isComplete = rs.getBoolean("isComplete");


                ITServiceRequest theITServiceRequest = new ITServiceRequest(type, description, nodeID, dateTimeSubmitted, dateTimeResolved, isComplete, userRequestedBy, userAssignedTo, ID);
                requests.add(theITServiceRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }

    public int getID() {
        return ID;
    }

    //Mark a ITServiceRequest complete
    public boolean resolveITServiceRequest() {
        String str = "UPDATE ITSERVICEREQUESTS SET DATETIMECOMPLETED = ? WHERE ID = ?";
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
