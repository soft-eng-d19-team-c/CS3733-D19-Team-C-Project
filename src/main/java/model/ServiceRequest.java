package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public ServiceRequest(int ID, String description, String type, Date dateTimeSubmitted, Date dateTimeResolved, String nodeID) {
        this.type = type;
        this.nodeID = nodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = dateTimeResolved != null;
        this.completedBy = null;
        this.requestedBy = null;
        this.ID = ID;
    }

    public String getType() {
        return type;
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

    public ServiceRequest(String type, String location, String description) {
        this(-1, description, type, new Date(), null, location);
    }

    //Determines amount of time task was completed in
    public Date computeTimeDiff(){

        long end = dateTimeResolved.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);

    }

    //Update the information in a ServiceRequest
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update SERVICEREQUESTS set NODEID = ?, DESCRIPTION = ?, TYPE = ?, DATETIMESUBMITTED = ?, DATETIMECOMPLETED = ? where ID = ?";
        java.sql.Date sqlCompleteDate = new java.sql.Date(dateTimeResolved.getTime()); //because ps.setDate takes an sql.date, not a util.date
        java.sql.Date sqlStartDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
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

    //Insert a new ServiceRequest into the database
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into SERVICEREQUESTS (NODEID,  DESCRIPTION, TYPE, DATETIMESUBMITTED) values (?,?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setString(3, type);
            ps.setDate(4, sqlSubmitDate);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    //Returns an observable list of all ServiceRequests for JavaFX's sake
    public static ObservableList<ServiceRequest> getAllServiceRequests() {

        ObservableList<ServiceRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM SERVICEREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                String type = rs.getString("type");
                Date dateTimeSubmitted = rs.getDate("dateTimeSubmitted");
                Date dateTimeResolved = rs.getDate("dateTimeCompleted");
                String nodeID = rs.getString("nodeID");
                ServiceRequest theServiceRequest = new ServiceRequest(ID, description, type, dateTimeSubmitted, dateTimeResolved, nodeID);
                requests.add(theServiceRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }


    public int getID() {
        return ID;
    }

    //Mark a ServiceRequest complete
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
