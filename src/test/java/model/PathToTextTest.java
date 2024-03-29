package model;

import base.Database;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class PathToTextTest {
    Database db;
    HashMap<String, LinkedList<Edge>> adjacencyList; // <NodeID, [Edge1, Edge2, ...]>
    HashMap<String, Node> nodesList; // <NodeID, Node>
    AStar astar;
//
//    @SuppressWarnings("Duplicates")
//    @Before
//    public void setUp() {
//        db = new Database(false, false);
//        this.adjacencyList = new HashMap<>();
//        this.nodesList = new HashMap<>();
//        this.astar = new AStar(adjacencyList, nodesList);
//        String getMeNodesAndEdges = "SELECT DISTINCT NODES.NODEID, NODES.XCOORD, NODES.YCOORD, NODES.FLOOR, NODES.BUILDING, NODES.NODETYPE, NODES.LONGNAME, NODES.SHORTNAME, EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM NODES LEFT JOIN EDGES ON NODES.NODEID=EDGES.STARTNODE OR NODES.NODEID = EDGES.ENDNODE";
//        try {
//            Statement stmt = db.getConnection().createStatement();
//            ResultSet rs = stmt.executeQuery(getMeNodesAndEdges);
//            while (rs.next()) {
//                String nodeID = rs.getString("NODEID");
//                int x = rs.getInt("XCOORD");
//                int y = rs.getInt("YCOORD");
//                String floor = rs.getString("FLOOR");
//                String building = rs.getString("BUILDING");
//                String type = rs.getString("NODETYPE");
//                String longName = rs.getString("LONGNAME");
//                String shortName= rs.getString("SHORTNAME");
//                this.nodesList.put(rs.getString("NODEID"), new Node(nodeID, x, y, floor, building, type, longName, shortName));
//                String edgeID = rs.getString("EDGEID");
//                String startNodeID = rs.getString("STARTNODE");
//                String endNodeID = rs.getString("ENDNODE");
//                Edge newEdge;
//                if (nodeID.equals(startNodeID))
//                    newEdge = new Edge(edgeID, startNodeID, endNodeID);
//                else
//                    newEdge = new Edge(edgeID, endNodeID, startNodeID);
//                if (this.adjacencyList.containsKey(nodeID)) {
//                    this.adjacencyList.get(nodeID).add(newEdge);
//                } else {
//                    LinkedList<Edge> newEdgeList = new LinkedList<>();
//                    newEdgeList.add(newEdge);
//                    this.adjacencyList.put(nodeID, newEdgeList);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void testPathToText1() {
//        LinkedList<Node> path =  astar.findPath("CHALL007L1", "CDEPT003L1");
//        PathToText ptt = new PathToText(path);
////        assertEquals("test", ptt.getDetailedPath());
//    }
}
