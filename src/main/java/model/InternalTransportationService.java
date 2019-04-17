package model;

import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class InternalTransportationService {
    private String nodeID; //location of request
    private String nodeIDDest; //location of transportation destination
    private String description; //what is the reason for transport
    private Timestamp dateTimeResolved;
    private Timestamp dateTimeSubmitted;
    private Timestamp pickUpTime;
    private User completedBy;
    private User requestedBy;
    private boolean isComplete;
    private int ID;

    public InternalTransportationService (int ID, String nodeID, String nodeIDDest, String description, Timestamp dateTimeSubmitted, Timestamp pickUpTime, Timestamp dateTimeResolved, User completedBy, User requestedBy) {
        this.nodeID = nodeID;
        this.nodeIDDest = nodeIDDest;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.pickUpTime = pickUpTime;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = dateTimeResolved != null;
        this.completedBy = completedBy;
        this.requestedBy = requestedBy;
        this.ID = ID;
    }

    public InternalTransportationService (int ID, String nodeID, String nodeIDDest, String description, Timestamp dateTimeSubmitted, Timestamp pickUpTime, Timestamp dateTimeResolved) {
        this.nodeID = nodeID;
        this.nodeIDDest = nodeIDDest;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.pickUpTime = pickUpTime;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = dateTimeResolved != null;
        this.completedBy = null;
        this.requestedBy = null;
        this.ID = ID;
    }

    //seperate constructor
    public InternalTransportationService(String location, String destination, String description, Timestamp pickUpTime) {
        this(-1, location, destination, description, new Timestamp(System.currentTimeMillis()), pickUpTime, null);
    }

    public int getID() { return ID; }

    public String getNodeID() {
        return nodeID;
    }

    public String getNodeIDDest() {
        return nodeIDDest;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getPickUpTime() {
        return pickUpTime;
    }

    public String getRequestedBy() {
        if (this.requestedBy != null)
            return requestedBy.getUsername();
        return "User not found";
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    //Update service request once complete
    //get the user who submitted the request and resolved it?
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update INTERNALTRANSPORTATIONREQUESTS set STARTNODEID = ?, ENDNODEID = ?, DESCRIPTION = ?, DATETIMESUBMITTED = ?, PICKUPTIME = ? where ID = ?";
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.nodeIDDest);
            ps.setString(3, this.description);
            ps.setTimestamp(1, ts);
            ps.setTimestamp(4, this.pickUpTime);

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into INTERNALTRANSPORTATIONREQUESTS (STARTNODEID, ENDNODEID, DESCRIPTION, DATETIMESUBMITTED, PICKUPTIME, USERREQUESTEDBY) values (?, ?, ?, ?, ?, ?)";
        Timestamp ts = new Timestamp(System.currentTimeMillis());


        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.nodeIDDest);
            ps.setString(3, this.description);
            ps.setTimestamp(4, ts);
            ps.setTimestamp(5, this.pickUpTime);
            ps.setString(6, Main.user.getUsername());
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE INTERNALTRANSPORTATIONREQUESTS SET DATETIMECOMPLETED = ? WHERE ID = ?";
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
    //Returns an observable list of all internal transportation requests for JavaFX's sake
    public static ObservableList<InternalTransportationService> getAllServiceRequests() {

        ObservableList<InternalTransportationService> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM INTERNALTRANSPORTATIONREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                Timestamp dateTimeSubmitted = rs.getTimestamp("dateTimeSubmitted");
                Timestamp pickUpTime = rs.getTimestamp("PICKUPTIME");
                Timestamp dateTimeResolved = rs.getTimestamp("DATETIMECOMPLETED");
                String nodeID = rs.getString("STARTNODEID");
                String nodeIDDest = rs.getString("ENDNODEID");
                User req = new User(rs.getString("USERREQUESTEDBY"));
                User comp = new User(rs.getString("USERCOMPLETEDBY"));
                InternalTransportationService theServiceRequest = new InternalTransportationService(ID, nodeID, nodeIDDest, description, dateTimeSubmitted, pickUpTime, dateTimeResolved, comp, req);
                requests.add(theServiceRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }
}
