package model;

import base.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PathAnalytics {
    private int ID;
    private String startNodeID;
    private String endNodeID;
    private String algorithm;
    private int count;

    /**
     * Creates a new path analytics object and tries to increment it in the database, otherwise insert it.
     * @author Ryan LaMarche
     * @param startNodeID
     * @param endNodeID
     * @param algorithm
     */
    public PathAnalytics(String startNodeID, String endNodeID, String algorithm) {
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.algorithm = algorithm;
        this.insert();
    }

    /**
     * @author Ryan LaMarche
     * try to increment an analytical count of paths found in the application, otherwise insert it.
     */
    private void insert() {
        String sqlStr = "select * from PATHS where STARTNODEID = ? and ENDNODEID = ? and ALGORITHM = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, this.startNodeID);
            ps.setString(2, this.endNodeID);
            ps.setString(3, this.algorithm);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sqlUpdateString = "update PATHS set count = ? where ID = ?";
                PreparedStatement ps1 = Database.getConnection().prepareStatement(sqlUpdateString);
                ps1.setInt(1, rs.getInt("count") + 1);
                ps1.setInt(2, rs.getInt("ID"));
                ps1.executeUpdate();
            } else {
                String sqlInsertString = "insert into PATHS (startNodeID, endNodeID, algorithm, count) values (?,?,?,?)";
                PreparedStatement ps1 = Database.getConnection().prepareStatement(sqlInsertString);
                ps1.setString(1, this.startNodeID);
                ps1.setString(2, this.endNodeID);
                ps1.setString(3, this.algorithm);
                ps1.setInt(4,1);
                ps1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<XYChart.Series> getPathAnalyticsData() {
        ObservableList<XYChart.Series> result = FXCollections.observableArrayList();
        String sqlStr = "select * from paths order by count desc";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next() && i++ < 5) {
                XYChart.Series s1 = new XYChart.Series();
                ObservableList<XYChart.Data> temp = FXCollections.observableArrayList();
                temp.add(new XYChart.Data<>(Integer.toString(rs.getInt("ID")), rs.getInt("count")));
                s1.setData(temp);
                s1.setName(rs.getString("STARTNODEID") + " to " + rs.getString("ENDNODEID") + " using " + rs.getString("algorithm"));
                result.add(s1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
