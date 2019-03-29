package model;

import static java.lang.Math.sqrt;

public class Edge {
    private String edgeId;
    private String startNode;
    private String endNode;
    private double distance;

    public Edge(String edgeId, String startNode, String endNode) {
        this.edgeId = edgeId;
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = -1;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public String getStartNode() {
        return startNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public double getDistance(){
        return this.distance;
    }

    public double getDistance(Node sNode, Node eNode){
        if(this.distance < 0){
            this.distance = findEuclideanDistance(sNode, eNode);
        }
        return this.distance;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeId='" + edgeId + '\'' +
                ", startNode='" + startNode + '\'' +
                ", endNode='" + endNode + '\'' +
                '}';
    }

    private double findEuclideanDistance(Node sNode, Node eNode){
        //query database for the nodes from the strings
        int xDistance = sNode.getX() - eNode.getX();
        int yDistance = sNode.getY() - eNode.getY();
        int distSquared = (xDistance*xDistance) + (yDistance*yDistance);
        return sqrt(distSquared);
    }

    public String findOtherNode(String ID){
        if(ID.equals(this.startNode)){
            return this.endNode;
        }
        else if(ID.equals(this.endNode)){
            return this.startNode;
        }
        else return "";
    }
}
