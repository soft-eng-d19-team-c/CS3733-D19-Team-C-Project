package controller;

import base.EnumSearchType;
import base.Main;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    @FXML
    private ComboBox<String> algosMenu;

    @FXML
    private ComboBox<String> searchesMenu;

    @FXML
    protected Accordion editNode;

    @FXML private TextField changeIdleTime;
    @FXML private JFXSlider changeTolerance;
    @FXML private Label changeIdleTimeLabel;
    @FXML private Label toleranceLabel;
    @FXML private Label toleranceWordLabel;
    @FXML private NavController navController;

    private LinkedList<Edge> edges;
    private LinkedList<Node> nodes;

    private HashMap<String, Circle> nodeCircles;

    private boolean addEdgeHandler_on = false;
    private String currentFloor;

    private HashMap<String, Node> allNodes = Node.getHashedNodes();

    TitledPane TitledPane = new TitledPane();
    VBox IDContent = new VBox();
    TextField nodeID = new TextField();
    TextField xCoord = new TextField();
    TextField yCoord = new TextField();
    TextField floor = new TextField();
    TextField building = new TextField();
    TextField type = new TextField();
    TextField shortName = new TextField();
    TextField longName = new TextField();

    Button save = new Button();

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.ADMINVIEW);
        algosMenu.setOnAction(null);
        searchesMenu.setOnAction(null);
        floorsMenu.setOnAction(null);
        if (Main.screenController.getData("floor") != null)
            currentFloor = (String) Main.screenController.getData("floor");
        if (currentFloor == null)
            currentFloor = Main.info.getKioskLocation().getFloor();
        if (currentFloor == null)
            currentFloor = "L1";
        changeFloor(currentFloor);
        ObservableList<String> differentFloors = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        "L2",
                        "L1",
                        "G",
                        "1",
                        "2",
                        "3");
        ObservableList<String> differentAlgorithms = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        Main.info.ASTAR.getAlgorithmName(),
                        Main.info.DIJKSTRA.getAlgorithmName(),
                        Main.info.BFS.getAlgorithmName(),
                        Main.info.DFS.getAlgorithmName());
        ObservableList<String> differentSearches = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        "Levenshtein Distance",
                                "Simple Comparison");
        //changeIdleTimeLabel.setText("Change Idle Time (minutes)");
        editNode.getPanes().removeAll(TitledPane);
        editNode.getPanes().addAll(TitledPane);
        IDContent.getChildren().remove(0, IDContent.getChildren().size());
        IDContent.getChildren().add(new Label("Node ID: "));
        IDContent.getChildren().add(nodeID);
        IDContent.getChildren().add(new Label("X Coord: "));
        IDContent.getChildren().add(xCoord);
        IDContent.getChildren().add(new Label("Y Coord: "));
        IDContent.getChildren().add(yCoord);
        IDContent.getChildren().add(new Label("Floor: "));
        IDContent.getChildren().add(floor);
        IDContent.getChildren().add(new Label("Building: "));
        IDContent.getChildren().add(building);
        IDContent.getChildren().add(new Label("Node Type: "));
        IDContent.getChildren().add(type);
        IDContent.getChildren().add(new Label("Short Name: "));
        IDContent.getChildren().add(shortName);
        IDContent.getChildren().add(new Label("Long Name: "));
        IDContent.getChildren().add(longName);
        IDContent.getChildren().add(save);
        save.setText("Save");


        TitledPane.setText("Edit Node Data");
        TitledPane.setContent(IDContent);
        if (editNode.getExpandedPane() != null)
            editNode.getExpandedPane().setExpanded(false);
        editNode.setVisible(false);

        Platform.runLater(() -> {
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor(currentFloor);
            edges = Edge.getEdgesByFloor(currentFloor);
            drawNodes();

            floorsMenu.setItems(differentFloors);
            floorsMenu.setValue(currentFloor);
            floorsMenu.setOnAction((event) -> {
                nodeCircles.clear();
                currentFloor = floorsMenu.getSelectionModel().getSelectedItem();
                changeFloor(currentFloor);
                /*
                if (currentFloor.equals("G")) {
                    mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/00_thegroundfloor.png"))));
                } else {
                    mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/" + currentFloor + "_NoIcons.png"))));
                }

                 */
                nodes = Node.getNodesByFloor(currentFloor);
                edges = Edge.getEdgesByFloor(currentFloor);
                drawNodes();
                if (addEdgeHandler_on) {
                    addPathButtonClick(new ActionEvent());
                }
            });

            algosMenu.setItems(differentAlgorithms);
            algosMenu.setValue(Main.info.getAlgorithm().getAlgorithmName());
            algosMenu.setOnAction(changeAlgorithmHandler);
            searchesMenu.setItems(differentSearches);
            if(Main.info.getSearchType().equals(EnumSearchType.COMPARISON)){
                searchesMenu.setValue("Simple Comparison");
                changeTolerance.setVisible(false);
                toleranceLabel.setVisible(false);
                toleranceWordLabel.setVisible(false);
                changeTolerance.valueProperty().removeListener(changeToleranceLabelListener);
                changeTolerance.valueChangingProperty().removeListener(changeToleranceListener);
            }
            else{
                searchesMenu.setValue("Levenshtein Distance");
                changeTolerance.setVisible(true);
                toleranceLabel.setVisible(true);
                toleranceWordLabel.setVisible(true);
                //toleranceLabel changes AS you scroll
                changeTolerance.valueProperty().addListener(changeToleranceLabelListener);
                //actual tolernace changes once the scrollbar is released
                changeTolerance.valueChangingProperty().addListener(changeToleranceListener);
            }
            searchesMenu.setOnAction(changeSearchHandler);
            changeTolerance.setMin(1);
            changeTolerance.setValue(Main.info.getSearchType().getTolerance());
            changeTolerance.setMax(15);
            toleranceLabel.setText(Integer.toString(Main.info.getSearchType().getTolerance()));
        });
    }

    private void changeFloor(String floor) {
        String floorURL;
        switch (floor) {
            case "3":
                floorURL = "03_thethirdfloor.png";
                break;
            case "2":
                floorURL = "02_thesecondfloor_withbookablelocations.png";
                break;
            case "1":
                floorURL = "01_thefirstfloor.png";
                break;
            case "G":
                floorURL = "00_thegroundfloor.png";
                break;
            case "L1":
                floorURL = "00_thelowerlevel1.png";
                break;
            case "L2":
                floorURL = "00_thelowerlevel2.png";
                break;
            default:
                System.out.println("Error in PathfindingController.updateFloorImg invalid floor");
                floorURL = "01_thefirstfloor.png";
        }
        mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/" + floorURL))));
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
            colorMultifloorNode(e);
        }
        for (Circle c : nodeCircles.values()) {
            c.toFront();
        }
    }

    private void colorMultifloorNode(Edge e) {

        if (e.getEndNode().equals("DHALL00102")) {
            System.out.println("HELP ME");
        }

        if (!allNodes.get(e.getStartNode()).getFloor().equals(allNodes.get(e.getEndNode()).getFloor())) {
            if (nodeCircles.containsKey(e.getStartNode())) {
                nodeCircles.get(e.getStartNode()).setFill(Color.ORANGERED);
            } else {
                nodeCircles.get(e.getEndNode()).setFill(Color.ORANGERED);
            }
        }


        /*

        if (!((Node) nodeCircles.get(e.getStartNode()).getProperties().get("node")).getFloor()
                .equals(((Node) nodeCircles.get(e.getEndNode()).getProperties().get("node")).getFloor())) {
            System.out.println("tests");
        }

        /*

        if (nodeCircles.containsKey(e.getStartNode()) && !nodeCircles.containsKey(e.getEndNode())) {
            Circle c = nodeCircles.get(e.getStartNode());
            c.setFill(Color.ORANGERED);
        } else if (!nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode())) {
            Circle c = nodeCircles.get(e.getEndNode());
            c.setFill(Color.ORANGERED);
        } else {
            Node n1 = Node.getNodeByID(e.getStartNode());
            Node n2 = Node.getNodeByID(e.getEndNode());
            if (!n1.getFloor().equals(n2.getFloor())) {
                System.out.println("EDGE : " + e.getEdgeId());
                System.out.println(n1.getFloor());
                System.out.println(n2.getFloor());
                System.out.println();
            }
        }

         */
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

    @SuppressWarnings("Duplicates")
    public void addPathButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, setKioskNodeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
        addEdgeHandler_on = true;
    }

    @SuppressWarnings("Duplicates")
    public void editNodeButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
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
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, setKioskNodeHandler);
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
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, setKioskNodeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    @SuppressWarnings("Duplicates")
    public void setKioskButtonClick(ActionEvent e){
        for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
            if (node.getProperties().containsKey("node")) {
                // remove drag from nodes when adding path
                node.removeEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, addEdgeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeNodeHandler);
                node.removeEventFilter(MouseEvent.MOUSE_PRESSED, removeEdgeHandler);
                // add event handler for mouse click on node
                node.addEventFilter(MouseEvent.MOUSE_PRESSED, setKioskNodeHandler);
            }
        }
        mapImgPane.getScene().setCursor(Cursor.CROSSHAIR);
    }

    public void setIdleTime(ActionEvent e){
        String time = changeIdleTime.getText();
        boolean canSetTime = true;
        char c;
        if(time.length() == 0){
            canSetTime = false;
        }
        for(int i = 0; i < time.length(); i++){
            c = time.charAt(i);
            if(!((Character.isDigit(c)) || c == ('.'))){
                canSetTime = false;
                i = time.length();
            }
        }
        double timeToSet = Double.parseDouble(time);
        if(timeToSet < 0.1){
            canSetTime = false;
        }
        if(canSetTime){
            Main.info.setIdleTime(timeToSet);
            changeIdleTimeLabel.setText("New Idle Time Set");
        }
        else{
            changeIdleTimeLabel.setText("Error: Invalid entry");
        }
    }

    /*


        Create a lot of event handlers. This was a lot of work :(



     */

    EventHandler<ActionEvent> changeAlgorithmHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            switch (algosMenu.getValue()){
                case "A* Algorithm":
                    Main.info.setAlgorithm(Main.info.ASTAR);
                    break;
                case "Dijkstra's Algorithm":
                    Main.info.setAlgorithm(Main.info.DIJKSTRA);
                    break;
                case "Breadth First Search":
                    Main.info.setAlgorithm(Main.info.BFS);
                    break;
                case "Depth First Search":
                    Main.info.setAlgorithm(Main.info.DFS);
                    break;
            }
        }
    };

    /**
     * changes the search type, enables and disables the tolerance selection
     * @author Fay Whittall
     */
    EventHandler<ActionEvent> changeSearchHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            switch (searchesMenu.getValue()){
                case "Levenshtein Distance":
                    Main.info.setSearchType(EnumSearchType.LEVENSHTEIN);
                    changeTolerance.setVisible(true);
                    toleranceLabel.setVisible(true);
                    toleranceWordLabel.setVisible(true);
                    //toleranceLabel changes AS you scroll
                    changeTolerance.valueProperty().addListener(changeToleranceLabelListener);
                    //actual tolernace changes once the scrollbar is released
                    changeTolerance.valueChangingProperty().addListener(changeToleranceListener);
                    break;
                case "Simple Comparison":
                    Main.info.setSearchType(EnumSearchType.COMPARISON);
                    changeTolerance.setVisible(false);
                    toleranceLabel.setVisible(false);
                    toleranceWordLabel.setVisible(false);
                    changeTolerance.valueProperty().removeListener(changeToleranceLabelListener);
                    changeTolerance.valueChangingProperty().removeListener(changeToleranceListener);
                    break;
            }
        }
    };

    ChangeListener changeToleranceListener = new ChangeListener(){
        @Override
        public void changed(ObservableValue arg0, Object arg1, Object arg2) {
            Main.info.getSearchType().setTolerance((int) changeTolerance.getValue());
        }
    };

    ChangeListener changeToleranceLabelListener = new ChangeListener(){
        @Override
        public void changed(ObservableValue arg0, Object arg1, Object arg2) {
            toleranceLabel.setText(Integer.toString((int) changeTolerance.getValue()));
        }
    };

    EventHandler addNodeHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                double randID = new Random().nextDouble();
                double mapScale = mapImg.getImage().getWidth() / mapImg.getFitWidth();
                Node n = new Node("CUSTOMNODE" + randID, (int) (me.getX() * mapScale), (int) (me.getY() * mapScale), currentFloor, "", "CUSTOM", "New Node", "New Custom Node");
                generateNode(n);
                n.insert();
                allNodes = Node.getHashedNodes();
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
                if (Main.info.getKioskLocation().getID().equals(n.getID())) {
                    return;
                }
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
                    nodes = Node.getNodesByFloor(currentFloor);
                    edges = Edge.getEdgesByFloor(currentFloor);
