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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

public class EditMapController extends Controller implements Initializable {

    @FXML
    private AnchorPane bigPane;
    @FXML
    private ImageView mapImg;
    @FXML
    private Pane mapImgPane;

    @FXML
    private ComboBox<String> floorsMenu;

    private LinkedList<Edge> edges;
    private LinkedList<Node> nodes;

    private HashMap<String, Circle> nodeCircles;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void danceParty(ActionEvent e){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+ Main.screenController.getData("floor")+"_NoIcons.png"))));
//        mapImg.fitWidthProperty().bind(mapImgPane.widthProperty()); // this breaks the nodes and gives them a weird offset right now
        ObservableList<String> differentFloors = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        "L2",
                        "L1",
                        "Floor 1",
                        "Floor 2",
                        "Floor 3");

        Platform.runLater(() -> {
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
            drawNodes();

            floorsMenu.setItems(differentFloors);
            floorsMenu.setValue((String) Main.screenController.getData("floor"));
        });
    }

    private void drawNodes() {
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        double mapX = mapImg.getLayoutX();
        double mapY = mapImg.getLayoutY();

        for (Node n : nodes){
            generateNode(n, mapX, mapY);
        }
        for (Edge e : edges){
            generateEdge(e);
        }
    }
    @SuppressWarnings("Duplicates")
    private void generateEdge(Edge e) {
        if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
            return; // this edge is not on this floor so we do not draw it
        }
        Line line = new Line();

        line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());

        line.getProperties().put("edge", e);

        line.setStroke(new Color(0,0,0,1));

        line.setStrokeWidth(3.0);

        mapImgPane.getChildren().add(line);
    }

    private void generateNode(Node n) {
        generateNode(n, mapImg.getLayoutX(), mapImg.getLayoutY());
    }

    private void generateNode(Node n, double mapX, double mapY) {
        Circle circle = new Circle();
        double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
        circle.setCenterX(mapX + n.getX()/mapScale);
        circle.setCenterY(mapY + n.getY()/mapScale);
        circle.setRadius(6.0);
        circle.setCursor(Cursor.HAND);
        circle.getProperties().put("node", n);

        circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);

        circle.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);

        mapImgPane.getChildren().add(circle);
        nodeCircles.put(n.getID(), circle);
    }

    /*


     * When buttons are clicked we add and remove event handlers respectively.




     */

    public void addNodeButtonClick(ActionEvent e){
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
        mapImg.addEventFilter(MouseEvent.MOUSE_PRESSED, addNodeHandler);
    }

    public void addPathButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void editNodeButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, editNodeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }
    @SuppressWarnings("Duplicates")
    public void deleteNodeButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    @SuppressWarnings("Duplicates")
    public void deletePathButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    /*


        Create a lot of event handlers. This was a lot of work :(



     */

    EventHandler addNodeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                double randID = new Random().nextDouble();
                double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
                Node n = new Node("CUSTOMNODE" + randID, (int) (me.getX() * mapScale), (int) (me.getY() * mapScale), (String) Main.screenController.getData("floor"), "", "CUSTOM", "New Node", "New Custom Node");
                generateNode(n);
                n.insert();
                mapImgPane.getScene().setCursor(Cursor.DEFAULT);
                mapImg.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
            }
        }
    };

    EventHandler removeNodeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getTarget();
                Node n = (Node) circle.getProperties().get("node");
                for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                    if (node.getProperties().containsKey("edge")) {
                        Edge e = (Edge) node.getProperties().get("edge");
                        String cursorEdgeStartNodeID = e.getStartNode();
                        String cursorEdgeEndNodeID = e.getEndNode();
                        if (cursorEdgeStartNodeID.equals(n.getID()) || cursorEdgeEndNodeID.equals(n.getID())) {
                            Platform.runLater(() -> mapImgPane.getChildren().remove(node));
                            e.remove();
                        }
                    }
                    node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
                    if (node.getProperties().containsKey("node")) {
                        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                        node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                    }
                }
                mapImgPane.getChildren().remove(me.getTarget());
                n.remove();
                mapImgPane.getScene().setCursor(Cursor.DEFAULT);
            }
        }
    };

    Node startNodeForRemoveEdge = null;
    Node endNodeForRemoveEdge = null;
    @SuppressWarnings("Duplicates")
    EventHandler removeEdgeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getSource();
                Node n = (Node) circle.getProperties().get("node");
                if (startNodeForRemoveEdge == null) {
                    startNodeForRemoveEdge = n;
                } else {
                    endNodeForRemoveEdge = n;
                    for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                        if (node.getProperties().containsKey("edge")) {
                            Edge cursor = (Edge) node.getProperties().get("edge");
                            if ((cursor.getStartNode().equals(startNodeForRemoveEdge.getID()) && cursor.getEndNode().equals(endNodeForRemoveEdge.getID()))
                                || (cursor.getEndNode().equals(startNodeForRemoveEdge.getID()) && cursor.getStartNode().equals(endNodeForRemoveEdge.getID()))) {
                                cursor.remove();
                                Platform.runLater(() -> mapImgPane.getChildren().remove(node));
                            }
                        }
                    }
                    // remove this event handler, set everything to null, add drag event handlers again
                    for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                        if (node.getProperties().containsKey("node")) {
                            node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);

                            node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                            node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                        }
                    }
                    startNodeForRemoveEdge = null;
                    endNodeForRemoveEdge = null;
                    mapImgPane.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        }
    };
    private Node startNodeForAddEdge = null;
    private Node endNodeForAddEdge = null;
    @SuppressWarnings("Duplicates")
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
                    for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                        if (node.getProperties().containsKey("node")) {
                            node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);

                            node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                            node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                        }
                    }
                    startNodeForAddEdge = null;
                    endNodeForAddEdge = null;
                    mapImgPane.getScene().setCursor(Cursor.DEFAULT);
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
        public void handle(MouseEvent me) {
            Circle circle = (Circle) me.getTarget();
            Node n = (Node) circle.getProperties().get("node");
            double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
            n.setX((int) (me.getX() * mapScale));
            n.setY((int) (me.getY() * mapScale));
            n.update();
            orgSceneX = -1;
            orgSceneY = -1;
        }
    };

    EventHandler editNodeHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                    node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
                }
                Circle circle = (Circle) me.getTarget();
                Node n = (Node) circle.getProperties().get("node");
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("node", n);
                Main.screenController.setScreen(EnumScreenType.NODEEDIT, hm);
            }
        }
    };

//a "fake submit" button, to bring you back to the dashboard, appear as if you are saving the page
    public void submitButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
