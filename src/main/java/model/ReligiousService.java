package model;
import java.util.Date;
import base.Main;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ReligiousService {
    private int ID;
    private String nodeID; //location of request
    private String description; //service request description
    private Date dateTimeSubmitted;
    private Date dateTimeResolved;
    private boolean isComplete;
    private User completedBy;
    private User requestedBy;

    public ReligiousService (int ID, String nodeID, String description, boolean isComplete, Date dateTimeSubmitted, Date dateTimeResolved) {
        this.ID = ID;
        this.nodeID = nodeID;
        this.description = description;
        this.isComplete = isComplete;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.requestedBy = null;
        this.completedBy = null;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getDescription() {
        return description;
    }

    //Determines amount of time service request was completed
    public Date computeTime(){
        long end = dateTimeResolved.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);
    }


    //Update the information in a Religiousequest
    public boolean update(){

        boolean executed = false;

        String sqlCmd = "update RELIGIOUSREQUESTS set LOCATION = ?, DESCRIPTION = ?, TIMESUBMITTED = ?, TIMERESOLVED = ? COMPLETED = ? where ID = ?";
        java.sql.Timestamp sqlCompleteDate = new java.sql.Timestamp(dateTimeResolved.getTime());
        java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(dateTimeSubmitted.getTime());

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setTimestamp(3, sqlStartDate);
            ps.setTimestamp(4, sqlCompleteDate);
            ps.setBoolean(5, isComplete);
            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;
    }

    //Insert a new ReligiousRequest into the database
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into RELIGIOUSREQUESTS (LOCATION, DESCRIPTION, TIMESUBMITTED, COMPLETED) values (?,?,?,?)";
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, nodeID);
            ps.setString(2, description);
            ps.setTimestamp(3, ts);
            ps.setBoolean(4,isComplete);

            executed = ps.execute(); //returns a boolean
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    public static ObservableList<ReligiousService> getAllRelgiousServiceRequests() {

        ObservableList<ReligiousService> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM SERVICEREQUESTS";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                int ID = rs.getInt("ID");
                String nodeID = rs.getString("LOCATION");
                String description = rs.getString("DESCRIPTION");
                Date dateTimeSubmitted = rs.getDate("TIMESUBMITTED");
                Date dateTimeResolved = rs.getDate("TIMECOMPLETED");
                boolean isComplete = rs.getBoolean("COMPLETED");
                ReligiousService religiousRequest = new ReligiousService(ID, description, dateTimeSubmitted, dateTimeResolved, nodeID, isComplete);
                requests.add(religiousRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;


    }


    public int getID() {
        return ID;
    }

    //Mark complete
    public boolean resolve() {
        String str = "UPDATE RELIGIOUSREQUESTS SET TIMECOMPLETED = ? WHERE ID = ?";
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


