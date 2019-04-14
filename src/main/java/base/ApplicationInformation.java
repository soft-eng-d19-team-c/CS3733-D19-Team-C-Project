package base;

import model.*;

/**
 * This is a class that will be used to store volatile information
 * about the currently running application. Think about it like a "temporary" database
 * where we can change configuration for the app while it's running.
 * @author Ryan LaMarche
 */
public final class ApplicationInformation {
    Node kioskLocation;
    PathFindingContext pathfinding;
    double timeoutTime;
    public static AStar ASTAR = new AStar();
    public static Dijkstra DIJKSTRA = new Dijkstra();
    public static BreadthFirstSearch BFS = new BreadthFirstSearch();
    public static DepthFirstSearch DFS = new DepthFirstSearch();

    public ApplicationInformation(String kioskLocation) {
        this.pathfinding = new PathFindingContext(ASTAR);
        this.kioskLocation = Node.getNodeByID(kioskLocation);
        this.timeoutTime = 0.5;
    }

    /**
     * @author Ryan LaMarche.
     * @return EnumAlgorithm the current algorithm.
     */
    public PathFindingContext getAlgorithm() {
        return this.pathfinding;
    }

    /**
     * @author Ryan LaMarche.
     * @return Node at kiosk location.
     */
    public Node getKioskLocation() {
        return this.kioskLocation;
    }

    /**
     * @author Ryan LaMarche.
     * @return Set Node kiosk location.
     */
    public void setKioskLocation(Node newLocation){
        this.kioskLocation = newLocation;
    }

    public void setKioskLocation(String newLocation){
        this.kioskLocation = Node.getNodeByID(newLocation);
    }

    /**
     *
     * @param algorithm One of ApplicationInformation.ASTAR, ApplicationInformation.DIJKSTRAS, ...
     */
    public void setAlgorithm(IPathFind algorithm) {
        this.pathfinding.setAlgorithm(algorithm);
    }
}
