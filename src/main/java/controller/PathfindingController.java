package controller;

import base.Main;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.twilio.type.PhoneNumber;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.Edge;
import model.Node;
import model.PathScroll;
import model.PathToText;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class PathfindingController extends Controller implements Initializable {
    @FXML private ToggleButton danceBtn;
    @FXML private ImageView findPathImgView;
    @FXML private AnchorPane findPathView;
    @FXML private Pane mapImgPane;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;
    @FXML private JFXTextArea phoneNumberInput;
    @FXML private Button phoneNumberBtn;
    @FXML private Button Floor3;
    @FXML private Button Floor2;
    @FXML private Button Floor1;
    @FXML private Button Ground;
    @FXML private Button L1;
    @FXML private Button L2;
    @FXML private JFXTextArea pathText;
    @FXML private JFXSlider pathScrollBar;

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

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathText.setText(null);
        searchController_destController.refresh();
        searchController_origController.refresh();
        Main.info.getAlgorithm().refresh();
        hasPath = false;
        currentFloor = (String) Main.screenController.getData("floor");
        allButtons.add(Floor3);
        allButtons.add(Floor2);
        allButtons.add(Floor1);
        allButtons.add(Ground);
        allButtons.add(L1);
        allButtons.add(L2);
        pathScrollBar.setVisible(false);
        pathScrollBar.valueProperty().removeListener(pathBarScrollListener);
        updateFloorImg(currentFloor);
        Platform.runLater(() -> {
            displayAllNodes();
            changeButtonColor(currentFloorButton);
        });
    }


    /**
     * Draws all nodes and edges on current floor.
     */
    @SuppressWarnings("Duplicates")
    public void displayAllNodes() {
        findLocationNodeID = (String) Main.screenController.getData("nodeID");

        nodeCircles = new HashMap<>();
        nodeLines = new HashMap<>();
        nodes = Node.getNodesByFloor(currentFloor);
        edges = Edge.getEdgesByFloor(currentFloor);
        black = new Color(0, 0, 0, 1);
        red = new Color(1, 0,0,1);

        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
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
            circle.setRadius(3.0);
            circle.setFill(black);
            mapImgPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
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
                mapImgPane.getChildren().add(line);
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
        String orig_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        nodesOnPath = Main.info.getAlgorithm().findPath(orig_nodeID, dest_nodeID);
        nodesOnPathArray = nodesOnPath.toArray(new Node[nodesOnPath.size()]);
        Node startNode = Node.getNodeByID(searchController_origController.getNodeID());
        changeFloor(startNode.getFloor());
        pathScrollBar.setMin(1);
        pathScrollBar.setValue(1);
        pathScrollBar.setMax(nodesOnPath.size());
        pathScrollBar.valueProperty().addListener(pathBarScrollListener);
        pathScrollBar.setVisible(true);
        pathScroll = new PathScroll(nodesOnPathArray);
        generateNodesAndEdges(nodesOnPath);
        phoneNumberBtn.setDisable(false);
        danceBtn.setVisible(true);
        hasPath = true;
        PathToText pathToText = new PathToText(nodesOnPath);
        pathText.setText(pathToText.getDetailedPath());
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    /**
     * Allows scroll to work when the scrollbar is scrolled
     * @author Fay Whittall
     */
    ChangeListener pathBarScrollListener = new ChangeListener(){
        @Override
        public void changed(ObservableValue arg0, Object arg1, Object arg2) {
            scroll();
        }
    };

    /**
     * Changes the color of the nodes on the path based on where the scrollbar is located
     * Right now, if you switch floors, it just changes the ones that should be redrawn to red
     * @author Fay Whittall
     */
    private void scroll(){
        int oldPosition = pathScroll.getOldPosition();
        int newPosition = (int) pathScrollBar.getValue();
        boolean forwards = false;
        if (oldPosition < newPosition)
            forwards = true;

        Node[] nodesToRedraw = pathScroll.getNodesInRange(newPosition);
        //hard-coded bug fix - make the whole path red if it is at the end of the scroll bar
        if(newPosition == (int) pathScrollBar.getMax()){
            generateNodesAndEdges(nodesOnPath, Color.RED);
        }
        else{
            //draw the nodes again red if moving forward, draw them again black if moving backward
            if(!(nodesOnPathArray[newPosition].getFloor().equals(currentFloor))){
                if (forwards)
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.BLACK);
                else
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.RED);
            }
            for(Node n: nodesToRedraw){
                if(nodeCircles.containsKey(n.getID())){
                    Circle nodeCircle = nodeCircles.get(n.getID());
                    if (nodeCircle.getFill().equals(black)){
                        nodeCircle.setFill(red);
                    }
                    else nodeCircle.setFill(black);
                }
                if (nodeLines.containsKey(n.getID())){
                    Line nodeLine = nodeLines.get(n.getID());
                    if(nodeLine.getStroke().equals(black)){
                        nodeLine.setStroke(red);
                    }
                    else nodeLine.setStroke(black);
                }
            }
        }
        pathScroll.setOldPosition(newPosition);
    }

    private void generateNodesAndEdges(LinkedList<Node> nodes) {
        generateNodesAndEdges(nodes, Color.BLACK);
    }

    /**
     * Draw nodes and edges from a linked list.
     * @param nodes Linked List of nodes to draw from.
     */
    private void generateNodesAndEdges(LinkedList<Node> nodes, Color c) {
        String prev = null;
        double mapX = findPathImgView.getLayoutX();
        double mapY = findPathImgView.getLayoutY();
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        for (Node n : nodes) {
            if (n.getFloor().equals(currentFloor)) { // checks if node is on the current floor
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / mapScale);
                circle.setCenterY(mapY + n.getY() / mapScale);
                circle.setRadius(3.0);
                circle.setFill(c);
                circle.getProperties().put("node", n);
                if (prev != null && nodeCircles.containsKey(prev) && ((Node) nodeCircles.get(prev).getProperties().get("node")).getFloor().equals(currentFloor)) {
                    Line line = new Line();
                    line.startXProperty().bind(nodeCircles.get(prev).centerXProperty());
                    line.startYProperty().bind(nodeCircles.get(prev).centerYProperty());
                    line.endXProperty().bind(circle.centerXProperty());
                    line.endYProperty().bind(circle.centerYProperty());
                    line.setStroke(c);
                    line.setStrokeWidth(3.0);
                    mapImgPane.getChildren().add(line);
                    nodeLines.put(n.getID(), line);
                }
                mapImgPane.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
            } else {
                prev = null;
            }
        }
    }

    public void sendTextClick(ActionEvent actionEvent) {
        String phoneNumber = phoneNumberInput.getText();
        PathToText pathToText = new PathToText(nodesOnPath);
        String path = pathToText.getDetailedPath();
        pathToText.SmsSender(path, new PhoneNumber("+1" + phoneNumber));
    }


    /*

        DANCE PARTY STUFF


     */

    public void dancePartyBtnClick(ActionEvent actionEvent) {
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        if (danceBtn.isSelected()) {
            Node prev = null;
            double mapX = findPathImgView.getLayoutX();
            double mapY = findPathImgView.getLayoutY();
            double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
            for (Node n : nodesOnPath) {
                if (n.getFloor().equals(currentFloor)) {
                    Circle circle = new Circle();
                    circle.setCenterX(mapX + n.getX() / mapScale);
                    circle.setCenterY(mapY + n.getY() / mapScale);
                    circle.setRadius(3.0 + (new Random().nextDouble() * 3.0));
                    circle.getProperties().put("node", n);
                    Color c1 = randomColorGenerator();
                    Color c2 = randomColorGenerator();
                    FillTransition ft = new FillTransition(Duration.millis(517), circle, c1, c2);
                    ft.setAutoReverse(true);
                    ft.setCycleCount(Animation.INDEFINITE);
                    ft.play();
                    if (prev != null && prev.getFloor().equals(currentFloor)) {
                        c1 = randomColorGenerator();
                        c2 = randomColorGenerator();
                        Line line = new Line();
                        line.startXProperty().bind(nodeCircles.get(prev.getID()).centerXProperty());
                        line.startYProperty().bind(nodeCircles.get(prev.getID()).centerYProperty());
                        line.endXProperty().bind(nodeCircles.get(n.getID()).centerXProperty());
                        line.endYProperty().bind(nodeCircles.get(n.getID()).centerYProperty());
                        line.setStroke(c1);
                        line.setStrokeWidth(3.0);
                        StrokeTransition st = new StrokeTransition(Duration.millis(400), line, c1, c2);
                        st.setCycleCount(Animation.INDEFINITE);
                        st.setAutoReverse(true);
                        st.play();
                        mapImgPane.getChildren().add(line);
                    }
                    mapImgPane.getChildren().add(circle);
                    nodeCircles.put(n.getID(), circle);
                }
                prev = n;
            }
            for (Circle c : nodeCircles.values()) {
                c.toFront();
            }
        } else {
            generateNodesAndEdges(nodesOnPath);
        }
    }

    private Color randomColorGenerator() {
        Random rand = new Random();
        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();
        Color color = new Color(r, g, b, 1);
        return color;
    }




    /*


        CHANGE FLOOR STUFF


     */

    public void updateFloorImg(String floor) {
        if (floor.equals("Ground")){
            floor = "G";
        }
        String floorURL;
        switch (floor) {
            case "3":
                floorURL = "3_NoIcons.png";
                currentFloorButton = Floor3;
                break;
            case "2":
                floorURL = "02_thesecondfloor_withbookablelocations.png";
                currentFloorButton = Floor2;
                break;
            case "1":
                floorURL = "01_thefirstfloor.png";
                currentFloorButton = Floor1;
                break;
            case "G":
                floorURL = "00_thegroundfloor.png";
                currentFloorButton = Ground;
                break;
            case "L1":
                floorURL = "00_thelowerlevel1.png";
                currentFloorButton = L1;
                break;
            case "L2":
                floorURL = "00_thelowerlevel2.png";
                currentFloorButton = L2;
                break;
            default:
                System.out.println("Error in PathfindingController.updateFloorImg invalid floor");
                floorURL = "01_thefirstfloor.png";
        }

        //colorFloorsOnPath(nodesOnPath, currentFloor);
        changeButtonColor(currentFloorButton);
        findPathImgView.setImage(new Image(String.valueOf(getClass().getResource("/img/" + floorURL))));
        findPathImgView.fitWidthProperty().bind(mapImgPane.widthProperty());
    }


    public void changeFloor(String floor) {
        changeFloor(floor, Color.BLACK);
    }

    public void changeFloor(String floor, Color c) {
        currentFloor = floor;
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        updateFloorImg(floor);
        if (hasPath) {
            generateNodesAndEdges(nodesOnPath, c);
        } else {
            displayAllNodes();
        }
    }

    public void floor3BtnClick(ActionEvent actionEvent) {
        changeFloor("3");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor2BtnClick(ActionEvent actionEvent) {
        changeFloor("2");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor1BtnClick(ActionEvent actionEvent) {
        changeFloor("1");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void groundBtnClick(ActionEvent actionEvent) {
        changeFloor("G");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L1BtnClick(ActionEvent actionEvent) {
        changeFloor("L1");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L2BtnClick(ActionEvent actionEvent) {
        changeFloor("L2");
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void changeButtonColor(Button button){
        switch (currentFloor){
            case "3": currentFloorButton = Floor3;
                Floor3.setStyle("-fx-background-color: -success");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "2": currentFloorButton = Floor2;
                Floor2.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "1": currentFloorButton = Floor1;
                Floor1.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "G": currentFloorButton = Ground;
                Ground.setStyle(" -fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "L1": currentFloorButton = L1;
                L1.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "L2": currentFloorButton = L2;
                L2.setStyle(" -fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");

                break;
        }
        //colorFloorsOnPath(nodesOnPath); //to keep the floors blue that the path is on (after going to green once on floor)
    }

    public void colorFloorsOnPath(LinkedList<Node> node_onPath, String currentFloor) {
        //if the floor has paths drawn on it setStyle(" -fx-background-color: -primary")
        if (hasPath) {
            LinkedList<String> allFloors = new LinkedList<>();


            for (int i = 0; i < node_onPath.size(); i++) {
                String floor = node_onPath.get(i).getFloor();
                if (allFloors.size() < 6) {
                    if (!allFloors.contains(floor)) {
                        allFloors.add(floor);
                    }
                }
            }

//        System.out.println(allFloors);

            for (int i = 0; i < allFloors.size(); i++) {
                String floor = allFloors.get(i);
                switch (floor) {
                    case "3":
                        Floor3.setStyle("-fx-background-color: -primary");
                        break;
                    case "2":
                        Floor2.setStyle("-fx-background-color: -primary");
                        break;
                    case "1":
                        Floor1.setStyle("-fx-background-color: -primary");
                        break;
                    case "Ground":
                        Ground.setStyle("-fx-background-color: -primary");
                        break;
                    case "L1":
                        L1.setStyle("-fx-background-color: -primary");
                        break;
                    case "L2":
                        L2.setStyle("-fx-background-color: -primary");
                        break;
                }

                if ((!(floor.equals(currentFloor))) && (!(allFloors.contains(floor)))) {
                    System.out.println(floor);
                    switch (floor) {
                        case "3":
                            Floor3.setStyle("-fx-background-color: -secondary");
                            break;
                        case "2":
                            Floor2.setStyle("-fx-background-color: -secondary");
                            break;
                        case "1":
                            Floor1.setStyle("-fx-background-color: -secondary");
                            break;
                        case "Ground":
                            Ground.setStyle("-fx-background-color: -secondary");
                            break;
                        case "L1":
                            L1.setStyle("-fx-background-color: -secondary");
                            break;
                        case "L2":
                            L2.setStyle("-fx-background-color: -secondary");
                            break;
                    }
                }

            }


            switch (currentFloor) {
                case "3":
                    Floor3.setStyle("-fx-background-color: -success");
                    break;
                case "2":
                    Floor2.setStyle("-fx-background-color: -success");
                    break;
                case "1":
                    Floor1.setStyle("-fx-background-color: -success");
                    break;
                case "Ground":
                    Ground.setStyle("-fx-background-color: -success");
                    break;
                case "L1":
                    L1.setStyle("-fx-background-color: -success");
                    break;
                case "L2":
                    L2.setStyle("-fx-background-color: -success");
                    break;
            }


        }
    }


}


