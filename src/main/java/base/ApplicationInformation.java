package base;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.util.Duration;
import model.*;

import java.io.IOException;

/**
 * This is a class that will be used to store volatile information
 * about the currently running application. Think about it like a "temporary" database
 * where we can change configuration for the app while it's running.
 * @author Ryan LaMarche
 */
public final class ApplicationInformation {
    Node kioskLocation;
    PathFindingContext pathfinding;
    double idleTime;
    public static AStar ASTAR = new AStar();
    public static Dijkstra DIJKSTRA = new Dijkstra();
    public static BreadthFirstSearch BFS = new BreadthFirstSearch();
    public static DepthFirstSearch DFS = new DepthFirstSearch();
    private EnumSearchType searchType;

    public ApplicationInformation(String kioskLocation) {
        this.pathfinding = new PathFindingContext(ASTAR);
        this.kioskLocation = Node.getNodeByID(kioskLocation);
        this.idleTime = 0.5;
        this.searchType = EnumSearchType.LEVENSHTEIN;
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

    public void setIdleTime(double idleTime) {
        try{
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(EnumScreenType.WELCOME.getPath())));
            Main.idleMonitor.unregister(scene, Event.ANY);
            this.idleTime = idleTime;
            Main.idleMonitor = new IdleMonitor(Duration.minutes(idleTime),
                    () -> Main.screenController.setScreen(EnumScreenType.WELCOME), true);
            Main.idleMonitor.register(scene, Event.ANY);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param algorithm One of ApplicationInformation.ASTAR, ApplicationInformation.DIJKSTRAS, ...
     */
    public void setAlgorithm(IPathFind algorithm) {
        this.pathfinding.setAlgorithm(algorithm);
    }


    public EnumSearchType getSearchType() {return this.searchType;}

    public void setSearchType(EnumSearchType type) {this.searchType = type;}
}
