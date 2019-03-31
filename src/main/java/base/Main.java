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
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
