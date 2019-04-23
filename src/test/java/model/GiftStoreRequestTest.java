//package model;
//
//import base.Database;
//import base.Main;
//import controller.GiftStoreController;
//import javafx.collections.ObservableList;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.sql.*;
//
//import static org.junit.Assert.*;
//
//public class GiftStoreRequestTest {
//    Database db;
//    Timestamp TestStamp = new Timestamp(2000);
//    GiftStoreRequest newRequest = new GiftStoreRequest(null, null, null, null);
//
//    @Before
//    public void setUp() throws Exception {
//        db = new Database(false, false);
//
//        String sqlStr = "insert into createGiftStoreRequests (recipient, sender, location, gifttype, datetimesubmitted) Values (?, ?, ?, ?, ?)";
//
//        try {
//            PreparedStatement ps = Database.getConnection().prepareStatement(sqlStr);
//            ps.setString(1, "TestRecip");
//            ps.setString(2, "TestSend");
//            ps.setString(3, "TestL");
//            ps.setString(4, "TestGift");
//            ps.setTimestamp(5, TestStamp);
//            ps.executeUpdate();
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void getAllServiceRequests() {
//        ObservableList<GiftStoreRequest> storeRequestList;
//        storeRequestList = newRequest.getAllServiceRequests();
//        String s = storeRequestList.toString();
//        //TODO manually make a string that is the expected result
//        assertEquals(s, "ManualString");
//
//    }
//
//    @Test
//    public void insert() {
//        String from = "from";
//        String to = "to";
//        String location = "AHALL01"; //TODO Find an actual node
//        String gifttype = "gifttype";
//        GiftStoreRequest newerRequest = new GiftStoreRequest(from, to, location, gifttype);
//
//        newerRequest.insert();
//        //TODO can't check SQL unless the program runs and can't run the program because of TWILIO
//        String selectS = "SELECT * FROM createGiftStoreRequests WHERE ID = 2";
//        try {
//            Statement stmt = db.getConnection().createStatement();
//            ResultSet rs = stmt.executeQuery(selectS);
//
//            String recipientSQL = rs.getString("recipient");
//            String senderSQL = rs.getString("sender");
//            String locatioNSQL = rs.getString("location");
//            String typeSQL = rs.getString("gifttype");
//            String userCompSQL = rs.getString("usercompletedby");
//
//            //This might be the right location
//            assertEquals(from, recipientSQL);
//            assertEquals(to, senderSQL);
//            assertEquals(location, locatioNSQL);
//            assertEquals(gifttype, typeSQL);
//
//            //TODO check inputs by resolve to make sure they are null for the time being
//            assertEquals(userCompSQL, null);
//
//
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void resolve() {
//        assertEquals(newRequest.resolve(), true);
//    }
//}