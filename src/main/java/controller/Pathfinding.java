package controller;

import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.twilio.type.PhoneNumber;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.*;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    @FXML private Button dancebtn;
    @FXML private ImageView findpathmap;
    @FXML private AnchorPane findpathview;
    @FXML private Pane imInPane;
    @FXML private TextField origin;
    @FXML private TextField destination;
    @FXML private Button findpathbtn;
    @FXML private AutocompleteSearchBar searchController_origController;
    @FXML private AutocompleteSearchBar searchController_destController;
    @FXML private JFXTextArea phoneNumberInput;
    @FXML private Button phoneNumberBtn;

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

    private String findLocationNodeID;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findpathmap.setImage(new Image(String.valueOf(getClass().getResource("/img/" + (String) Main.screenController.getData("floor") + "_NoIcons.png"))));
        findpathmap.fitWidthProperty().bind(imInPane.widthProperty());
        Platform.runLater(() -> {

            findLocationNodeID = (String)Main.screenController.getData("nodeID");

            dt = new DataTable();
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
            black = new Color(0,0,0,1);
            drawNodes(nodes, edges, black);

            if (findLocationNodeID != null) {
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
//                drawDancingline(prev, n.getID());
                Line line = new Line();
                line.startXProperty().bind(nodeCircles.get(prev).centerXProperty());
                line.startYProperty().bind(nodeCircles.get(prev).centerYProperty());
                line.endXProperty().bind(circle.centerXProperty());
                line.endYProperty().bind(circle.centerYProperty());
                line.setStroke(black);
                line.setStrokeWidth(3.0);
                imInPane.getChildren().add(line);
            }
            imInPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
            prev = n.getID();
        }
    }

    @SuppressWarnings("Duplicates")
    private void dancePartyNode(LinkedList<Node> nodes) {
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
                drawDancingline(prev, n.getID());
            }
            imInPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
            prev = n.getID();
        }
    }

    private void drawDancingline(String start, String end) {
        Color c1 = randomColorGenerator();
        Color c2 = randomColorGenerator();
        Line line = new Line();
        line.startXProperty().bind(nodeCircles.get(start).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(start).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(end).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(end).centerYProperty());
        line.setStroke(c1);
        line.setStrokeWidth(3.0);
        StrokeTransition ft = new StrokeTransition(Duration.millis(400), line, c1, c2);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        imInPane.getChildren().add(line);
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

    private Line generateEdge(Edge e, Color r) {
        if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
            return null; // this edge is not on this floor so we do not draw it
        }
        Line line = new Line();
        line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());
        line.setStroke(r);
        imInPane.getChildren().add(line);
        return line;
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
        phoneNumberBtn.setDisable(false);
    }

    public void sendTextClick(ActionEvent actionEvent){
        String phoneNumber = phoneNumberInput.getText();
        PathToText pathToText = new PathToText(node_onPath);
        String path = pathToText.getDetailedPath();
        pathToText.SmsSender(path, new PhoneNumber("+1" + phoneNumber));

    }

    private Color randomColorGenerator(){
        Random rand = new Random();
        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();
        Color color = new Color(r, g, b, 1);
        return color;
    }

    private void generateDancePartyEdge(Edge e) {
        Color c1 = randomColorGenerator();
        Line line = generateEdge(e, randomColorGenerator());
        Color c2 = randomColorGenerator();
        StrokeTransition ft = new StrokeTransition(Duration.millis(400), line, c1, c2);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    public void Dancebtnclick(ActionEvent actionEvent) {
        dancePartyNode(node_onPath);
    }

/*    public void danceParty() {
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
            Random rand = new Random();
            double mapX = findpathmap.getLayoutX();
            double mapY = findpathmap.getLayoutY();
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.4);
            findpathmap.setEffect(blackout);
            Color c = new Color(0, 0, 0, 1.0);
            bigPane.setBackground(new Background(new BackgroundFill(c, null, null)));
            for (Edge e : adjacencyList) {
                Node startNode = dt.getDataById(e.getStartNode());
                Node endNode = dt.getDataById(e.getEndNode());
                if (startNode != null && endNode != null && startNode.getFloor().equals(endNode.getFloor())) {
                    double r = rand.nextDouble();
                    double g = rand.nextDouble();
                    double b = rand.nextDouble();
                    Color color = new Color(r, g, b, 1);
                    line.setStroke(color);
                    imInPane.getChildren().add(line);
                    r = rand.nextDouble();
                    g = rand.nextDouble();
                    b = rand.nextDouble();
                    Color color2 = new Color(r, g, b, 1.0);
                    StrokeTransition ft = new StrokeTransition(Duration.millis(400), line, color, color2);
                    ft.setCycleCount(Animation.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }
            }
            for (Node n : nodeList) {
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / 4.0);
                circle.setCenterY(mapY + n.getY() / 4.0);
                circle.setRadius(1.0 + (rand.nextDouble() * 5.0));
                double r = rand.nextDouble();
                double g = rand.nextDouble();
                double b = rand.nextDouble();
                Color color = new Color(r, g, b, 1.0);
                imInPane.getChildren().add(circle);
                r = rand.nextDouble();
                g = rand.nextDouble();
                b = rand.nextDouble();
                Color color2 = new Color(r, g, b, 1.0);
                FillTransition ft = new FillTransition(Duration.millis(250), circle, color, color2);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
            }
    }*/
}


