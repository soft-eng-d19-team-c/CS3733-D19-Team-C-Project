package model;

import base.Database;
import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * ITServiceRequest for requesting assistance from IT
 * @author Matt Burd
 */

public class ITRequest {

    private int ID;
    private String description;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeCompleted;
    private boolean isComplete;
    private User userCompletedBy;
    private User userRequestedBy;
    private String nodeID;

    public ITRequest(String description, String nodeID, Timestamp dateTimeSubmitted, Timestamp dateTimeCompleted, int ID, User userRequestedBy) {
        this.ID = ID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = dateTimeCompleted != null;
        this.nodeID = nodeID;
        this.userRequestedBy = userRequestedBy;
    }

    public ITRequest(String description, String nodeID) {
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

    public int getID() {
        return ID;
    }

    public String getUserRequestedBy() {
        return this.userRequestedBy.getUsername();
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
        String sqlCmd = "update ITREQUESTS set USERASSIGNEDTO = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ?, NODEID = ? where ID = ?";
        java.sql.Timestamp sqlCompleteDate = new java.sql.Timestamp(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, userCompletedBy.getUsername());
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

        String sqlCmd = "insert into ITREQUESTS (DESCRIPTION, DATETIMESUBMITTED, USERREQUESTEDBY, NODEID) values (?,?,?,?)";
        java.sql.Timestamp sqlSubmitDate = new java.sql.Timestamp(System.currentTimeMillis()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
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
    public static ObservableList<ITRequest> getAllServiceRequests() {

        ObservableList<ITRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Database.getConnection().createStatement();
            String str = "SELECT * FROM ITREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                Timestamp dateTimeSubmitted = rs.getTimestamp("dateTimeSubmitted");
                Timestamp dateTimeResolved = rs.getTimestamp("dateTimeCompleted");
                String nodeID = rs.getString("nodeID");
                String userResolvedBy = rs.getString("userCompletedBy");
                String userRequestedBy = rs.getString("userRequestedBy");

                ITRequest theITServiceRequest = new ITRequest(description, nodeID, dateTimeSubmitted, dateTimeResolved, ID, new User(userRequestedBy));
                requests.add(theITServiceRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }

    //Mark a ITServiceRequest complete
    public boolean resolve() {
        String str = "UPDATE ITREQUESTS SET DATETIMECOMPLETED = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
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
