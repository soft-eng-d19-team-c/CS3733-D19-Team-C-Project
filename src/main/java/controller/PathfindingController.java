package controller;

import base.Main;
import com.jfoenix.controls.JFXButton;
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

import javax.swing.*;
import java.net.URL;
import java.util.*;

public class PathfindingController extends Controller implements Initializable {
    public AutocompleteSearchBarController autocompletesearchbarController;
    @FXML
    private ToggleButton danceBtn;
    @FXML
    private ToggleButton handicapBtn;
    @FXML
    private ImageView findPathImgView;
    @FXML
    private ImageView Gif;
    @FXML
    private AnchorPane findPathView;
    @FXML
    private Pane mapImgPane;
    @FXML
    private AutocompleteSearchBarController searchController_origController;
    @FXML
    private AutocompleteSearchBarController searchController_destController;
    @FXML
    private NavController navController;
    @FXML
    private JFXTextArea phoneNumberInput;
    @FXML
    private JFXButton phoneNumberBtn;
    @FXML
    private JFXButton clearBtn;
    @FXML
    private JFXButton Floor3;
    @FXML
    private JFXButton Floor2;
    @FXML
    private JFXButton Floor1;
    @FXML
    private JFXButton Ground;
    @FXML
    private JFXButton L1;
    @FXML
    private JFXButton L2;
    @FXML
    private JFXButton playBtn;
    @FXML
    private JFXTextArea pathText;
    @FXML
    private JFXSlider pathScrollBar;
    @FXML
    private Pane searchWrapper;

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

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodesOnPath = new LinkedList<>();
        navController.setActiveTab(NavTypes.MAP);
        pathText.setText(null);
        danceBtn.setSelected(false);
        danceBtn.setDisable(false);
        playBtn.setVisible(false);
        phoneNumberInput.setText(null);
        if (Main.screenController.getData("showSearch") != null && (Boolean) Main.screenController.getData("showSearch")) {
            searchWrapper.setVisible(true);
            navController.setActiveTab(NavTypes.FINDROOM);
        } else {
            searchWrapper.setVisible(false);
            navController.setActiveTab(NavTypes.MAP);
        }
        findLocationNodeID = null;
        searchController_origController.refresh();
        searchController_destController.refresh();
        searchController_destController.setLocation(null);
        autocompletesearchbarController.refresh();
        autocompletesearchbarController.setLocation(null);
        handicapBtn.setSelected(false);
        Main.info.getAlgorithm().refresh();
        hasPath = false;
        currentFloor = (String) Main.screenController.getData("floor");
        if (currentFloor == null)
            currentFloor = Main.info.getKioskLocation().getFloor();
        allButtons.add(Floor3);
        allButtons.add(Floor2);
        allButtons.add(Floor1);
        allButtons.add(Ground);
        allButtons.add(L1);
        allButtons.add(L2);
        pathScrollBar.setVisible(false);
        Gif.setVisible(false);
        clearBtn.setVisible(false);
        pathScrollBar.valueProperty().removeListener(pathBarScrollListener);
        updateFloorImg(currentFloor);
        Platform.runLater(() -> {
            displayAllNodes();
            changeButtonColor(currentFloorButton);
        });


            /*
                Load images into the cache with MULTITEHRADING
             */

