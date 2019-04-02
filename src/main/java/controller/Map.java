package controller;

import base.EnumScreenType;
import base.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Edge;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class Map extends Controller implements Initializable {

    @FXML
    private AnchorPane bigPane;
    @FXML
    private ImageView mapImg;
    @FXML
    private Pane imInPane;
    @FXML
    private ToggleButton dancePartyBtn;

    @FXML
    private ComboBox<String> floorsMenu;

    private LinkedList<Edge> edges;
    private LinkedList<Node> nodes;

    private HashMap<String, Circle> nodeCircles;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void backBtnClick(ActionEvent e) {
        Main.screenController.setScreen(EnumScreenType.NODETABLE);
    }

    public void danceParty(ActionEvent e){}

    /*
    public void danceParty(ActionEvent event) {
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        if (dancePartyBtn.isSelected()) {
            Random rand = new Random();
            double mapX = mapImg.getLayoutX();
            double mapY = mapImg.getLayoutY();
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.4);
            mapImg.setEffect(blackout);
            Color c = new Color(0, 0, 0, 1.0);
            bigPane.setBackground(new Background(new BackgroundFill(c, null, null)));
            dancePartyBtn.setTextFill(new Color(1, 1, 1, 1.0));
            for (Edge e : adjacencyList) {
                Node startNode = dt.getDataById(e.getStartNode());
                Node endNode = dt.getDataById(e.getEndNode());
                if (startNode != null && endNode != null && startNode.getFloor().equals(endNode.getFloor())) {
                    Line line = new Line();
                    line.setStartX(mapX + (startNode.getX() / 4));
                    line.setStartY(mapY + (startNode.getY()) / 4);
                    line.setEndX(mapX + (endNode.getX()) / 4);
                    line.setEndY(mapY + (endNode.getY()) / 4);
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
        } else {
            drawNodes();
        }
    }
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+ Main.screenController.getData("floor")+"_NoIcons.png"))));
        ObservableList<String> differentFloors =
                FXCollections.observableArrayList(
                        "Floor 1",
                        "Floor 2",
                        "Floor 3");

        Platform.runLater(() -> {
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
            dancePartyBtn.setSelected(false);
            drawNodes();

            floorsMenu.setItems(differentFloors);
        });
    }

    private void drawNodes() {
        Color c = new Color(1,1,1,1.0);
        bigPane.setBackground(new Background(new BackgroundFill(c, null, null)));
        dancePartyBtn.setTextFill(new Color(0, 0, 0, 1.0));

        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        mapImg.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        double mapX = mapImg.getLayoutX();
        double mapY = mapImg.getLayoutY();

        for (Node n : nodes){
            generateNode(n, mapX, mapY);
        }
        for (Edge e : edges){
            generateEdge(e);
        }
    }

    private void generateEdge(Edge e) {
        if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
            return; // this edge is not on this floor so we do not draw it
        }
        Line line = new Line();

        line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());

        line.setStroke(new Color(0,0,0,1));

        imInPane.getChildren().add(line);
    }

    private void generateNode(Node n) {
        generateNode(n, mapImg.getLayoutX(), mapImg.getLayoutY());
    }

    private void generateNode(Node n, double mapX, double mapY) {
        Circle circle = new Circle();
        double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
        circle.setCenterX(mapX + n.getX()/mapScale);
        circle.setCenterY(mapY + n.getY()/mapScale);
        circle.setRadius(3.0);
        circle.setCursor(Cursor.HAND);
        circle.getProperties().put("node", n);

        circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);

        circle.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);

        imInPane.getChildren().add(circle);
        nodeCircles.put(n.getID(), circle);
    }


    public void addNodeButtonClick(ActionEvent e){
        imInPane.getScene().setCursor(Cursor.CROSSHAIR);
        mapImg.addEventFilter(MouseEvent.MOUSE_PRESSED, addNodeHandler);
    }

    public void addPathButtonClick(ActionEvent e){
        for (javafx.scene.Node node : imInPane.getChildren().subList(1, imInPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
            }
        }
        imInPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void editNodeButtonClick(ActionEvent e){

        System.out.println("node edited");
    }

    public void deleteNodeButtonClick(ActionEvent e){

        System.out.println("node deleted");
    }

    public void deletePathButtonClick(ActionEvent e){

        System.out.println("path deleted");
    }

    EventHandler addNodeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                double randID = new Random().nextDouble();
                double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
                Node n = new Node("CUSTOMNODE" + randID, (int) (me.getX() * mapScale), (int) (me.getY() * mapScale));
                generateNode(n);
                n.setFloor((String) Main.screenController.getData("floor"));
                n.insert();
                imInPane.getScene().setCursor(Cursor.DEFAULT);
                mapImg.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
            }
        }
    };

    private Node startNodeForAddEdge = null;
    private Node endNodeForAddEdge = null;
    EventHandler addEdgeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getSource();
                Node n = (Node) circle.getProperties().get("node");
                if (startNodeForAddEdge == null) {
                    startNodeForAddEdge = n;
                } else {
                    endNodeForAddEdge = n;
                    double randID = new Random().nextDouble();
                    double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
                    Edge e = new Edge("CUSTOMEDGE" + randID, startNodeForAddEdge.getID(), endNodeForAddEdge.getID());
                    generateEdge(e);
                    e.insert();
                    // remove this event handler, set everything to null, add drag event handlers again
                    for (javafx.scene.Node node : imInPane.getChildren().subList(1, imInPane.getChildren().size())) {
                        if (node.getProperties().containsKey("node")) {
                            node.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);

                            node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                            node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                        }
                    }
                    startNodeForAddEdge = null;
                    endNodeForAddEdge = null;
                    imInPane.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        }
    };

    private double orgSceneX = -1;
    private double orgSceneY = -1;

    EventHandler dragNodeHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
            Circle circle = ((Circle) me.getSource());
            if (orgSceneX == -1) {
                orgSceneX = me.getSceneX();
            }
            if (orgSceneY == -1) {
                orgSceneY = me.getSceneY();
            }
            circle.toFront();
            double offsetX = me.getSceneX() - orgSceneX;
            double offsetY = me.getSceneY() - orgSceneY;

            circle.setCenterX(circle.getCenterX() + offsetX);
            circle.setCenterY(circle.getCenterY() + offsetY);

            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
        }
    };
    EventHandler undragNodeHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            orgSceneX = -1;
            orgSceneY = -1;
        }
    };

}
