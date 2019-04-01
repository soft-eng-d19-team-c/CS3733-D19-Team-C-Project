package controller;

import base.EnumScreenType;
import base.Main;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Window;
import javafx.util.Duration;
import model.DataTable;
import model.Edge;
import model.Node;
import model.AStar;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    AStar star;
    LinkedList<Node> astarResult;
    @FXML
    private AnchorPane findpathview;
    @FXML
    private TextField origin;
    @FXML
    private TextField destination;
    @FXML
    private Button findpathbtn;
    @FXML private Window dialog1;
    @FXML private AutocompleteSearchBar searchController_origController;
    @FXML private Window dialog2;
    @FXML private AutocompleteSearchBar searchController_destController;


    private DataTable dt;

    private LinkedList<Node> nodeList;

    private HashMap<String, Edge> edgeList;

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
