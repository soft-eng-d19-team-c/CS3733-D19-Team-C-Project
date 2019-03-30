package model;

import base.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.lang.Math.sqrt;

public class AStar{
    private Node originNode;
    private Node destNode;
    private boolean isHandicap;
    private HashMap<String, Node> cache;
    private HashMap<String, Edge> currentPath;

    private HashMap<String, LinkedList<Edge>> adjacencyList; // <NodeID, [Edge1, Edge2, ...]>
    private HashMap<String, Node> nodesList; // <NodeID, Node>



    /*
        Warning!!!
        If there are changes made to the nodes you need to make a new Astar object
        This class will retrieve all of the nodes from the database in its constructor
     */





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

    public AStar(){
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

//    public HashMap<String, Edge> dijkstra(Node startNode, Node endNode){
//        ArrayList<PathValue> queueOfNodes = new ArrayList<>();
//        PathValue sNode = new PathValue(startNode.getID(), 0, 0);
//        HashMap<String, Edge> currentPath = new HashMap<>();
//        queueOfNodes.add(sNode);
//        for(PathValue p: queueOfNodes){
//            queueOfNodes.remove(p);
//            if(p.previousNode().equals(endNode.getID())) {
//                return currentPath;
//            }
//            LinkedList<Edge> currentNodeEdges = this.adjacencyList.get(p.previousNode()); //database magic
//            for(Edge e : currentNodeEdges){
//                Node eStartNode = nodesList.get(e.getStartNode());
//                Node eEndNode = nodesList.get(e.getEndNode());
//                boolean needToAdd = true;
//                for(int i = 0; i < queueOfNodes.size(); i++){
//                    //if the node we encounter is already in the list, lower its totalCost if need be
//                    if(e.findOtherNode(p.previousNode()).equals(queueOfNodes.get(i).previousNode())){
//                        if(e.getDistance(eStartNode, eEndNode) < queueOfNodes.get(i).getTotalCostFromStart()){
//                            queueOfNodes.get(i).setTotalCostFromStart(p.getTotalCostFromStart() + e.getDistance());
//                            needToAdd = false;
//                            i = queueOfNodes.size(); //break out of loop
//                        }
//                    }
//                    if(needToAdd && e.getDistance(eStartNode, eEndNode) < queueOfNodes.get(i).getTotalCostFromStart()){
//                        PathValue path = new PathValue(e.findOtherNode(p.previousNode()), p.getTotalCostFromStart()+ e.getDistance(), 0);
//                        queueOfNodes.add(i, path);
//                        needToAdd = false;
//                        i = queueOfNodes.size(); //break out of loop
//                    }
//                }
//                if(needToAdd){
//                    PathValue path = new PathValue(e.findOtherNode(p.previousNode()), p.getTotalCostFromStart()+ e.getDistance(), 0);
//                    queueOfNodes.add(path);
//                }
//            }
//        }
//        return null;
//    }

    // This is a star
    public LinkedList<Node> findPath(Node startNode, Node endNode){
        // keeps track of visited nodes in PathValue class
        // list of nodes kept in nodeList

        HashMap<Node, PathValue> pathValues = new HashMap<>();
        pathValues.put(startNode, new PathValue(startNode));

        Queue<PathValue> queue = new PriorityQueue<>();
        queue.add(pathValues.get(startNode));

        while(!queue.isEmpty()) {
            PathValue currentPathValue = queue.remove();
            Node currentNode = currentPathValue.getNode();

            if (!currentPathValue.visited()){ // makes sure the current node has not be
                currentPathValue.setVisited(true);

                if (currentNode.equals(endNode)) {

                    System.out.println("Found end node!");
                    return createPath(currentNode, startNode, pathValues);
                }

                LinkedList<Edge> adjacentEdges= adjacencyList.get(currentNode.getID());
                LinkedList<Node> adjacentNodes = new LinkedList<>();

                for (Edge e : adjacentEdges){
                    String nodeID = e.findOtherNode(currentNode.getID());
                    adjacentNodes.add(nodesList.get(nodeID));
                }

                for (Node n : adjacentNodes){
                    if (!pathValues.containsKey(n)){
                        pathValues.put(n, new PathValue(n,currentNode));
                    }

                    PathValue path = pathValues.get(n);
                    if (!path.visited()){
                        double costOfPrevToStart;
                        double costFromPrev;
                        double predictedCostToEnd;
                        double totalCost;

                        costOfPrevToStart = currentPathValue.getTotalCostFromStart();
                        costFromPrev = 0;
                        predictedCostToEnd = findEuclideanDistance(n, currentNode);
                        totalCost = costOfPrevToStart + costFromPrev + predictedCostToEnd;
                        //if this calculated total cost is less than a previous total cost, relax the node and set the parent
                        if(totalCost < path.getTotalCost()){
                            path.setTotalCostFromStart(costOfPrevToStart + costFromPrev);
                            path.setPredictedCostToEnd(predictedCostToEnd);
                            path.setTotalCost(totalCost);
                            path.setPreviousNode(currentNode);
                        }
                    }
                }
            }
        }
        return null;
    }

    public LinkedList<Node> findPath() {
        return findPath(originNode, destNode);
    }

    public LinkedList<Node> findPath(String startNode, String endNode) {
        return findPath(nodesList.get(startNode), nodesList.get(endNode));
    }

    private LinkedList<Node> createPath(Node startNode, Node endNode, HashMap<Node, PathValue> pathValues){
        return null; // will return the path between nodes
    }

    private double distanceBetweenFloors(Node a, Node b){
        int aFloor = a.getFloorNumber();
        int bFloor = b.getFloorNumber();
        return Math.abs(aFloor - bFloor)*10;
    }

    private double findEuclideanDistance(Node a, Node b){

        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        int xDistance = aX - bX;
        int yDistance = aY - bY;
        int distSquared = (xDistance*xDistance) + (yDistance*yDistance);
        return sqrt(distSquared) + distanceBetweenFloors(a,b);
    }


}