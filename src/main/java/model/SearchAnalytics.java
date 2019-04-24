package model;

import base.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchAnalytics {
    private String nodeID;
    private int count;

    public SearchAnalytics(String nodeID) {
        this.nodeID = nodeID;
        this.insert();
    }

    /**
     * @author Ryan LaMarche
     * try to increment an analytical count of paths found in the application, otherwise insert it.
     */
    private void insert() {
        String sqlStr = "select * from SEARCHES where NODEID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, this.nodeID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sqlUpdateString = "update SEARCHES set count = ? where NODEID = ?";
                PreparedStatement ps1 = Database.getConnection().prepareStatement(sqlUpdateString);
                ps1.setInt(1, rs.getInt("count") + 1);
                ps1.setInt(2, rs.getInt("NODEID"));
                ps1.executeUpdate();
            } else {
                String sqlInsertString = "insert into SEARCHES (NODEID, COUNT) values (?,?)";
                PreparedStatement ps1 = Database.getConnection().prepareStatement(sqlInsertString);
                ps1.setString(1, this.nodeID);
                ps1.setInt(2, 1);
                ps1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public static ObservableList<XYChart.Series> getPathAnalyticsData() {
        ObservableList<XYChart.Series> result = FXCollections.observableArrayList();
        String sqlStr = "select * from SEARCHES order by count desc";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            while (rs.next() && i++ < 10) {
                XYChart.Series s1 = new XYChart.Series();
                ObservableList<XYChart.Data> temp = FXCollections.observableArrayList();
                temp.add(new XYChart.Data<>(rs.getString("NODEID"), rs.getInt("count")));
                s1.setData(temp);
                s1.setName(rs.getString("NODEID"));
                result.add(s1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
