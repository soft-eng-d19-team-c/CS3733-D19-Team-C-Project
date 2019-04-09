package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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

        public GiftStoreRequest(int ID, String location, String sender, String recipient, String gifttype, Timestamp datetimesubmited, Timestamp datetimeresolved, User userResolver, boolean isCompleted) {
                this.ID = ID;
                this.location = location;
                this.sender = sender;
                this.recipient = recipient;
                this.gifttype = gifttype;
                this.datetimesubmited = datetimesubmited;
                this.datetimeresolved = datetimeresolved;
                this.userResolver = userResolver;
                this.isCompleted = isCompleted;
        }

        public GiftStoreRequest(String recipient, String sender, String location, String gifttype, Timestamp datetimesubmited) {
                this.location = location;
                this.sender = sender;
                this.recipient = recipient;
                this.gifttype = gifttype;
                this.datetimesubmited = datetimesubmited;
        }

        public void insert(){
                String sqlStr = "insert into giftStoreRequestTable (recipent, sender, location, gifttype, datetimesubmitted) Values (?, ?, ?, ?, ?)";
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
}
