package base;

import model.AStar;
import model.Node;
import model.PathToText;

import java.util.LinkedList;

/**
 * @author Ryan LaMarche.
 */
public class Main {
    public static Facade screenController;
    public static Database database;
    public static void main(String[] args) {
        database = new Database(false);
//        MainFXML app = new MainFXML();
//        app.main(args);


        // astar test
        AStar astar = new AStar();
        LinkedList<Node> a = astar.findPath("WHALL00602", "EHALL03601");
        LinkedList<Node> b = astar.dijkstra("WHALL00602", "EHALL03601");

        System.out.println("Path length: " + a.size());
        System.out.println(a.equals(b));

        System.out.println("~~~~~~~~~~~~~~~~~~~");
        PathToText text = new PathToText(a);
        String test =  text.getDetailedPath();
        text.SmsSender(test);
        System.out.println("~~~~~~~~~~~~~~~~~~~");


    }
}
