package model;

import java.util.*;

public class BreadthFirstSearch implements IPathFind {

    public String getAlgorithmName(){
        return "Breadth First Search";
    }

    @SuppressWarnings("Duplicates")
    public HashMap<Node, PathValue> findPath(Node startNode, Node endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList) {
        // keeps track of visited nodes in PathValue class
        // list of nodes kept in nodeList
        int count = 0;

        HashMap<Node, PathValue> pathValues = new HashMap<>();
        pathValues.put(startNode, new PathValue(startNode));

        Queue<PathValue> queue = new LinkedList<>();
        queue.add(pathValues.get(startNode));

        while (!queue.isEmpty()) {
            PathValue currentPathValue = queue.remove();
            Node currentNode = currentPathValue.getNode();

            if (!currentPathValue.visited()) { // makes sure the current node has not be
                currentPathValue.setVisited(true);
                count++;

                //System.out.println(currentNode);

                if (currentNode.equals(endNode)) { //path has been found

                    System.out.println("Number of nodes visited by bfs: " + count);
                    return pathValues;
                }

                //obtain the edges connected to the node
                LinkedList<Edge> adjacentEdges = adjacencyList.get(currentNode.getID());
                LinkedList<Node> adjacentNodes = new LinkedList<>();

                //add the nodes connected by the edges to the current node to adjacentNodes
                for (Edge e : adjacentEdges) {
                    String nodeID = e.findOtherNode(currentNode.getID());
                    adjacentNodes.add(nodesList.get(nodeID));
                }

                for (Node n : adjacentNodes) { //add the nodes to the priority queue, if necessary
                    if (!pathValues.containsKey(n)) {
                        pathValues.put(n, new PathValue(n, currentNode));
                    }
                    PathValue path = pathValues.get(n);
                    //prevents any stairs from being searched for
                    if(isHandicap &&
                            n.getNodeType().equals("STAI") &&
                            n != startNode  &&
                            n != endNode){
                        path.setVisited(true);
                    }
                    if (!path.visited()) { //we do not need to add nodes that have already been visited
                        queue.add(path);
                    }
                }
            }
        }
        return null;
    }


    @Override
    public HashMap<Node, PathValue> findPath(String startNode, String endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList) {
        return findPath(nodesList.get(startNode), nodesList.get(endNode), isHandicap, nodesList, adjacencyList);
    }
}
