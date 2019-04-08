package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ExternalTransportationRequest {
    private String nodeID;
    private String destination;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimePickup;

    public ExternalTransportationRequest(String nodeID, String destination, Timestamp dateTimeSubmitted, Timestamp dateTimePickup) {
        this.nodeID = nodeID;
        this.destination = destination;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimePickup = dateTimePickup;
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
}
