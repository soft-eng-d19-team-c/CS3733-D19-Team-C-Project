package controller;

import base.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.*;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class ROBOTPathfindingController extends Controller implements Initializable {
    @FXML private AnchorPane pathfindingScreen;
    @FXML private ImageView findPathImgView;
    @FXML private AnchorPane findPathView;
    @FXML private Group zoomGroup;
    @FXML private GesturePane mapImgPane;
    @FXML private VBox findPathBar;
    //    @FXML private Pane mapImgPane;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;
    @FXML private NavController navController;
    @FXML private JFXTextArea phoneNumberInput;
    @FXML private JFXButton phoneNumberBtn;
    @FXML private JFXButton clearBtn;
    @FXML private JFXButton Floor4;
    @FXML private JFXButton Floor3;
    @FXML private JFXButton Floor2;
    @FXML private JFXButton Floor1;
    @FXML private JFXButton Ground;
    @FXML private JFXButton L1;
    @FXML private JFXButton L2;
    @FXML private JFXButton setStartBtn;
    @FXML private JFXButton goFromNodeBtn;
    @FXML private JFXTextArea pathText;
    @FXML private JFXSlider pathScrollBar;
    @FXML private Pane searchWrapper;
    @FXML private JFXButton robotButton;
    @FXML private Pane nodePopUpPane;
    @FXML private Label popUpLongName;
    @FXML
    protected Accordion addText;

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Node> nodesOnPath;
    private Node[] nodesOnPathArray;
    private HashMap<String, Circle> nodeCircles;
    private HashMap<String, Line> nodeLines;
    private PathScroll pathScroll;

    private LinkedList<Button> allButtons = new LinkedList<Button>();

    private Color black;
    private Color red;

    private String findLocationNodeID; // this is the node ID that comes from the Find a Room page
    private String currentFloor;
    private boolean hasPath;
    private Button currentFloorButton;
    private HashMap<String, Image> imageCache = new HashMap<>();

    LinkedList<TitledPane> allPanes = new LinkedList<>();


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void initialize(URL location, ResourceBundle resources) {
        findPathImgView.setImage(new Image(String.valueOf(getClass().getResource("/img/FullerMap.png"))));
        navController.setActiveTab(NavTypes.MAP);
        searchController_origController.refreshForRobots();
        searchController_destController.refreshForRobots();
        searchController_origController.setLocation((String) null);
        searchController_destController.setLocation((String) null);
        Platform.runLater(() -> {
            displayAllNodes();
            nodesOnPath = new LinkedList<>();
            Main.info.getAlgorithm().refreshForRobots();
        });
    }

    /**
     * Draws all nodes and edges on current floor.
     */
    @SuppressWarnings("Duplicates")
    public void displayAllNodes() {
        /*
            This was for some other stuff when it was a different page
         */
//        findLocationNodeID = (String) Main.screenController.getData("nodeID");

        nodeCircles = new HashMap<>();
        nodeLines = new HashMap<>();
        nodes = Node.getSearchableRobotNodes();
        edges = Edge.getEdgesForRobot();
        black = new Color(0, 0, 0, 1);
        red = new Color(1, 0,0,1);

        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
        double mapX = findPathImgView.getLayoutX();
        double mapY = findPathImgView.getLayoutY();

        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];

        for (Node n : nodes) {
            orgSceneX[0] = -1;
            orgSceneY[0] = -1;
            Circle circle = new Circle();
            double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
            circle.setCenterX(mapX + n.getX() / mapScale);
            circle.setCenterY(mapY + n.getY() / mapScale);
            circle.setRadius(4.2);
            circle.setFill(black);
            zoomGroup.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
            circle.getProperties().put("node", n);
        }
        for (Edge e : edges) {
            if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
                // this edge is not on this floor so we do not draw it
            } else {
                Line line = new Line();
                line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
                line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
                line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
                line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());
                line.setStroke(black);
                line.setMouseTransparent(true);
                zoomGroup.getChildren().add(line);

            }
        }

        if (findLocationNodeID != null && nodeCircles.containsKey(findLocationNodeID)) {
            Circle foundNode = nodeCircles.get(findLocationNodeID);

            foundNode.setRadius(6.0);
            foundNode.setFill(Color.ORANGERED);
            foundNode.toFront();

            ScaleTransition st = new ScaleTransition(Duration.millis(2000), foundNode);
            st.setByX(1.2);
            st.setByY(1.2);
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);
            st.play();
        }
    }
    /*


        PATHFINDING STUFF


     */
    public void goBtnClick(ActionEvent actionEvent) {
        makePath();
    }

    @SuppressWarnings("Duplicates")
    public void makePath(){
        String orig_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        if (orig_nodeID == null || dest_nodeID == null)
            return;
        nodesOnPath = Main.info.getAlgorithm().findPath(orig_nodeID, dest_nodeID);
//        System.out.println(nodesOnPath.toString());
        generateNodesAndEdges(nodesOnPath);
        PathToText pathToText = new PathToText(nodesOnPath);
        TextInfo pathsByFloor = pathToText.getDetailedPath();
//        System.out.println("Path made");
    }

    private void generateNodesAndEdges(LinkedList<Node> nodes) {
        generateNodesAndEdges(nodes, Color.BLACK);
    }

    /**
     * Draw nodes and edges from a linked list.
     * @param nodes Linked List of nodes to draw from.
     */
    @SuppressWarnings("Duplicates")
    private void generateNodesAndEdges(LinkedList<Node> nodes, Color c) {
        String prev = null;
        double mapX = findPathImgView.getLayoutX();
        double mapY = findPathImgView.getLayoutY();
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
        for (Node n : nodes) {
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / mapScale);
                circle.setCenterY(mapY + n.getY() / mapScale);
                circle.setRadius(5.0);
                circle.setFill(c);
                circle.getProperties().put("node", n);
                if (prev != null) {
                    Line line = new Line();
                    line.startXProperty().bind(nodeCircles.get(prev).centerXProperty());
                    line.startYProperty().bind(nodeCircles.get(prev).centerYProperty());
                    line.endXProperty().bind(circle.centerXProperty());
                    line.endYProperty().bind(circle.centerYProperty());
                    line.setStroke(c);
                    line.setStrokeWidth(3.0);
                    zoomGroup.getChildren().add(line);
                    nodeLines.put(n.getID(), line);
                }
                zoomGroup.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
        }
    }

    public void robotButtonClick(ActionEvent actionEvent){
        new Thread(()->{
            try {
                RobotPOST.doPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @SuppressWarnings("Duplicates")
    public void scrolling(ScrollEvent scrollEvent) {
        zoomGroup.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                Point2D pivotOnTarget = mapImgPane.targetPointAt(new Point2D(e.getX(), e.getY()))
                        .orElse(mapImgPane.targetPointAtViewportCentre());
                // increment of scale makes more sense exponentially instead of linearly
                mapImgPane.animate(Duration.millis(200))
                        .interpolateWith(Interpolator.EASE_BOTH)
                        .zoomBy(mapImgPane.getCurrentScale(), pivotOnTarget);
            }
        });
    }
}


