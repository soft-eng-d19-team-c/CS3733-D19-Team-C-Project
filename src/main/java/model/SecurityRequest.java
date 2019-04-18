package model;

import base.Database;
import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

//Create service request, get service request, and have it talk to database

public class SecurityRequest {
    private boolean isUrgent;
    private String nodeID;
    private String description;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeCompleted;
    private String userCompletedBy;
    private String userRequestedBy;
    private boolean isComplete;
    private int ID;

    public SecurityRequest(int ID, String description, boolean isUrgent, Timestamp dateTimeSubmitted, Timestamp dateTimeCompleted, String nodeID) {
        this.isUrgent = isUrgent;
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

    public SecurityRequest(boolean isUrgent, String location, String description) {
        this(-1, description, isUrgent, new Timestamp(System.currentTimeMillis()), null, location);
    }

    //Determines amount of time task was completed in
    public Date computeTimeDiff(){
        long end = dateTimeCompleted.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);
    }

    //Update the information in a SecurityRequest
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update SECURITYREQUESTS set ISURGENT = ?, NODEID = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ? where ID = ?";
        java.sql.Timestamp sqlCompleteDate = new java.sql.Timestamp(dateTimeCompleted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
            ps.setBoolean(1, isUrgent);
            ps.setString(2, nodeID);
            ps.setString(3, description);
            ps.setTimestamp(4, sqlStartDate);
            ps.setTimestamp(5, sqlCompleteDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    //Insert a new SecurityRequest into the database
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into SECURITYREQUESTS (ISURGENT,  NODEID, DESCRIPTION, DATETIMESUBMITTED, USERREQUESTEDBY) values (?,?,?,?,?)";
        Timestamp ts = new Timestamp(System.currentTimeMillis()); //because ps.setDate takes an sql.date, not a util.date
        String username = Main.user.getUsername();

        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
            ps.setBoolean(1, isUrgent);
            ps.setString(2, nodeID);
            ps.setString(3, description);
            ps.setTimestamp(4, ts);
            ps.setString(5, username);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    // TODO we probably want a getActiveServiceRequests()

    //Returns an observable list of all SECURITYREQUESTS for JavaFX's sake
    public static ObservableList<SecurityRequest> getAllServiceRequests() {

        ObservableList<SecurityRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Database.getConnection().createStatement();
            String str = "SELECT * FROM SECURITYREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                boolean isUrgent = rs.getBoolean("ISURGENT");
                String nodeID = rs.getString("NODEID");
                String description = rs.getString("DESCRIPTION");
                Timestamp dateTimeSubmitted = rs.getTimestamp("DATETIMESUBMITTED");
                Timestamp dateTimeResolved = rs.getTimestamp("DATETIMECOMPLETED");
                SecurityRequest theSecurityRequest = new SecurityRequest(ID, description, isUrgent, dateTimeSubmitted, dateTimeResolved, nodeID);
                requests.add(theSecurityRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }


    public int getID() {
        return ID;
    }

    public boolean getIsUrgent() {
        return isUrgent;
    }

    public Timestamp getDateTimeSubmitted() {
        return dateTimeSubmitted;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    //Mark a SanitationRequest complete
    public boolean resolve() {
        String str = "UPDATE SECURITYREQUESTS SET DATETIMECOMPLETED = ?, USERCOMPLETEDBY = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String username = Main.user.getUsername();
            ps.setTimestamp(1, ts);
            ps.setString(2, username);
            ps.setInt(3, this.getID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
