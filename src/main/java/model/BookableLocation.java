package model;

import base.Database;
import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookableLocation {

    private String ID;
    private String type;
    private String title;
    private int x;
    private int y;

    public BookableLocation(String ID, String type, String title) {
        this.ID = ID;
        this.type = type;
        this.title = title;
    }

    public BookableLocation(String ID, String type, String title, int x, int y) {
        this.ID = ID;
        this.type = type;
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static ObservableList<BookableLocation> getAllBookingLocations() {
        ObservableList<BookableLocation> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGLOCATIONS";
        try {
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new BookableLocation(rs.getString("ID"), rs.getString("type"), rs.getString("title"), rs.getInt("XCOORD"), rs.getInt("YCOORD")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
