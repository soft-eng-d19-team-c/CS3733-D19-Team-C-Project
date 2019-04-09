package model;

import base.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.lang.Math.sqrt;

public class Dijkstra implements IPathFind{
    public String getAlgorithmName(){
        return "Dijkstra's Algorithm";
    }


    @SuppressWarnings("Duplicates")
    public HashMap<Node, PathValue> findPath(Node startNode, Node endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList) {
        // keeps track of visited nodes in PathValue class
        // list of nodes kept in nodeList
        int count = 0;

        HashMap<Node, PathValue> pathValues = new HashMap<>();
        pathValues.put(startNode, new PathValue(startNode));

        Queue<PathValue> queue = new PriorityQueue<>(new PathValueComparator());
        queue.add(pathValues.get(startNode));


        while (!queue.isEmpty()) {
            PathValue currentPathValue = queue.remove();
            Node currentNode = currentPathValue.getNode();

            if (!currentPathValue.visited()) { // makes sure the current node has not be
                currentPathValue.setVisited(true);
                count++;

                if (currentNode.equals(endNode)) { //the path has been found

                    System.out.println("Number of nodes visited Dijkstra:" + count);
                    return pathValues;
                }

                LinkedList<Edge> adjacentEdges = adjacencyList.get(currentNode.getID());
                LinkedList<Node> adjacentNodes = new LinkedList<>();

                for (Edge e : adjacentEdges) {
                    String nodeID = e.findOtherNode(currentNode.getID());
                    adjacentNodes.add(nodesList.get(nodeID));
                }

                for (Node n : adjacentNodes) {
                    if (!pathValues.containsKey(n)) {
                        pathValues.put(n, new PathValue(n, currentNode));
                    }

                    PathValue path = pathValues.get(n);
                    if (!path.visited()) {
                        double costOfPrevToStart;
                        double costFromPrev;
                        //double predictedCostToEnd;
                        double totalCost;


                        costOfPrevToStart = currentPathValue.getTotalCostFromStart();
                        costFromPrev = findEuclideanDistance(n, currentNode);
                        // predictedCostToEnd = findEuclideanDistance(n, endNode);
                        totalCost = costOfPrevToStart + costFromPrev;
                        //if this calculated total cost is less than a previous total cost, relax the node and set the parent
                        if (totalCost < path.getTotalCost()) {
                            path.setTotalCostFromStart(costOfPrevToStart + costFromPrev);
                            //path.setPredictedCostToEnd(predictedCostToEnd);
                            path.setTotalCost(totalCost);
                            path.setPreviousNode(currentNode);
                            queue.add(path);
                        }
                    }
                }
            }
        }
        return null;
    }

    //implements A star using nodeIDs rather than nodes
    public HashMap<Node, PathValue> findPath(String startNode, String endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList) {
        return findPath(nodesList.get(startNode), nodesList.get(endNode), isHandicap, nodesList, adjacencyList);
    }


    //finds the number of floors apart two nodes are
    //used to find the predicted cost to end
    private int distanceBetweenFloors(Node a, Node b) {
        int aFloor = a.getFloorNumber();
        int bFloor = b.getFloorNumber();
        return (aFloor - bFloor) * 20;
    }

    @SuppressWarnings("Duplicates")
    //also used to find the predicted cost to the end
    private double findEuclideanDistance(Node a, Node b) {

        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        int xDistance = aX - bX;
        int yDistance = aY - bY;
        int zDistance = distanceBetweenFloors(a,b);
        int distSquared = (xDistance * xDistance) + (yDistance * yDistance) + (zDistance * zDistance);
        return sqrt(distSquared);
    }
}