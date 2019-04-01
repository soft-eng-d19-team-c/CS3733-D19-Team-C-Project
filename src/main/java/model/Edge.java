package model;

import base.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class Edge {
    private String edgeId;
    private String startNode;
    private String endNode;

    public Edge(String edgeId, String startNode, String endNode) {
        this.edgeId = edgeId;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public String getStartNode() {
        return startNode;
    }

    public String getEndNode() {
        return endNode;
    }


    @Override
    public String toString() {
        return "Edge{" +
                "edgeId='" + edgeId + '\'' +
                ", startNode='" + startNode + '\'' +
                ", endNode='" + endNode + '\'' +
                '}';
    }


    public String findOtherNode(String ID){
        if(ID.equals(this.startNode)){
            return this.endNode;
        }
        else if(ID.equals(this.endNode)){
            return this.startNode;
        }
        else return "";
    }

    public static LinkedList<Edge> getEdgesByFloor(String floor) {
        LinkedList<Edge> edges = new LinkedList<>();
        String sqlStmt = "SELECT DISTINCT EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM EDGES INNER JOIN NODES ON EDGES.STARTNODE = NODES.NODEID WHERE NODES.FLOOR='"+floor+"'";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            while (rs.next()) {
                String edgeID = rs.getString("EDGEID");
                String startNode = rs.getString("STARTNODE");
                String endNode = rs.getString("ENDNODE");
                edges.add(new Edge(edgeID, startNode, endNode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edges;
    }

    public static LinkedList<Edge> getEdges() {
        LinkedList<Edge> edges = new LinkedList<>();
        String sqlStmt = "SELECT * FROM EDGES";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt);
            while (rs.next()) {
                String edgeID = rs.getString("EDGEID");
                String startNode = rs.getString("STARTNODE");
                String endNode = rs.getString("ENDNODE");
                edges.add(new Edge(edgeID, startNode, endNode));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edges;
    }
}
