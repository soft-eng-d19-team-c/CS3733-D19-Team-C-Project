package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GiftStoreRequest {

        private int ID;
        private String location;
        private String sender;
        private String recipient;
        private String gifttype;
        private Timestamp datetimesubmited;
        private Timestamp datetimeresolved;
        private User userResolver;
        private boolean isCompleted;

        public GiftStoreRequest(int ID, String location, String sender, String recipient, String gifttype, Timestamp datetimesubmited, Timestamp datetimeresolved) {
                this.ID = ID;
                this.location = location;
                this.sender = sender;
                this.recipient = recipient;
                this.gifttype = gifttype;
                this.datetimesubmited = datetimesubmited;
                this.datetimeresolved = datetimeresolved;
                this.isCompleted = datetimeresolved != null;
        }

        public GiftStoreRequest(String recipient, String sender, String location, String gifttype) {
                this (-1, location, sender, recipient, gifttype, new Timestamp(System.currentTimeMillis()), null);
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

    public String getGifttype() {
        return gifttype;
    }

    public Timestamp getDatetimesubmited() {
        return datetimesubmited;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void insert(){
                String sqlStr = "insert into createGiftStoreRequests (recipent, sender, location, gifttype, datetimesubmitted) Values (?, ?, ?, ?, ?)";
                try {
                        PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
                        ps.setString(1, this.recipient);
                        ps.setString(2, this.sender);
                        ps.setString(3, this.location);
                        ps.setString(4, this.gifttype);
                        ps.setTimestamp(5, this.datetimesubmited);
                        ps.executeUpdate();
                }
                catch (SQLException e){
                        e.printStackTrace();
                }
        }

        //Returns an observable list of all ServiceRequests for JavaFX's sake
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
    public boolean resolve() {
        String str = "UPDATE createGiftStoreRequests SET DATETIMECOMPLETED = ? WHERE ID = ?";
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
