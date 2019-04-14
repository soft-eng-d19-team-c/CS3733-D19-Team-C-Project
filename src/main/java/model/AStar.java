package model;

import java.util.Queue;

import static java.lang.Math.sqrt;

public class AStar extends AStarOrDijkstra{

    public String getAlgorithmName(){
        return "A* Algorithm";
    }

    /**
     *
     * @param newPath
     * @param currentPathValue
     * @param queue
     * @param endNode
     * @author Fay Whittall
     */
    public void addPathToQueue(PathValue newPath, PathValue currentPathValue, Queue<PathValue> queue, Node currentNode, Node endNode){
        double costOfPrevToStart;
        double costFromPrev;
        double predictedCostToEnd;
        double totalCost;

        costOfPrevToStart = currentPathValue.getTotalCostFromStart();
        costFromPrev = findEuclideanDistance(newPath.getNode(), currentNode);
        predictedCostToEnd = findEuclideanDistance(newPath.getNode(), endNode);
        totalCost = costOfPrevToStart + costFromPrev + predictedCostToEnd;
        //if this calculated total cost is less than a previous total cost, relax the node and set the parent
        if (totalCost < newPath.getTotalCost()) {
            newPath.setTotalCostFromStart(costOfPrevToStart + costFromPrev);
            newPath.setPredictedCostToEnd(predictedCostToEnd);
            newPath.setTotalCost(totalCost);
            newPath.setPreviousNode(currentNode);
            queue.add(newPath);
        }
    }

    //finds the number of floors apart two nodes are
    //used to find the predicted cost to end
    private int distanceBetweenFloors(Node a, Node b) {
        int aFloor = a.getFloorNumber();
        int bFloor = b.getFloorNumber();
        return (aFloor - bFloor) * 20;
    }

    //also used to find the predicted cost to the end
    @SuppressWarnings("Duplicates")
    private double findEuclideanDistance(Node a, Node b) {

        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        int xDistance = aX - bX;
        int yDistance = aY - bY;
        int zDistance = distanceBetweenFloors(a,b);
        int distSquared = (xDistance * xDistance) + (yDistance * yDistance) + (zDistance * zDistance);
        return sqrt(distSquared);
    }
}