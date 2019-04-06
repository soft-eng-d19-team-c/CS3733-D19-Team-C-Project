package model;

import java.util.HashMap;
import java.util.LinkedList;

public interface IPathFind {
    HashMap<Node, PathValue> findPath(Node startNode, Node endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList);
    HashMap<Node, PathValue> findPath(String startNode, String endNode, boolean isHandicap, HashMap<String, Node> nodesList, HashMap<String, LinkedList<Edge>> adjacencyList);
}
