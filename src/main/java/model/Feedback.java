package model;

import base.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Feedback {
    private int ID;
    private int level;

    public Feedback(int level) {
        this.level = level;
        this.insert();
    }

    public Feedback(int ID, int level) {
        this.ID = ID;
        this.level = level;
    }

    public static ObservableList<PieChart.Data> getPieChartData() {
        ObservableList<PieChart.Data> result = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM feedback";
        PreparedStatement ps = null;
        try {
            ps = Database.getConnection().prepareStatement(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = ps.executeQuery();
            int goodCounter = 0;
            int okCounter = 0;
            int badCounter = 0;
            while (rs.next()) {
                switch (rs.getInt("level")) {
                    case 0:
                        badCounter++;
                        break;
                    case 1:
                        okCounter++;
                        break;
                    case 2:
                        goodCounter++;
                        break;
                }
            }
            PieChart.Data goodData = new PieChart.Data("Happy", goodCounter);
            result.add(goodData);
            PieChart.Data okData = new PieChart.Data("Neutral", okCounter);
            result.add(okData);
            PieChart.Data badData = new PieChart.Data("Sad", badCounter);
            result.add(badData);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return result;
    }

    public int getID() {
        return ID;
    }

    public int getLevel() {
        return level;
    }

    public void insert() {
        String s = "insert into FEEDBACK (LEVEL, datetimesubmitted) values (?, ?)";
        try {
            PreparedStatement stmt = Database.getConnection().prepareStatement(s);
            stmt.setInt(1, this.getLevel());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
