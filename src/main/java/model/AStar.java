package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AStar{
    private Node originNode;
    private Node destNode;
    private boolean isHandicap;
    private HashMap<String, Node> cache;
    private HashMap<String, Edge> currentPath;

    public AStar(Node originNode, Node destNode, boolean isHandicap) {
        this.originNode = originNode;
        this.destNode = destNode;
        this.isHandicap = isHandicap;
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
            p.getNode().getEdges(currentPath);
            for(Edge e : currentPath.values()){
                for(int i = 0; i < queueOfNodes.size(); i++){
                    if(e.getDistance() < queueOfNodes.get(i).getTotalPathCost()){
                        PathValue path = new PathValue(e.findOtherNode(p.getNode()), e.getDistance(), 0);
                        queueOfNodes.add(path);
                        i = queueOfNodes.size(); //break out of loop
                    }
                }
            }
        }
    }
}