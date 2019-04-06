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
    public static AStar ASTAR = new AStar();
    public static Dijkstra DIJKSTRA = new Dijkstra();
//    public static BFS BFS= new BFS();
//    public static DFS DFS = new DFS();

    public ApplicationInformation(String kioskLocation) {
        this.pathfinding = new PathFindingContext(ASTAR);
        this.kioskLocation = Node.getNodeByID(kioskLocation);
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
     *
     * @param algorithm One of ApplicationInformation.ASTAR, ApplicationInformation.DIJKSTRAS, ...
     */
    public void setAlgorithm(IPathFind algorithm) {
        this.pathfinding.setAlgorithm(algorithm);
    }
}
