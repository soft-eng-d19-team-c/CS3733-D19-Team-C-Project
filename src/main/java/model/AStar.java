package model;

import base.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class AStar{
    private Node originNode;
    private Node destNode;
    private boolean isHandicap;
    private HashMap<String, Node> cache;
    private HashMap<String, Edge> currentPath;

    private HashMap<String, LinkedList<Edge>> adjacencyList; // <NodeID, [Edge1, Edge2, ...]>
    private HashMap<String, Node> nodesList; // <NodeID, Node>

    public AStar(Node originNode, Node destNode, boolean isHandicap) {
        this.originNode = originNode;
        this.destNode = destNode;
        this.isHandicap = isHandicap;
        this.adjacencyList = new HashMap<>();
        this.nodesList = new HashMap<>();
        String getMeNodesAndEdges = "SELECT DISTINCT NODES.NODEID, NODES.XCOORD, NODES.YCOORD, NODES.FLOOR, NODES.BUILDING, NODES.NODETYPE, EDGES.EDGEID, EDGES.STARTNODE, EDGES.ENDNODE FROM NODES LEFT JOIN EDGES ON NODES.NODEID=EDGES.STARTNODE OR NODES.NODEID = EDGES.ENDNODE";
        try {
            Statement stmt = Main.database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(getMeNodesAndEdges);
            while (rs.next()) {
                String nodeID = rs.getString("NODEID");
                int x = rs.getInt("XCOORD");
                int y = rs.getInt("YCOORD");
                String floor = rs.getString("FLOOR");
                String building = rs.getString("BUILDING");
                String type= rs.getString("NODETYPE");
                this.nodesList.put(rs.getString("NODEID"), new Node(nodeID, x, y, floor, building, type));
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

    public HashMap<String, Edge> findPath(Node startNode, Node endNode){
        ArrayList<PathValue> queueOfNodes = new ArrayList<PathValue>();
        PathValue sNode = new PathValue(startNode.getID(), 0, 0);
        HashMap<String, Edge> currentPath = new HashMap<String, Edge>();
        queueOfNodes.add(sNode);
        for(PathValue p: queueOfNodes){
            queueOfNodes.remove(p);
            if(p.getNode().equals(endNode.getID())) {
                return currentPath;
            }
            LinkedList<Edge> currentNodeEdges = this.adjacencyList.get(p.getNode()); //database magic
            for(Edge e : currentNodeEdges){
                Node eStartNode = nodesList.get(e.getStartNode());
                Node eEndNode = nodesList.get(e.getEndNode());
                boolean needToAdd = true;
                for(int i = 0; i < queueOfNodes.size(); i++){
                    if(e.getDistance(eStartNode, eEndNode) < queueOfNodes.get(i).getTotalPathCost()){
                        PathValue path = new PathValue(e.findOtherNode(p.getNode()), e.getDistance(), 0);
                        queueOfNodes.add(i, path);
                        needToAdd = false;
                        i = queueOfNodes.size(); //break out of loop
                    }
                }
                if(needToAdd){
                    PathValue path = new PathValue(e.findOtherNode(p.getNode()), e.getDistance(), 0);
                    queueOfNodes.add(path);
                }
            }
        }
        return null;
    }
}