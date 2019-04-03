package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookingLocation {

    private String ID;
    private String type;
    private String title;

    public BookingLocation(String ID, String type, String title) {
        this.ID = ID;
        this.type = type;
        this.title = title;
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


    public static ObservableList<BookingLocation> getAllBookingLocations() {
        ObservableList<BookingLocation> result = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGLOCATIONS";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(str);
            while (rs.next()) {
                result.add(new BookingLocation(rs.getString("ID"), rs.getString("type"), rs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
