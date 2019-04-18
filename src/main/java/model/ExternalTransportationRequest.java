package model;

import base.Database;
import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ExternalTransportationRequest {
    private int ID;
    private String nodeID;
    private String destination;
    private Timestamp dateTimeSubmitted;
    private Timestamp pickupTime;
    private Timestamp dateTimeCompleted;
    private User userCompletedBy;
    private boolean isCompleted;

    public ExternalTransportationRequest(String nodeID, String destination, Timestamp dateTimeSubmitted, Timestamp pickupTime) {
        this.nodeID = nodeID;
        this.destination = destination;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.pickupTime = pickupTime;
    }

    public ExternalTransportationRequest(int ID, String nodeID, String destination, Timestamp dateTimeSubmitted, Timestamp pickupTime, Timestamp dateTimeCompleted, User userCompletedBy, boolean isCompleted) {
        this.ID = ID;
        this.nodeID = nodeID;
        this.destination = destination;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.pickupTime = pickupTime;
        this.dateTimeCompleted = dateTimeCompleted;
        this.userCompletedBy = userCompletedBy;
        this.isCompleted = isCompleted;
    }

    public static ObservableList<ExternalTransportationRequest> getAllServiceRequests() {
        ObservableList<ExternalTransportationRequest> requests =  FXCollections.observableArrayList();
        try {
            Statement stmt = Database.getConnection().createStatement();
            String str = "SELECT * FROM EXTERNALTRANSPORTATIONREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String nodeID = rs.getString("NODEID");
                String destination = rs.getString("DESTINATION");
                Timestamp dateTimeSubmitted = rs.getTimestamp("DATETIMESUBMITTED");
                Timestamp pickupTime = rs.getTimestamp("PICKUPTIME");
                Timestamp dateTimeCompleted = rs.getTimestamp("DATETIMECOMPLETED");
                String userCompletedBy = rs.getString("USERCOMPLETEDBY");
                User user = null;
                if (userCompletedBy != null) {
                    user = new User(userCompletedBy);
                }
                ExternalTransportationRequest req = new ExternalTransportationRequest(ID, nodeID, destination, dateTimeSubmitted, pickupTime, dateTimeCompleted, user, dateTimeCompleted != null);
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void insert() {
        String sqlStr = "insert into EXTERNALTRANSPORTATIONREQUESTS (NODEID, DESTINATION, DATETIMESUBMITTED, PICKUPTIME) values (?,?,?,?)";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.destination);
            ps.setTimestamp(3, this.dateTimeSubmitted);
            ps.setTimestamp(4, this.pickupTime);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ExternalTransportationRequest{" +
                "nodeID='" + nodeID + '\'' +
                ", destination='" + destination + '\'' +
                ", dateTimeSubmitted=" + dateTimeSubmitted +
                ", pickupTime=" + pickupTime +
                '}';
    }


    public int getID() {
        return ID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDestination() {
        return destination;
    }

    public Timestamp getDateTimeSubmitted() {
        return dateTimeSubmitted;
    }

    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public Timestamp getDateTimeCompleted() {
        return dateTimeCompleted;
    }

    public User getUserCompletedBy() {
        return userCompletedBy;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public String getUsername() {
        return userCompletedBy.getUsername();
    }

    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE EXTERNALTRANSPORTATIONREQUESTS SET DATETIMECOMPLETED = ?, USERCOMPLETEDBY = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
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
