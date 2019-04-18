package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AStarOrDijkstra implements IPathFind{
    public abstract String getAlgorithmName();

    public abstract void addPathToQueue(PathValue newPath, PathValue currentPathValue, Queue<PathValue> queue, Node currentNode, Node endNode);

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

                    System.out.println("Number of nodes visited using " + getAlgorithmName() + ": " + count);
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
                        addPathToQueue(path, currentPathValue, queue, currentNode, endNode); //how the paths are added has been moved to AStar and Dijkstra
                    }
                }
            }
        }
        return null;
    }


    //implements pathfinding using nodeIDs rather than nodes
    public HashMap<Node, PathValue> findPath(String startNode, String endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList) {
        return findPath(nodesList.get(startNode), nodesList.get(endNode), isHandicap, nodesList, adjacencyList);
    }
}
