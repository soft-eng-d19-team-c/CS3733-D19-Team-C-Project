package model;

import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

//Create service request, get service request, and have it talk to database

public class InterpreterRequest {

    private String nodeID;
    private String description;
    private Timestamp dateTimeRequest;
    private Timestamp dateTimeCompleted;
    private boolean isComplete;
    private User userCompletedBy;
    private int ID;

    public InterpreterRequest(String nodeID, String description, Timestamp dateTimeRequest, Timestamp dateTimeCompleted, boolean isComplete, User userCompletedBy, int ID) {
        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeRequest = dateTimeRequest;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = isComplete;
        this.userCompletedBy = userCompletedBy;
        this.ID = ID;
    }

    public InterpreterRequest(int ID, String description, Timestamp dateTimeRequest, Timestamp dateTimeCompleted, String nodeID) {
        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeRequest = dateTimeRequest;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = dateTimeCompleted != null;
        this.userCompletedBy = null;
        this.ID = ID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    public Timestamp getDateTimeCompleted() {
        return dateTimeCompleted;
    }

    public Timestamp getDateTimeRequest() {
        return this.dateTimeRequest;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getCompletedBy() {
        return userCompletedBy.getUsername();
    }

    public InterpreterRequest(String location, Timestamp dateTimeRequest, String description) {
        this(-1, description, dateTimeRequest, null, location);
    }

    //Determines amount of time task was completed in
    public Date computeTimeDiff(){
        long end = dateTimeCompleted.getTime();
        long start = dateTimeRequest.getTime();
        return new Date(end - start);

    }

    @SuppressWarnings("Duplicates")
    public boolean insert(){
        String sqlCmd = "insert into INTERPRETERREQUESTS (NODEID, DESCRIPTION, DATETIMESUBMITTED) values (?,?,?)";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.description);
            ps.setTimestamp(3, this.dateTimeRequest);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    

    //Insert a new Interpreter into the database
    public boolean update(){
        // TODO

        boolean executed = false;

        String sqlCmd = "insert into INTERPRETERREQUESTS (NODEID,  DESCRIPTION, DATETIMESUBMITTED, DATETIMECOMPLETED, USERCOMPLETEDBY) values (?,?,?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeRequest.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.description);
            ps.setTimestamp(3, this.dateTimeRequest);
            ps.setTimestamp(4, this.dateTimeCompleted);
            ps.setString(5, this.userCompletedBy.getUsername());
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    //Returns an observable list of all SANITATIONREQUESTS for JavaFX's sake
    public static ObservableList<InterpreterRequest> getAllServiceRequests() {
        ObservableList<InterpreterRequest> requests =  FXCollections.observableArrayList();
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM INTERPRETERREQUESTS";
            ResultSet rs = stmt.executeQuery(str);
            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                String username = rs.getString("USERCOMPLETEDBY");
                Timestamp dateTimeRequest = rs.getTimestamp("DATETIMESUBMITTED");
                Timestamp dateTimeResolved = rs.getTimestamp("DATETIMECOMPLETED");
                String nodeID = rs.getString("NODEID");
                InterpreterRequest req = new InterpreterRequest(nodeID, description, dateTimeRequest, dateTimeResolved, dateTimeResolved != null, new User(username), ID);
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }


    public int getID() {
        return ID;
    }

    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE INTERPRETERREQUESTS SET DATETIMECOMPLETED = ?, USERCOMPLETEDBY = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(1, ts);
            ps.setString(2, Main.user.getUsername());
            ps.setInt(3, this.getID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
