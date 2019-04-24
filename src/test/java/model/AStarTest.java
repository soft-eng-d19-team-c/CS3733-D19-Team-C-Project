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

public class AStarTest {
    Database db;
    HashMap<String, LinkedList<Edge>> adjacencyList; // <NodeID, [Edge1, Edge2, ...]>
    HashMap<String, Node> nodesList; // <NodeID, Node>
    Node node1;
    Node node2;
    PathFindingContext dijkstraFinder;
    PathFindingContext aStarFinder;
    AStar aStar;
    Dijkstra dijkstra;

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() throws Exception {
        db = new Database(false, false);


        this.adjacencyList = new HashMap<>();
        this.nodesList = new HashMap<>();
        this.aStar = new AStar();
        this.dijkstra = new Dijkstra();
        String getMeNodesAndEdges = "SELECT DISTINCT NODES.NODEID, NODES.XCOORD, NODES.YCOORD, NODES.FLOOR, NODES.BUILDING, NODES.NODETYPE, NODES.LONGNAME, NODES.SHORTNAME, EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM NODES LEFT JOIN EDGES ON NODES.NODEID=EDGES.STARTNODE OR NODES.NODEID = EDGES.ENDNODE";
        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(getMeNodesAndEdges);
            while (rs.next()) {
                String nodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String floor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String type = rs.getString("NODETYPE");
                String longName = rs.getString("LONGNAME");
                String shortName= rs.getString("SHORTNAME");
                this.nodesList.put(rs.getString("NODEID"), new Node(nodeID, x, y, floor, building, type, longName, shortName));
                String edgeID = rs.getString("EDGEID");
                String startNodeID = rs.getString("STARTNODE");
                String endNodeID = rs.getString("ENDNODE");
                Edge newEdge;
                if (nodeID.equals(startNodeID))
                    newEdge = new Edge(edgeID, startNodeID, endNodeID);
                else
                    newEdge = new Edge(edgeID, endNodeID, startNodeID);
                if (this.adjacencyList.containsKey(nodeID)) {
                    this.adjacencyList.get(nodeID).add(newEdge);
                } else {
                    LinkedList<Edge> newEdgeList = new LinkedList<>();
                    newEdgeList.add(newEdge);
                    this.adjacencyList.put(nodeID, newEdgeList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        node1 = nodesList.get("WHALL00602");
        node2 = nodesList.get("EHALL03601");
        this.dijkstraFinder = new PathFindingContext(this.dijkstra, adjacencyList, nodesList);
        this.aStarFinder = new PathFindingContext(this.aStar, adjacencyList, nodesList);
    }

    @Test
    public void compareAStarAndDijkstra(){
        LinkedList<Node> aStarSameBuildingDiffFloor =  aStarFinder.findPath("WHALL00602", "EHALL03601");
        LinkedList<Node> dijkstraSameBuildingDiffFloor =  dijkstraFinder.findPath("WHALL00602", "EHALL03601");

        LinkedList<Node> aStarSameBuildingSameFloor =  aStarFinder.findPath("DCONF00102", "DEXIT00102");
        LinkedList<Node> dijkstraSameBuildingSameFloor =  dijkstraFinder.findPath("DCONF00102", "DEXIT00102");

        LinkedList<Node> aStarSameNode =  aStarFinder.findPath("DEXIT00102", "DEXIT00102");
        LinkedList<Node> dijkstraSameNode =  dijkstraFinder.findPath("DEXIT00102", "DEXIT00102");

        LinkedList<Node> aStarL1 =  aStarFinder.findPath("CHALL014L1", "CREST002L1");
        LinkedList<Node> dijkstraL1 =  dijkstraFinder.findPath("CHALL014L1", "CREST002L1");

        LinkedList<Node> aStarWithNodes =  aStarFinder.findPath(node1, node2);
        LinkedList<Node> dijkstraWithNodes =  dijkstraFinder.findPath(node1, node2);

        assertEquals(aStarSameBuildingDiffFloor, dijkstraSameBuildingDiffFloor);
        assertEquals(aStarSameBuildingSameFloor, dijkstraSameBuildingSameFloor);
        assertEquals(aStarSameNode, dijkstraSameNode);
        assertEquals(aStarL1, dijkstraL1);
        assertEquals(aStarWithNodes, dijkstraWithNodes);
    }

    @Test
    public void longPath(){
        LinkedList<Node> astar =  aStarFinder.findPath("ADEPT00301", "DHALL00402");
        LinkedList<Node> dijkstra=  dijkstraFinder.findPath("ADEPT00301", "DHALL00402");

        assertEquals(astar, dijkstra);


        astar =  aStarFinder.findPath("DELEV00A02", "ACONF00103");
        dijkstra =  dijkstraFinder.findPath("DELEV00A02", "ACONF00103");

        assertEquals(astar, dijkstra);
    }
    
}