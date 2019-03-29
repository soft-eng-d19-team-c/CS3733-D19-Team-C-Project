package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
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
            System.out.println(result);
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
