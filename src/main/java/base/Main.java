package base;

import model.AStar;

/**
 * @author Ryan LaMarche.
 */
public class Main {
    public static Facade screenController;
    public static Database database;
    public static void main(String[] args) {
        database = new Database(false);
        MainFXML app = new MainFXML();
        app.main(args);


        // astar test
        AStar astar = new AStar();
        astar.findPath("AELEV00S02", "BHALL02302");

    }
}
