package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;

public class Node {
    private String ID;
    private int x;
    private int y;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;


    public String getID() {
        return ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node(String ID, int x, int y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }

    public Node(String ID, int x, int y, String floor, String building, String nodeType, String longName, String shortName) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }
    public Node(String ID, int x, int y, String floor, String building, String nodeType) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = null;
        this.shortName = null;
    }

    public String getFloor() {
        return floor;
    }

    public String getBuilding() {
        return building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setFloor(String floor) {
        // error checks for user entering floor as lowercase l
        if (floor.substring(0,1).equals("l")) {
            floor = "L" + floor.substring(1,2);
        }
        String[] floors = {"L2", "L1", "1", "2", "3"};
        if (Arrays.asList(floors).contains(floor)) {
            this.floor = floor;
        } else {
            System.out.println("Error node.setFloor: did not enter a valid floor");
        }
    }

    public void setBuilding(String building) {
        String[] buildings = {"15 Francis" ,"45 Francis", "BTM", "Shapiro", "Tower"};
        if (Arrays.asList(buildings).contains(building)) {
            this.building = building;
        } else {
            System.out.println("Error node.setBuilding: did not enter a valid floor");
        }

    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }



    @Override
    public String toString() {
        return "Node{" +
                "ID='" + ID + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", floor='" + floor + '\'' +
                ", building='" + building + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }

    // Node connects to the database and updates its values in the database

    /**
     *
     * @return number of updated items
     */
    public int update() {
        try {
            //Statement stmt = connection.createStatement();
            String str = "UPDATE NODES SET XCOORD = ?, YCOORD = ?, FLOOR = ?, " +
                    "BUILDING = ?, NODETYPE = ?,   LONGNAME = ?, SHORTNAME = ? WHERE NODEID = ?";
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            ps.setInt(1, this.getX());
            ps.setInt(2, this.getY());
            ps.setString(3, this.getFloor());
            ps.setString(4, this.getBuilding());
            ps.setString(5, this.getNodeType());
            ps.setString(6, this.getLongName());
            ps.setString(7, this.getShortName());
            ps.setString(8, this.getID());
            int result = ps.executeUpdate();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @SuppressWarnings("Duplicates")
    public static LinkedList<Node> getNodesByFloor(String floor) {
        LinkedList<Node> nodes = new LinkedList<>();
        String sqlStmt = "SELECT * FROM NODES WHERE FLOOR = '" + floor + "'";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            while (rs.next()) {
                String nodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String nodeFloor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String nodeType = rs.getString("NODETYPE");
                String shortName = rs.getString("SHORTNAME");
                String longName = rs.getString("LONGNAME");
                nodes.add(new Node(nodeID, x, y, nodeFloor, building, nodeType, longName, shortName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    @SuppressWarnings("Duplicates")
    public static LinkedList<Node> getNodes() {
        LinkedList<Node> nodes = new LinkedList<>();
        String sqlStmt = "SELECT * FROM NODES";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            while (rs.next()) {
                String nodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String floor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String nodeType = rs.getString("NODETYPE");
                String shortName = rs.getString("SHORTNAME");
                String longName = rs.getString("LONGNAME");
                nodes.add(new Node(nodeID, x, y, floor, building, nodeType, longName, shortName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodes;
    }
    
    @SuppressWarnings("Duplicates")
    public static Node getNodeByID(String nodeID) {
        Node result = null;
        String sqlStmt = "SELECT * FROM NODES WHERE NODEID = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStmt);
            ps.setString(1,nodeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String newNodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String floor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String nodeType = rs.getString("NODETYPE");
                String shortName = rs.getString("SHORTNAME");
                String longName = rs.getString("LONGNAME");
                result = new Node(newNodeID, x, y, floor, building, nodeType, longName, shortName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    // Floor 3  4
    // Floor 2  3
    // Floor 1  2
    // Floor L1 1
    // Floor L2 0
    //used when determining the distance between floors
    public int getFloorNumber(){
        switch (this.floor){
            case "3": return 4;
            case "2": return 3;
            case "1": return 2;
            case "L1": return 1;
            case "L2": return 0;
            default:
                System.out.println("Error in node.getfloornumber");
                return -1;

        }
    }

    public boolean insert() {
        String str = "INSERT INTO NODES (NODEID, XCOORD, YCOORD, FLOOR) VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            ps.setString(1, this.getID());
            ps.setInt(2, this.getX());
            ps.setInt(3, this.getY());
            ps.setString(4, this.getFloor());
            return ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remove() {
        String str = "DELETE FROM NODES WHERE NODEID = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            ps.setString(1, this.getID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