        new Thread(() -> {
            if (!imageCache.containsKey("03_thethirdfloor.png")) {
                imageCache.put("03_thethirdfloor.png", new Image(String.valueOf(getClass().getResource("/img/03_thethirdfloor.png"))));
            }
            if (!imageCache.containsKey("02_thesecondfloor_withbookablelocations.png")) {
                imageCache.put("02_thesecondfloor_withbookablelocations.png", new Image(String.valueOf(getClass().getResource("/img/02_thesecondfloor_withbookablelocations.png"))));
            }
            if (!imageCache.containsKey("01_thefirstfloor.png")) {
                imageCache.put("01_thefirstfloor.png", new Image(String.valueOf(getClass().getResource("/img/01_thefirstfloor.png"))));
            }
            if (!imageCache.containsKey("00_thegroundfloor.png")) {
                imageCache.put("00_thegroundfloor.png", new Image(String.valueOf(getClass().getResource("/img/00_thegroundfloor.png"))));
            }
            if (!imageCache.containsKey("00_thelowerlevel1.png")) {
                imageCache.put("00_thelowerlevel1.png", new Image(String.valueOf(getClass().getResource("/img/00_thelowerlevel1.png"))));
            }
            if (!imageCache.containsKey("00_thelowerlevel2.png")) {
                imageCache.put("00_thelowerlevel2.png", new Image(String.valueOf(getClass().getResource("/img/00_thelowerlevel2.png"))));
            }
        }).start();


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
        nodes = Node.getNodesByFloor(currentFloor);
        edges = Edge.getEdgesByFloor(currentFloor);
        black = new Color(0, 0, 0, 1);
        red = new Color(1, 0, 0, 1);

