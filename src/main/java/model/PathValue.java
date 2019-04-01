package model;

public class PathValue{
    private Node node; // the node the path value is attached to
    private Node previousNode;
    private double totalCostFromStart;
    private double predictedCostToEnd;
    private double totalCost;
    private boolean visited; // true if visited

    public PathValue(Node node) {
        this.node = node;
        this.totalCostFromStart = 0;
        this.predictedCostToEnd = 0;
        this.visited = false;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public PathValue(Node node, Node previousNode) {
        this.node = node;
        this.previousNode = previousNode;
        this.totalCostFromStart = Double.MAX_VALUE;
        this.predictedCostToEnd = Double.MAX_VALUE;
        this.totalCost = Double.MAX_VALUE;
        this.visited = false;
    }


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public double getTotalCostFromStart() {
        return totalCostFromStart;
    }

    public void setTotalCostFromStart(double totalCostFromStart) {
        this.totalCostFromStart = totalCostFromStart;
    }

    public double getPredictedCostToEnd() {
        return predictedCostToEnd;
    }

    public void setPredictedCostToEnd(double predictedCostToEnd) {
        this.predictedCostToEnd = predictedCostToEnd;
    }

    public boolean visited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}