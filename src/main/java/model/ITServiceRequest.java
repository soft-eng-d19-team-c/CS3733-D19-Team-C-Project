package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * ITServiceRequest for requesting assistance from IT
 * @author Matt Burd
 */

public class ITServiceRequest {

    private int ID;
    private String description;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeCompleted;
    private boolean isComplete;
    private User userResolvedBy;
    private User userRequestedBy;
    private String nodeID;

    public ITServiceRequest(String description, String nodeID, Timestamp dateTimeSubmitted, Timestamp dateTimeCompleted, boolean isComplete, int ID) {
        this.ID = ID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = isComplete;
        this.nodeID = nodeID;
    }

    public ITServiceRequest(String description, String nodeID) {
        this.description = description;
        this.nodeID = nodeID;
        this.userRequestedBy = Main.user;
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
    public Timestamp computeTimeDiff(){

        long end = dateTimeCompleted.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Timestamp(end - start);

    }

    //Update the information in a ITServiceRequest
    public boolean update(){

        boolean executed = false;
        //are these fields correct? How do we deal with assignment without completion?
        String sqlCmd = "update ITSERVICEREQUESTS set USERASSIGNEDTO = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ?, NODEID = ? where ID = ?";
        java.sql.Timestamp sqlCompleteDate = new java.sql.Timestamp(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, userResolvedBy.getUsername());
            ps.setString(2, description);
            ps.setTimestamp(3, sqlStartDate);
            ps.setTimestamp(4, sqlCompleteDate);
            ps.setString(5, nodeID);
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

        String sqlCmd = "insert into ITSERVICEREQUESTS (DESCRIPTION, DATETIMESUBMITTED, USERREQUESTEDBY, NODEID) values (?,?,?,?)";
        java.sql.Timestamp sqlSubmitDate = new java.sql.Timestamp(System.currentTimeMillis()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, description);
            ps.setTimestamp(2, sqlSubmitDate);
            ps.setString(3, userRequestedBy.getUsername());
            ps.setString(4, nodeID);

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
                Timestamp dateTimeSubmitted = rs.getTimestamp("dateTimeSubmitted");
                Timestamp dateTimeResolved = rs.getTimestamp("dateTimeCompleted");
                String nodeID = rs.getString("nodeID");
                String userResolvedBy = rs.getString("userResolvedBy");
                String userRequestedBy = rs.getString("userRequestedBy");
                Boolean isComplete = rs.getBoolean("isComplete");


                ITServiceRequest theITServiceRequest = new ITServiceRequest(description, nodeID, dateTimeSubmitted, dateTimeResolved, isComplete, ID);
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
