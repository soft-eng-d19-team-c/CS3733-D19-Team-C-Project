package controller;

import base.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import model.AStar;
import model.DataTable;
import model.Edge;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    @FXML private ImageView findpathmap;
    @FXML private AnchorPane findpathview;
    @FXML private Pane imInPane;
    @FXML private TextField origin;
    @FXML private TextField destination;
    @FXML private Button findpathbtn;
    @FXML private AutocompleteSearchBar searchController_origController;
    @FXML private AutocompleteSearchBar searchController_destController;

    AStar star;
    LinkedList<Node> astarResult;

    private DataTable dt;

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Node> node_onPath;
    private LinkedList<Edge> edge_onPath;
    private HashMap<String, Circle> nodeCircles;

    private Color black;
    private Color somecolor;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findpathmap.setImage(new Image(String.valueOf(getClass().getResource("/img/" + (String) Main.screenController.getData("floor") + "_NoIcons.png"))));
        Platform.runLater(() -> {
            dt = new DataTable();
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
//            generateNodes(nodes);
            black = new Color(0,0,0,1);
            drawNodes(nodes, edges, black);
        });
    }

    private void generateNodes(LinkedList<Node> nodes) {
        String prev = null;
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        for (Node n : nodes) {
            Circle circle = new Circle();
            circle.setCenterX(mapX + n.getX()/mapScale);
            circle.setCenterY(mapY + n.getY()/mapScale);
            circle.setRadius(3.0);
            circle.getProperties().put("node", n);

            if (prev != null) {
                Line line = new Line();

                line.startXProperty().bind(nodeCircles.get(prev).centerXProperty());
                line.startYProperty().bind(nodeCircles.get(prev).centerYProperty());

                line.endXProperty().bind(circle.centerXProperty());
                line.endYProperty().bind(circle.centerYProperty());

                line.setStroke(new Color(0, 0, 0, 1));
                line.setStrokeWidth(3.0);
                imInPane.getChildren().add(line);
            }
            imInPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
            prev = n.getID();
        }
    }

    private void drawNodes(LinkedList<Node> nodes_p, LinkedList<Edge> edges_p, Color c) {
        Color w = new Color(1,1,1,1);
        findpathview.setBackground(new Background(new BackgroundFill(w, null, null)));
        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        findpathmap.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();

        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];

        for (Node n : nodes_p){
            generateNode(n, orgSceneX, orgSceneY, mapX, mapY, c);
        }
        for (Edge e : edges_p){
            generateEdge(e, c);
        }
    }
    private void drawNodes(LinkedList<Node> nodes_p, Color c) {
        Color w = new Color(1,1,1,1);
        findpathview.setBackground(new Background(new BackgroundFill(w, null, null)));
        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        findpathmap.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();

        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];
        Node prevNode = null;
        for (Node n : nodes_p) {
            if (prevNode == null) {
                prevNode = n;
                generateNode(n, orgSceneX, orgSceneY, mapX, mapY, c);
            } else {
                generateNode(n, orgSceneX, orgSceneY, mapX, mapY, c);
                Line line = new Line();
                line.startXProperty().bind(nodeCircles.get(prevNode).centerXProperty());
                line.startYProperty().bind(nodeCircles.get(prevNode).centerYProperty());
                line.endXProperty().bind(nodeCircles.get(n).centerXProperty());
                line.endYProperty().bind(nodeCircles.get(n).centerYProperty());
                line.setStroke(c);
                imInPane.getChildren().add(line);
                prevNode = n;
            }
        }
    }

    private void generateEdge(Edge e, Color r) {
        if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
            return; // this edge is not on this floor so we do not draw it
        }
        Line line = new Line();

        line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());

        line.setStroke(r);

        imInPane.getChildren().add(line);
    }


    private void generateNode(Node n, double[] orgSceneX, double[] orgSceneY, double mapX, double mapY, Color c) {
        orgSceneX[0] = -1;
        orgSceneY[0] = -1;
        Circle circle = new Circle();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        circle.setCenterX(mapX + n.getX()/mapScale);
        circle.setCenterY(mapY + n.getY()/mapScale);
        circle.setRadius(3.0);
        circle.setFill(c);
        imInPane.getChildren().add(circle);
        nodeCircles.put(n.getID(), circle);
    }
    public void gobtnclick(ActionEvent actionEvent) {
        /*
        I think here instead of switching screens, we just want to create a new AStar object and findPath
        then display the path on the map. This will all happen on one page.
        HashMap<String, Object> data = new HashMap<>();
        data.put("orgi_nodeID", searchController_origController.getNodeID());
        data.put("dest_nodeID", searchController_destController.getNodeID());
        Main.screenController.setScreen(EnumScreenType.DASHBOARD, data);
         */
        String orgi_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        star = new AStar();
        node_onPath = star.findPath(orgi_nodeID, dest_nodeID);
        somecolor = new Color(0,1,1,1);
//        drawNodes(node_onPath, somecolor);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        generateNodes(node_onPath);
    }
}


