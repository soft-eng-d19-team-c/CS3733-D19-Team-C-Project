package model;

public class Edge {
    private String edgeId;
    private String startNode;
    private String endNode;

    public Edge(String edgeId, String startNode, String endNode) {
        this.edgeId = edgeId;
        this.startNode = startNode;
        this.endNode = endNode;
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

    @Override
    public String toString() {
        return "Edge{" +
                "edgeId='" + edgeId + '\'' +
                ", startNode='" + startNode + '\'' +
                ", endNode='" + endNode + '\'' +
                '}';
    }
}
