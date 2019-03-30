package model;

import base.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
/*
    public static LinkedList<Node> getNodesAndEdgesByFloor(String floor) {
        Object[] result = new Object[2];
//        HashMap<String, LinkedList<Edge>> adjacencyList;
//        adjacencyList = new HashMap<>();
        LinkedList<Edge> edges;
        edges = new LinkedList<>();
//        HashMap<String, Node> nodesList;
//        nodesList = new HashMap<>();
        LinkedList<Node> nodes;
        nodes = new LinkedList<>();
        String getMeNodesAndEdges = "SELECT DISTINCT NODES.NODEID, NODES.XCOORD, NODES.YCOORD, NODES.FLOOR, NODES.BUILDING, NODES.NODETYPE, EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM NODES LEFT JOIN EDGES ON NODES.NODEID=EDGES.STARTNODE WHERE NODES.FLOOR = '"+floor+"'";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(getMeNodesAndEdges);
            while (rs.next()) {
                String nodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String nodeFloor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String type= rs.getString("NODETYPE");
                nodes.add(new Node(nodeID, x, y, floor, building, type));
                String edgeID = rs.getString("EDGEID");
                String startNodeID = rs.getString("STARTNODE");
                String endNodeID = rs.getString("ENDNODE");
                edges.add(new Edge(edgeID, startNodeID, endNodeID));
//                Edge newEdge;
//                if (nodeID.equals(startNodeID))
//                    newEdge = new Edge(edgeID, startNodeID, endNodeID);
//                else
//                    newEdge = new Edge(edgeID, endNodeID, startNodeID);
//                if (adjacencyList.containsKey(nodeID)) {
//                    adjacencyList.get(nodeID).add(newEdge);
//                } else {
//                    LinkedList<Edge> newEdgeList = new LinkedList<>();
//                    newEdgeList.add(newEdge);
//                    adjacencyList.put(nodeID, newEdgeList);
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result[0] = nodes;
        result[1] = edges;
        return result;
    }
 */
}
