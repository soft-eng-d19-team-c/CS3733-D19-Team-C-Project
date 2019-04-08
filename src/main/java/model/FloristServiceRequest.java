package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FloristServiceRequest {
    private String startNodeID;
    private String endNodeID;
    private String description;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeResolved;
    private User userResolvedBy;
    private int ID;

    public FloristServiceRequest(String startNodeID, String endNodeID, String description, Timestamp dateTimeSubmitted) {
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
    }

    public FloristServiceRequest(int ID, String startNodeID, String endNodeID, String description, Timestamp dateTimeSubmitted, Timestamp dateTimeResolved, User userResolvedBy) {
        this.ID = ID;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.userResolvedBy = userResolvedBy;
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
}
