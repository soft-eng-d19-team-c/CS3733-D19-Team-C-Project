package model;

public class PathValue{
    private String node;
    private double totalPathCost;
    private double heuristicCost;

    public PathValue(String node, double totalPathCost, double heuristicCost) {
        this.node = node;
        this.totalPathCost = totalPathCost;
        this.heuristicCost = heuristicCost;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
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
}