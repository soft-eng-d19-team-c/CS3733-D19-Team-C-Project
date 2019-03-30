package model;

public class PathValue{
    private Node node; // the node the path value is attached to
    private Node previousNode;
    private double totalPathCost;
    private double heuristicCost;
    private boolean visted; // true if visted

    public PathValue(Node node, double totalPathCost, double heuristicCost, boolean visited) {
        this.node = node;
        this.totalPathCost = totalPathCost;
        this.heuristicCost = heuristicCost;
        this.visted = visited;
    }

    public PathValue(Node node, double totalPathCost, double heuristicCost) {
        this.node = node;
        this.totalPathCost = totalPathCost;
        this.heuristicCost = heuristicCost;
        this.visted = false;
    }

    public PathValue(Node node) {
        this.node = node;
        this.totalPathCost = Double.MAX_VALUE;
        this.heuristicCost = Double.MAX_VALUE;
        this.visted = false;
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

    public double getTotalPathCost() {
        return totalPathCost;
    }

    public void setTotalPathCost(double totalPathCost) {
        this.totalPathCost = totalPathCost;
    }

    public double getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(double heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public boolean isVisted() {
        return visted;
    }

    public void setVisted(boolean visted) {
        this.visted = visted;
    }
}