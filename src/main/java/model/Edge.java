package model;

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
        if(this.distance < 0){
            this.distance = findEuclideanDistance();
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

    private double findEuclideanDistance(){
        int xDistance = startNode.x - endNode.x;
        int yDistance = startNode.y - endNode.y;
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
