package controller;

import javafx.fxml.Initializable;
import model.AStar;
import model.Node;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    AStar star;
    LinkedList<Node> astarResult;
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        String startNode = (String) Main.screenController.getData("startNode");
        String endNode = (String) Main.screenController.getData("endNode");
        star = new AStar(startNode, endNode);
        astarResult = star.findPath();

         */
    }
}
