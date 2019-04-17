package model;

import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class FloristServiceRequest {
    private String startNodeID;
    private String endNodeID;
    private String description;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeResolved;
    private User userResolvedBy;
    private boolean isComplete;
    private int ID;

    public FloristServiceRequest(String startNodeID, String endNodeID, String description, Timestamp dateTimeSubmitted) {
        this (-1, startNodeID, endNodeID, description, dateTimeSubmitted, null, null);
    }

    public FloristServiceRequest(int ID, String startNodeID, String endNodeID, String description, Timestamp dateTimeSubmitted, Timestamp dateTimeResolved, User userResolvedBy) {
        this.ID = ID;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.userResolvedBy = userResolvedBy;
        this.isComplete = dateTimeResolved != null;
    }

    public String getStartNodeID() {
        return startNodeID;
    }

    public String getEndNodeID() {
        return endNodeID;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDateTimeSubmitted() {
        return dateTimeSubmitted;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public int getID() {
        return ID;
    }

    public void insert() {
        String sqlStr = "insert into FLORISTSERVICEREQUESTS (STARTNODEID, ENDNODEID, DESCRIPTION, DATETIMESUBMITTED) values (?,?,?,?)";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, this.startNodeID);
            ps.setString(2, this.endNodeID);
            ps.setString(3, this.description);
            ps.setTimestamp(4, this.dateTimeSubmitted);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<FloristServiceRequest> getAllServiceRequests() {

        ObservableList<FloristServiceRequest> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM FLORISTSERVICEREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String description = rs.getString("description");
                Timestamp dateTimeSubmitted = rs.getTimestamp("DATETIMESUBMITTED");
                Timestamp dateTimeResolved = rs.getTimestamp("DATETIMERESOLVED");
                String startNodeID = rs.getString("STARTNODEID");
                String endNodeID = rs.getString("ENDNODEID");
                FloristServiceRequest sr = new FloristServiceRequest(ID, startNodeID, endNodeID, description, dateTimeSubmitted, dateTimeResolved, null);
                requests.add(sr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public boolean resolve() {
        String str = "UPDATE FLORISTSERVICEREQUESTS SET DATETIMERESOLVED = ? WHERE ID = ?";
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