//                    drawNodes();
                    mapImgPane.getScene().setCursor(Cursor.DEFAULT);
//                    System.out.println(startNodeForAddEdge);
//                    System.out.println(endNodeForAddEdge);
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

    @SuppressWarnings("Duplicates")
    EventHandler editNodeHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getTarget();
                Node n = (Node) circle.getProperties().get("node");

                nodeID.setText(n.getID());
                nodeID.setDisable(true);
                xCoord.setText(Integer.toString(n.getX()));
                yCoord.setText(Integer.toString(n.getY()));
                floor.setText(n.getFloor());
                building.setText(n.getBuilding());
                type.setText(n.getNodeType());
                shortName.setText(n.getShortName());
                longName.setText(n.getLongName());
                editNode.setVisible(true);
                editNode.setExpandedPane(editNode.getPanes().get(0));

                save.setOnAction(event -> {
                    editNode.setVisible(false);

                    try {
                        int X = Integer.parseInt(xCoord.getText());
                        int Y = Integer.parseInt(yCoord.getText());
                        n.setX(X);
                        n.setY(Y);
                    }
                    catch (NumberFormatException exp){
                        return;
                    }

                    if (strshorterthan255(building.getText())){ n.setBuilding(building.getText()); } else {
                        return;
                    }
                    if (strshorterthan255(floor.getText())){ n.setFloor(floor.getText()); } else {
                        return;
                    }
                    if (strshorterthan255(longName.getText())){ n.setLongName(longName.getText()); } else {
                        return;
                    }
                    if (strshorterthan255(shortName.getText())){ n.setShortName(shortName.getText()); } else {
                        return;
                    }
                    if (strshorterthan255(type.getText())){ n.setNodeType(type.getText()); } else {
                        return;
                    }
                    for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                        node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
                        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                        node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                    }
                    mapImgPane.getScene().setCursor(Cursor.DEFAULT);
                    n.update();
                });
            }
        }
    };


    private boolean strshorterthan255 (String str){
        return (str.length() < 255);
    }

    @SuppressWarnings("Duplicates")
    EventHandler setKioskNodeHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getTarget();
                Node n = (Node) circle.getProperties().get("node");
                Main.info.setKioskLocation(n);
                // remove this event handler, set everything to null, add drag event handlers again
                for (javafx.scene.Node node : mapImgPane.getChildren().subList(1, mapImgPane.getChildren().size())) {
                    if (node.getProperties().containsKey("node")) {
                        node.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
                        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragNodeHandler);
                        node.addEventFilter(MouseEvent.MOUSE_RELEASED, undragNodeHandler);
                    }
                }
                mapImgPane.getScene().setCursor(Cursor.DEFAULT);
            }
        }
    };

//a "fake submit" button, to bring you back to the dashboard, appear as if you are saving the page
    public void submitButtonClick(ActionEvent actionEvent) {
        Main.screenController.goBack();
    }


}
