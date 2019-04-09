package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

public class ExternalTransportationRequest {
    private int ID;
    private String nodeID;
    private String destination;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimePickup;
    private Timestamp dateTimeResolved;
    private User userResolvedBy;
    private boolean isCompleted;

    public ExternalTransportationRequest(String nodeID, String destination, Timestamp dateTimeSubmitted, Timestamp dateTimePickup) {
        this.nodeID = nodeID;
        this.destination = destination;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimePickup = dateTimePickup;
    }

    public ExternalTransportationRequest(int ID, String nodeID, String destination, Timestamp dateTimeSubmitted, Timestamp dateTimePickup, Timestamp dateTimeResolved, User userResolvedBy, boolean isCompleted) {
        this.ID = ID;
        this.nodeID = nodeID;
        this.destination = destination;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimePickup = dateTimePickup;
        this.dateTimeResolved = dateTimeResolved;
        this.userResolvedBy = userResolvedBy;
        this.isCompleted = isCompleted;
    }

    public static ObservableList<ExternalTransportationRequest> getAllServiceRequests() {
        ObservableList<ExternalTransportationRequest> requests =  FXCollections.observableArrayList();
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM EXTERNALTRANSPORTATIONREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String nodeID = rs.getString("pickupLocation");
                String destination = rs.getString("DESTINATION");
                Timestamp dateTimeSubmitted = rs.getTimestamp("dateTimeSubmitted");
                Timestamp dateTimePickup = rs.getTimestamp("dateTimePickup");
                Timestamp dateTimeResolved = rs.getTimestamp("dateTimeResolved");
                String userID = rs.getString("userCompletedBy");
                User user = null;
                if (userID != null) {
                    user = new User(userID);
                }
                ExternalTransportationRequest req = new ExternalTransportationRequest(ID, nodeID, destination, dateTimeSubmitted, dateTimePickup, dateTimeResolved, user, dateTimeResolved != null);
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void insert() {
        String sqlStr = "insert into EXTERNALTRANSPORTATIONREQUESTS (PICKUPLOCATION, DESTINATION, DATETIMESUBMITTED, DATETIMEPICKUP) values (?,?,?,?)";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, this.nodeID);
            ps.setString(2, this.destination);
            ps.setTimestamp(3, this.dateTimeSubmitted);
            ps.setTimestamp(4, this.dateTimePickup);
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
                ", dateTimePickup=" + dateTimePickup +
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

    public Timestamp getDateTimePickup() {
        return dateTimePickup;
    }

    public Timestamp getDateTimeResolved() {
        return dateTimeResolved;
    }

    public User getUserResolvedBy() {
        return userResolvedBy;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public String getUsername() {
        return userResolvedBy.getUsername();
    }

    public boolean resolve() {
        String str = "UPDATE EXTERNALTRANSPORTATIONREQUESTS SET DATETIMERESOLVED = ?, USERCOMPLETEDBY = ? WHERE ID = ?";
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
