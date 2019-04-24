package model;

import base.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

public class PathFindingContext {
    private String originNode;
    private String destNode;
    private boolean isHandicap;
    private HashMap<String, Node> cache;
    private IPathFind algorithm;
    private HashMap<String, LinkedList<Edge>> adjacencyList; // <NodeID, [Edge1, Edge2, ...]>
    private HashMap<String, Node> nodesList; // <NodeID, Node>

    public PathFindingContext(String originNode, String destNode, boolean isHandicap, IPathFind algorithm) {
        this.originNode = originNode;
        this.destNode = destNode;
        this.isHandicap = isHandicap;
        this.algorithm = algorithm;
        this.adjacencyList = new HashMap<>();
        this.nodesList = new HashMap<>();
        this.refresh();
    }

    public PathFindingContext(IPathFind algorithm){
        this.originNode = null;
        this.destNode = null;
        this.isHandicap = false;
        this.algorithm = algorithm;
        this.adjacencyList = new HashMap<>();
        this.nodesList = new HashMap<>();
        this.refreshForRobots();
    }

    public void setHandicap(boolean handicap) {
        isHandicap = handicap;
    }

    public String getAlgorithmName() {
        return this.algorithm.getAlgorithmName();
    }

    public void setAlgorithm(IPathFind algorithm) {
        this.algorithm = algorithm;
    }

    // ONLY USED FOR TESTING
    // TESTS CANNOT USE MAIN SO IT IMPORTS THE DATABASE IN TEST
    public PathFindingContext(IPathFind algorithm, HashMap<String, LinkedList<Edge>> adjacencyList, HashMap<String, Node> nodesList){
        this.isHandicap = false;
        this.adjacencyList = adjacencyList;
        this.nodesList = nodesList;
    }

    @SuppressWarnings("Duplicates")
    public void refresh() {
        System.out.println("------------------ TEST -------------- refresh()");
        /*
        This queries the database and obtains two Hashmaps: one in which Nodes can be accessed
        using NodeIDs, and one in which all edges of can be accessed when given a NodeID. These
        are used in the A* algorithm and are saved in this class as nodesList and adjacencyList,
        respectively.
         */
        this.adjacencyList.clear();
        String getMeNodesAndEdges = "SELECT DISTINCT NODES.NODEID, NODES.XCOORD, NODES.YCOORD, NODES.FLOOR, NODES.BUILDING, NODES.NODETYPE, NODES.LONGNAME, NODES.SHORTNAME, EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM NODES LEFT JOIN EDGES ON NODES.NODEID=EDGES.STARTNODE OR NODES.NODEID = EDGES.ENDNODE";
        try {
            Statement stmt = Database.getConnection().createStatement();
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
    }

    public LinkedList<Node> findPath(){
        HashMap<Node, PathValue> pathValues = this.algorithm.findPath(this.originNode, this.destNode, this.isHandicap, this.nodesList, this.adjacencyList);
        Node sNode = nodesList.get(this.originNode);
        Node eNode = nodesList.get(this.destNode);
        return createPath(eNode, sNode, pathValues);
    }

    public LinkedList<Node> findPath(Node startNode, Node endNode){
        HashMap<Node, PathValue> pathValues = this.algorithm.findPath(startNode, endNode, this.isHandicap, this.nodesList, this.adjacencyList);
        return createPath(endNode, startNode, pathValues);
    }

    public LinkedList<Node> findPath(String startNode, String endNode){


        System.out.println(this.nodesList.toString());
        System.out.println(this.adjacencyList.toString());



        HashMap<Node, PathValue> pathValues = this.algorithm.findPath(startNode, endNode, this.isHandicap, this.nodesList, this.adjacencyList);
        Node sNode = nodesList.get(startNode);
        Node eNode = nodesList.get(endNode);
        return createPath(eNode, sNode, pathValues);
    }

    //this creates the path found by dijkstra or a star as a Linked List of Nodes using the PathValue objects
    private LinkedList<Node> createPath(Node goalNode, Node endNode, HashMap<Node, PathValue> pathValues) {
        LinkedList<Node> path = new LinkedList<>();
        Node temp = goalNode;
        path.add(temp);

        while (!temp.equals(endNode)){
            temp = pathValues.get(temp).getPreviousNode();
            path.add(temp);
        }
        return path;
    }

    @SuppressWarnings("Duplicates")
    public void refreshForRobots() {
        System.out.println("------------------ TEST -------------- refreshForRobots()");
        /*
        This queries the database and obtains two Hashmaps: one in which Nodes can be accessed
        using NodeIDs, and one in which all edges of can be accessed when given a NodeID. These
        are used in the A* algorithm and are saved in this class as nodesList and adjacencyList,
        respectively.
         */
        this.adjacencyList.clear();
        this.nodesList.clear();
        String getMeNodesAndEdges = "SELECT DISTINCT FULLERNODES.NODEID, FULLERNODES.XCOORD, FULLERNODES.YCOORD, FULLERNODES.FLOOR, FULLERNODES.BUILDING, FULLERNODES.NODETYPE, FULLERNODES.LONGNAME, FULLERNODES.SHORTNAME, FULLEREDGES.EDGEID, FULLEREDGES.STARTNODE, FULLEREDGES.ENDNODE FROM FULLERNODES LEFT JOIN FULLEREDGES ON FULLERNODES.NODEID=FULLEREDGES.STARTNODE OR FULLERNODES.NODEID = FULLEREDGES.ENDNODE";
        try {
            Statement stmt = Database.getConnection().createStatement();
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

        System.out.println(this.adjacencyList.toString());
    }
}
