package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

//Create service request, get service request, and have it talk to database

public class SanitationRequest {
    private String nodeID;
    private String description;
    private Date dateTimeSubmitted;
    private Date dateTimeCompleted;
    private boolean isComplete;
    private User userCompletedBy;
    private User userRequestedBy;
    private int ID;

    public SanitationRequest(int ID, String description, Date dateTimeSubmitted, Date dateTimeCompleted, String nodeID) {
        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeCompleted = dateTimeCompleted;
        this.isComplete = dateTimeCompleted != null;
        this.userCompletedBy = null;
        this.userRequestedBy = null;
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

    public SanitationRequest(String location, String description) {
        this(-1, description, new Date(), null, location);
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

        String sqlCmd = "update SERVICEREQUESTS set NODEID = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ? where ID = ?";
        java.sql.Date sqlCompleteDate = new java.sql.Date(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
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

        String sqlCmd = "insert into SERVICEREQUESTS (NODEID,  DESCRIPTION, DATETIMESUBMITTED) values (?,?,?)";
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

    // TODO we probably want a getActiveServiceRequests()

    //Returns an observable list of all ServiceRequests for JavaFX's sake
    public static ObservableList<SanitationRequest> getAllServiceRequests() {

        ObservableList<SanitationRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM SERVICEREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                Date dateTimeSubmitted = rs.getDate("dateTimeSubmitted");
                Date dateTimeResolved = rs.getDate("dateTimeCompleted");
                String nodeID = rs.getString("nodeID");
                SanitationRequest theSanitationRequest = new SanitationRequest(ID, description, dateTimeSubmitted, dateTimeResolved, nodeID);
                requests.add(theSanitationRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }


    public int getID() {
        return ID;
    }

    //Mark a SanitationRequest complete
    public boolean resolve() {
        String str = "UPDATE SERVICEREQUESTS SET DATETIMECOMPLETED = ? WHERE ID = ?";
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
