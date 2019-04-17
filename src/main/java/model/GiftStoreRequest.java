package model;

import base.Main;
import base.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GiftStoreRequest {

    private int ID;
    private String location;
    private String sender;
    private String recipient;
    private String giftType;
    private Timestamp dateTimeSubmitted;
    private Timestamp dateTimeResolved;
    private User userResolver;
    private boolean isCompleted;

    public GiftStoreRequest(int ID, String location, String sender, String recipient, String giftType, Timestamp dateTimeSubmitted, Timestamp dateTimeResolved) {
            this.ID = ID;
            this.location = location;
            this.sender = sender;
            this.recipient = recipient;
            this.giftType = giftType;
            this.dateTimeSubmitted = dateTimeSubmitted;
            this.dateTimeResolved = dateTimeResolved;
            this.isCompleted = dateTimeResolved != null;
    }

    public GiftStoreRequest(String recipient, String sender, String location, String giftType) {
            this (-1, location, sender, recipient, giftType, new Timestamp(System.currentTimeMillis()), null);
    }

    public int getID() {
        return ID;
    }

    public String getLocation() {
        return location;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getGiftType() {
        return giftType;
    }

    public Timestamp getDateTimeSubmitted() {
        return dateTimeSubmitted;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void insert(){
            String sqlStr = "insert into createGiftStoreRequests (recipient, sender, location, gifttype, datetimesubmitted) Values (?, ?, ?, ?, ?)";
            try {
                    PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
                    ps.setString(1, this.recipient);
                    ps.setString(2, this.sender);
                    ps.setString(3, this.location);
                    ps.setString(4, this.giftType);
                    ps.setTimestamp(5, this.dateTimeSubmitted);
                    ps.executeUpdate();
            }
            catch (SQLException e){
                    e.printStackTrace();
            }
    }

    //Returns an observable list of all SANITATIONREQUESTS for JavaFX's sake
    public static ObservableList<GiftStoreRequest> getAllServiceRequests() {

            ObservableList<GiftStoreRequest> requests =  FXCollections.observableArrayList();

            try {
                    Statement stmt = Main.database.getConnection().createStatement();
                    String str = "SELECT * FROM createGiftStoreRequests";
                    ResultSet rs = stmt.executeQuery(str);

                    while(rs.next()) {
                            int ID = rs.getInt("ID");
                           String nodeID = rs.getString("location");
                           String sender = rs.getString("sender");
                           String recipient = rs.getString("recipient");
                           String giftType = rs.getString("giftType");
                           Timestamp datetimesubmitted = rs.getTimestamp("DATETIMESUBMITTED");
                           Timestamp datetimeresolved = rs.getTimestamp("DATETIMERESOLVED");
                           GiftStoreRequest sr = new GiftStoreRequest(ID, nodeID, sender, recipient, giftType, datetimesubmitted, datetimeresolved);
                           requests.add(sr);
                    }
            } catch (SQLException e) {
                    e.printStackTrace();
            }
            return requests;


    }
    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE createGiftStoreRequests SET DATETIMERESOLVED = ?, USERCOMPLETEDBY = ? WHERE ID = ?";
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