        clearMap();


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
            showFoundNode(nodeCircles.get(findLocationNodeID), Color.ORANGERED);
        } else if (nodeCircles.containsKey(Main.info.getKioskLocation().getID())) {
            showFoundNode(nodeCircles.get(Main.info.getKioskLocation().getID()), Color.GREEN);
        }
    }

    /**
     * shows the node that has been found as a flashing red dot
     *
     * @param foundNode
     */
    public void showFoundNode(Circle foundNode, Color color) {
        foundNode.setRadius(6.0);
        foundNode.setFill(color);
        foundNode.toFront();
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), foundNode);
        st.setByX(1.2);
        st.setByY(1.2);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }

    public void goBtnClick(ActionEvent actionEvent) {
        makePath();
    }


    //nodesOnPathArray
    public void playBtnClick(ActionEvent actionEvent) {
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        double imgWidth = (Gif.getFitWidth() / 2);
        double imgHeight = (Gif.getFitHeight() / 2);
        double mapX = findPathImgView.getLayoutX();
        double mapY = findPathImgView.getLayoutY();
        danceBtn.setDisable(true);
        pathScrollBar.setDisable(true);
        Gif.setLayoutX(((mapX + Main.info.getKioskLocation().getX()) / mapScale) - imgWidth);
        Gif.setLayoutY(((mapY + Main.info.getKioskLocation().getY()) / mapScale) - imgHeight);
        Gif.toFront();
        Gif.setVisible(true);

        SequentialTransition animations = new SequentialTransition();

        for(int i = 0; i < nodesOnPathArray.length - 1; i++){

            /*
                TODO This doesn't need to happen in the for loop.
                 We probably just want to do this once for the first floor before we even start this loop ya know?
             */


            if(animations.getChildren().size() > 0){
                changeFloor(nodesOnPathArray[0].getFloor());
            }

            PathTransition path = new PathTransition();
            Line line = new Line();
            path.setNode(Gif);
            path.setPath(line);
            path.setDuration(Duration.millis(200));

            Node start = nodesOnPathArray[i];
            Node dest = nodesOnPathArray[i + 1];

            path.setOnFinished(event -> nodeCircles.get(start).setFill(Color.PINK));


            double startX = ((start.getX()/mapScale)- imgHeight);
            double startY = (((start.getY())/mapScale ) - imgWidth);
            double endX = (((dest.getX())/mapScale) - imgHeight);
            double endY = (((dest.getY())/mapScale) - imgWidth);

            line.setStartX(startX - 523);
            line.setStartY(startY - 225);
            line.setEndX(endX - 523);
            line.setEndY(endY - 225);

            path.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

            // change floor stuff here
            if (!start.getFloor().equals(dest.getFloor())) {
                path.setOnFinished(event -> changeFloor(dest.getFloor()));
            }

            /*if(i == (nodesOnPathArray.length -1)){
                Gif.setVisible(false);
                playBtn.setVisible(false);
                danceBtn.setDisable(false);
            }*/

            animations.getChildren().add(path);
        }

        /*
            TODO We can do something like this to get the last child of the animation sequence
                and set its "onFinishedEvent" to re-enable all the stuff. Then, right before
                we play the animation we disable the stuff we want to disable. Then we play the
                animation.
         */

        animations.getChildren().get(animations.getChildren().size() - 1).setOnFinished(event -> {
            Gif.setVisible(false);
            danceBtn.setDisable(false);
            pathScrollBar.setDisable(false);
            playBtn.setDisable(false);
        });

        playBtn.setDisable(true);

        animations.play();
    }


    public void makePath(){
        String orig_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        nodesOnPath = Main.info.getAlgorithm().findPath(orig_nodeID, dest_nodeID);
        pathScroll = new PathScroll(nodesOnPath);
        nodesOnPathArray = pathScroll.getNodesOnPath();
        Node startNode = Node.getNodeByID(searchController_origController.getNodeID());
        changeFloor(startNode.getFloor());
        pathScrollBar.setMin(0);
        pathScrollBar.setValue(0);
        pathScrollBar.setMax(nodesOnPath.size());
        pathScrollBar.valueProperty().addListener(pathBarScrollListener);
        pathScrollBar.setVisible(true);
        clearBtn.setVisible(true);
        playBtn.setVisible(true);
        generateNodesAndEdges(nodesOnPath);
        phoneNumberBtn.setDisable(false);
        danceBtn.setSelected(false);
        hasPath = true;
        PathToText pathToText = new PathToText(nodesOnPath);
        pathText.setText(pathToText.getDetailedPath());
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void clearBtnClick(ActionEvent e){
        pathScrollBar.setVisible(false);
        pathScrollBar.setDisable(false);
        Gif.setVisible(false);
        danceBtn.setDisable(false);
        clearBtn.setVisible(false);
        playBtn.setVisible(false);
        displayAllNodes();
        hasPath = false;
        nodesOnPath.clear();
        danceBtn.setSelected(false);
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
    @SuppressWarnings("Duplicates")
    private void scroll(){
        int oldPosition = pathScroll.getOldPosition();
        int newPosition = (int) pathScrollBar.getValue();
        /*Gif.setX(nodesOnPathArray[newPosition].getX());
        Gif.setY(nodesOnPathArray[newPosition].getY());*/
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
                if (forwards){
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.BLACK);
                    for(int i = 0; i < oldPosition; i++){
                        Node n = nodesOnPathArray[i];
                        if(n.getFloor().equals(currentFloor)){
                            Circle nodeCircle = nodeCircles.get(n.getID());
                            nodeCircle.setFill(red);
                            if (nodeLines.containsKey(n.getID())){
                                Line nodeLine = nodeLines.get(n.getID());
                                nodeLine.setStroke(red);
                            }
                        }
                    }
                }
                else{
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.RED);
                    for(int i = nodesOnPathArray.length - 1; i > newPosition; i--){
                        Node n = nodesOnPathArray[i];
                        if(n.getFloor().equals(currentFloor)){
                            Circle nodeCircle = nodeCircles.get(n.getID());
                            nodeCircle.setFill(black);
                            if (nodeLines.containsKey(n.getID())){
                                Line nodeLine = nodeLines.get(n.getID());
                                nodeLine.setStroke(black);
                            }
                        }
                    }
                }
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
        clearMap();
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

    public void handicapBtnClick(ActionEvent e){
        if(handicapBtn.isSelected()){
            Main.info.getAlgorithm().setHandicap(true);
        }
        else Main.info.getAlgorithm().setHandicap(false);
        if(hasPath){
            makePath();
        }
    }

    /*

        DANCE PARTY STUFF


     */
    @SuppressWarnings("Duplicates")
    public void dancePartyBtnClick(ActionEvent actionEvent) {
        clearMap();
        double mapX = findPathImgView.getLayoutX();
        double mapY = findPathImgView.getLayoutY();
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        if (danceBtn.isSelected() && hasPath) {
            pathScrollBar.setDisable(true);
            Node prev = null;
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
        } else if (danceBtn.isSelected() && !hasPath) {
            for (Node n : nodes) {
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
                    mapImgPane.getChildren().add(circle);
                    nodeCircles.put(n.getID(), circle);
                }

            }
            for (Edge e : edges) {
                if (nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode())) {
                    Color c1 = randomColorGenerator();
                    Color c2 = randomColorGenerator();
                    Line line = new Line();
                    line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
                    line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
                    line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
                    line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());
                    line.setStroke(c1);
                    line.setStrokeWidth(3.0);
                    StrokeTransition st = new StrokeTransition(Duration.millis(400), line, c1, c2);
                    st.setCycleCount(Animation.INDEFINITE);
                    st.setAutoReverse(true);
                    st.play();
                    mapImgPane.getChildren().add(line);
                }
            }
            for (Circle c : nodeCircles.values()) {
                c.toFront();
            }
        } else if (hasPath) {
            pathScrollBar.setDisable(false);
            generateNodesAndEdges(nodesOnPath);
            pathScroll.setOldPosition(0);
            scroll();
        } else {
            displayAllNodes();
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
                floorURL = "03_thethirdfloor.png";
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
        if (imageCache.containsKey(floorURL)) {
            findPathImgView.setImage(imageCache.get(floorURL));
        } else {
            Image newImage = new Image(String.valueOf(getClass().getResource("/img/" + floorURL)));
            imageCache.put(floorURL, newImage);
            findPathImgView.setImage(newImage);
        }
        findPathImgView.fitWidthProperty().bind(mapImgPane.widthProperty());
    }


    public void changeFloor(String floor) {
        changeFloor(floor, Color.BLACK);
    }

    public void changeFloor(String floor, Color c) {
        currentFloor = floor;
        clearMap();
        updateFloorImg(floor);
        if (hasPath) {
            generateNodesAndEdges(nodesOnPath, c);
        } else {
            displayAllNodes();
        }
        colorFloorsOnPath(nodesOnPath, currentFloor);
        Gif.toFront();
    }

    public void floor3BtnClick(ActionEvent actionEvent) {
        changeFloor("3");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor2BtnClick(ActionEvent actionEvent) {
        changeFloor("2");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor1BtnClick(ActionEvent actionEvent) {
        changeFloor("1");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void groundBtnClick(ActionEvent actionEvent) {
        changeFloor("G");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L1BtnClick(ActionEvent actionEvent) {
        changeFloor("L1");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L2BtnClick(ActionEvent actionEvent) {
        changeFloor("L2");
        pathScrollBar.setValue(0);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
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
        // start by setting all floors to "off"
        Floor3.setStyle("-fx-background-color: -secondary");
        Floor2.setStyle("-fx-background-color: -secondary");
        Floor1.setStyle("-fx-background-color: -secondary");
        Ground.setStyle("-fx-background-color: -secondary");
        L1.setStyle("-fx-background-color: -secondary");
        L2.setStyle("-fx-background-color: -secondary");

        // get all floors on path
        LinkedList<String> allFloors = new LinkedList<>();

        for (int i = 0; i < node_onPath.size(); i++) {
            String floor = node_onPath.get(i).getFloor();
            if (allFloors.size() < 6) {
                if (!allFloors.contains(floor)) {
                    allFloors.add(floor);
                }
            }
        }

        // color floors on path as blue
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
                case "G":
                    Ground.setStyle("-fx-background-color: -primary");
                    break;
                case "L1":
                    L1.setStyle("-fx-background-color: -primary");
                    break;
                case "L2":
                    L2.setStyle("-fx-background-color: -primary");
                    break;
            }
        }

        // set current floor to green
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
            case "G":
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

    public void searchBtnClick(ActionEvent actionEvent) {
        findLocationNodeID = autocompletesearchbarController.getNodeID();
        changeFloor(autocompletesearchbarController.getNodeFloor());
        displayAllNodes();
        searchWrapper.setVisible(false);
    }

    /**
     * helper method to clear the map image view of everything except for the map image and the gif.
     * @author Ryan LaMarche
     */
    private void clearMap() {
        Gif.toBack();
        mapImgPane.getChildren().remove(2, mapImgPane.getChildren().size());
        Gif.toFront();
    }
}


