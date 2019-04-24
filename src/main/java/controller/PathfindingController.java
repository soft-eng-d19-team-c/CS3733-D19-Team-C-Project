package controller;

import base.EnumScreenType;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.*;
import net.kurobako.gesturefx.GesturePane;
import model.*;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class PathfindingController extends Controller implements Initializable {
    public AutocompleteSearchBarController autocompletesearchbarController;
    @FXML private AnchorPane pathfindingScreen;
    @FXML private AnchorPane labelAnchor;
    @FXML private ToggleButton danceBtn;
    @FXML private ToggleButton handicapBtn;
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

    //Labels for the floors along the scroll-bar

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
    int counter =0; //current pane


    // List of Floor Labels for the scrollbar
    LinkedList<ScrollBarLabel> allLabels = new LinkedList<>();


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.MAP);
//         pathText.setText(null);
        danceBtn.setSelected(false);
        phoneNumberInput.setText(null);
        if (Main.screenController.getData("showSearch") != null && (Boolean) Main.screenController.getData("showSearch")) {
            searchWrapper.setVisible(true);
            navController.setActiveTab(NavTypes.FINDROOM);
        }
        else {
            searchWrapper.setVisible(false);
            navController.setActiveTab(NavTypes.MAP);
        }
        findLocationNodeID = null;
        searchController_origController.refresh();
        searchController_destController.refresh();
        searchController_destController.setLocation((String) null);
        autocompletesearchbarController.refresh();
        autocompletesearchbarController.setLocation((String) null);
        handicapBtn.setSelected(false);
        Main.info.getAlgorithm().refresh();
        currentFloor = (String) Main.screenController.getData("floor");
        if (currentFloor == null)
            currentFloor = Main.info.getKioskLocation().getFloor();
        allButtons.add(Floor4);
        allButtons.add(Floor3);
        allButtons.add(Floor2);
        allButtons.add(Floor1);
        allButtons.add(Ground);
        allButtons.add(L1);
        allButtons.add(L2);
        pathScrollBar.setVisible(false);
        pathScrollBar.valueProperty().removeListener(pathBarScrollListener);
        updateFloorImg(currentFloor);
        nodePopUpPane.setVisible(false);
        clearBtn.setVisible(false);
        removeAllLabels();
        Platform.runLater(() -> {
            displayAllNodes();
            changeButtonColor(currentFloorButton);
            nodesOnPath = new LinkedList<>();
            setHasPath(false);
        });
            /*
                Load images into the cache with MULTITEHRADING
             */

        new Thread(() -> {
            if (!imageCache.containsKey("3_NoIcons.png")) {
                imageCache.put("3_NoIcons.png", new Image(String.valueOf(getClass().getResource("/img/3_NoIcons.png"))));
            }
            if (!imageCache.containsKey("02_thesecondfloor_withbookablelocations.png")) {
                imageCache.put("02_thesecondfloor_withbookablelocations.png", new Image(String.valueOf(getClass().getResource("/img/02_thesecondfloor_withbookablelocations.png"))));
            }
            if (!imageCache.containsKey("02_thesecondfloor.png")) {
                imageCache.put("02_thesecondfloor.png", new Image(String.valueOf(getClass().getResource("/img/02_thesecondfloor.png"))));
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

        labelAnchor.setMouseTransparent(true);
        clearBtn.setVisible(false);


        if (addText != null){
            addText.getPanes().remove(0, allPanes.size());
            allPanes.clear();
        }

    }

    /**
     * adds and removes the event filter on the modes depending on if there is a path on the screen
     * @param b true or false
     * @author Fay Whittall
     */
    private void setHasPath(boolean b){
        if(b) {
            hasPath = true;
        }
        else {
            hasPath = false;
        }
        setClickPathFind();
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

    /**
     * shows the node that has been found as a flashing red dot
     * @param foundNode
     */
    public void showFoundNode(Circle foundNode, Color color){
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
    /*


        PATHFINDING STUFF


     */
    public void goBtnClick(ActionEvent actionEvent) {
        makePath();
    }

    public void makePath(){
        //addText.getPanes().removeAll(floor4, floor3, floor2, floor1, ground, l1, l2);
        if (addText != null)
            if (allPanes.size() != 0)
                addText.getPanes().remove(0, allPanes.size());

        String orig_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();

        if (orig_nodeID == null || dest_nodeID == null)
            return;

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

        generateNodesAndEdges(nodesOnPath);
        phoneNumberBtn.setDisable(false);
        danceBtn.setSelected(false);
        setHasPath(true);
        PathToText pathToText = new PathToText(nodesOnPath);

        TextInfo pathsByFloor = pathToText.getDetailedPath();
        // System.out.println(pathsByFloor.getFloorStrings().size());
        // System.out.println(pathsByFloor.getNumberOfAccordions());
        if (pathsByFloor.getFloorStrings().size() == pathsByFloor.getNumberOfAccordions()) {

            for (int i = 0; i < pathsByFloor.getNumberOfAccordions(); i++) {
                TitledPane tp = new TitledPane();
                allPanes.add(tp);
                Label text = new Label("Floor " + pathsByFloor.getCurrentFloors().get(i));
                addText.getPanes().add(tp);
                VBox content = new VBox();
                text.setText(pathsByFloor.getFloorStrings().get(i));
                tp.setText("Floor " + pathsByFloor.getCurrentFloors().get(i));
                content.getChildren().add(text);
                tp.setContent(content);

            }
        } else {
            System.out.println("Error");
        }

        colorFloorsOnPath(nodesOnPath, currentFloor);
        //if(allPanes != null)
        addText.setExpandedPane(allPanes.getFirst());
        createScrollBarLabels();
    }

    public void clearBtnClick(ActionEvent e){
        pathScrollBar.setVisible(false);
        pathScrollBar.setDisable(false);
        clearBtn.setVisible(false);
        displayAllNodes();
        setHasPath(false);
        nodesOnPath.clear();
        danceBtn.setSelected(false);
        colorFloorsOnPath(nodesOnPath, currentFloor);
        mapImgPane.reset();
        removeAllLabels();
        addText.getPanes().remove(0, allPanes.size());
        allPanes.clear();
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
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        double x = 0;
        double y = 0;
        boolean forwards = false;
        if (oldPosition < newPosition)
            forwards = true;

        Node[] nodesToRedraw = pathScroll.getNodesInRange(newPosition);
        for(Node n : nodesToRedraw) {
            x = (double) n.getX() / mapScale;
            y = (double) n.getY() / mapScale;
            mapImgPane.animate(Duration.millis(400)).centreOn(new Point2D(x, y));
        }
        //hard-coded bug fix - make the whole path red if it is at the end of the scroll bar
        if(newPosition == (int) pathScrollBar.getMax()){
            Node n = nodesOnPathArray[newPosition -1];
            if(nodeCircles.containsKey(n.getID())){
                Circle nodeCircle = nodeCircles.get(n.getID());
                nodeCircle.setFill(red);
            }
        }
        else{
            //draw the nodes again red if moving forward, draw them again black if moving backward
            if(!(nodesOnPathArray[newPosition].getFloor().equals(currentFloor))){
                if (forwards) {
                    counter++;
                    if (counter >= allPanes.size()){
                        counter = allPanes.size()-1;
                    }
                    if (allPanes.size() != 0 || allPanes.size() != -1)
                        addText.setExpandedPane(allPanes.get(counter));
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.BLACK);
                }
                else{
                    counter--;
                    if (counter < 0)
                        counter = 0;
                    if (allPanes.size() != 0)
                        addText.setExpandedPane(allPanes.get(counter));
                    changeFloor(nodesOnPathArray[newPosition].getFloor(), Color.RED);
                }
            }
            for(Node n: nodesToRedraw){
                if(nodeCircles.containsKey(n.getID())){
                    Circle nodeCircle = nodeCircles.get(n.getID());
                    if (nodeCircle.getFill().equals(black)){
                        nodeCircle.setFill(red);
                    } else {
                        nodeCircle.setFill(black);
                    }
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


    /**
     * Kyle
     */
    private void removeAllLabels(){
        //labelAnchor.getChildren().remove(0, allLabels.size()); // remove it from the parent
        for (int i = 0; i < allLabels.size(); i++) {
            allLabels.get(i).getLabel().setVisible(false); // Hide everything
        }
        allLabels.clear();
    }

    /**
     * Kyle
     */
    private void createScrollBarLabels(){
        //First Remove ALL of the labels that exist from previous path finds
        if (allLabels.size() > 0){
            removeAllLabels();
        }
        // now add New ones
        ScrollBarLabel firstL = new ScrollBarLabel(0, nodesOnPathArray[0].getFloor());
        allLabels.add(firstL);
        firstL.getLabel().setVisible(false);
        for (int i = 1; i < nodesOnPath.size() - 1; i++) {
            if (!nodesOnPathArray[i].getFloor().equals(nodesOnPathArray[i - 1].getFloor())) {
                double scrollWidth = pathScrollBar.getLayoutBounds().getMaxX() - pathScrollBar.getLayoutBounds().getMinX();
                ScrollBarLabel newL = new ScrollBarLabel(i * ( (int)scrollWidth / nodesOnPath.size()), nodesOnPathArray[i].getFloor());
                allLabels.add(newL);
                newL.getLabel().setVisible(false); //Hide the created Labels
            }
        }
        for (int i = 0; i < allLabels.size(); i++){
            allLabels.get(i).getLabel().setVisible(true); //Show the created Labels
            // Label Size
            allLabels.get(i).getLabel().setFont(Font.font("Arial Black", 30.0));
            // White Text
            allLabels.get(i).getLabel().setTextFill(Color.WHITE);
            // Add the label to the path finding screen anchor page
            labelAnchor.getChildren().add(allLabels.get(i).getLabel());
            // Bring it to the front
            allLabels.get(i).getLabel().toFront();
            // Relocate it to the correct position
            allLabels.get(i).getLabel().relocate(allLabels.get(i).getLocation() + 10, 570);
            System.out.println(allLabels.get(i).getLocation());
        }
    }



    /**
     * Move the floor labels
     * @author: Kyle Heavey unless it doesn't work in which case I didn't do it
     */

    /*
    private void moveFloorLabels(){
        System.out.println("I'm moving Labels Here!");
        // The scroll bar spans from pixel 10 to pixel 530
        float pixelsPerStep = 500 / (float)nodesOnPath.size();  // This is how many steps we need to move the label by
        System.out.println(nodesOnPath.size());
        System.out.println(pixelsPerStep);

        moveLabels(0, nodesOnPathArray[0]); // Add a label for the first node
                                                    // depending on which step it is
        for (int i = 1; i < nodesOnPath.size() - 1; i++){   //increment through each node in the list
                                                            //starting at 1 so I can check the "first" node
                                                            //and compare it to the actual first node and the
                                                            //second to last node and compare it to the first
            String floor = nodesOnPathArray[i].getFloor();
            Label l = new Label();
            if (!nodesOnPathArray[i].getFloor().equals(nodesOnPathArray[i-1].getFloor())){    // if the floor of the current node
                                                                                        // is NOT the same as the last floor
                // Add a new label
                System.out.println("Floor Change Here. There should only be 2 floor changes");
                moveLabels( (i * pixelsPerStep), nodesOnPathArray[i]);
            }
        }
    }
    */

    /** Helper for the moveFloorLabels function
     * @Param: a distance to move the label and a node that indicate the floor change
     * @Author: Kyle Heavey
     */
    /*
    void moveLabels(float distance, Node n){
        float relocateDist = distance + 20; // offset
        if (n.getFloor().equals("G")){
            groundLabel.relocate(relocateDist, 525);
        }
        if (n.getFloor().equals("L1")){
            l1Label.relocate(relocateDist, 525);
        }
        if (n.getFloor().equals("L2")){
            l2Label.relocate(relocateDist,525);
        }
        if (n.getFloor().equals("1")){
            flr1Label.relocate(relocateDist, 525);
        }
        if (n.getFloor().equals("2")){
            flr2Label.relocate(distance, 525);
        }
        if (n.getFloor().equals("3")){
            flr3Label.relocate(relocateDist, 525);
        }
    }
    */

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
        double x = 0;
        double y = 0;
        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
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
                    zoomGroup.getChildren().add(line);
                    nodeLines.put(n.getID(), line);
                }
                zoomGroup.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
                x = (double) n.getX() / mapScale;
                y = (double) n.getY() / mapScale;
                mapImgPane.animate(Duration.millis(400)).zoomTo(2.5, new Point2D(x, y));
            } else {
                prev = null;
            }
        }
    }

    public void sendTextClick(ActionEvent actionEvent) {
        String phoneNumber = phoneNumberInput.getText();
        PathToText pathToText = new PathToText(nodesOnPath);
        TextInfo pathsByFloor = pathToText.getDetailedPath();
        StringBuilder path = new StringBuilder();
        for(int i=0; i < pathsByFloor.getNumberOfAccordions(); i++){
            path.append(pathsByFloor.getFloorStrings().get(i));
        }
        pathToText.SmsSender(path.toString(), new PhoneNumber("+1" + phoneNumber));
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
        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
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
                        zoomGroup.getChildren().add(line);
                    }
                    zoomGroup.getChildren().add(circle);
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
                    zoomGroup.getChildren().add(circle);
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
                    zoomGroup.getChildren().add(line);
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
        if (floor.equals("G")){
            floor = "G";
        }
        String floorURL;
        switch (floor) {
            case "4":
                floorURL = "02_thesecondfloor_withbookablelocations.png";
                currentFloorButton = Floor4;
                break;
            case "3":
                floorURL = "03_thethirdfloor.png";
                currentFloorButton = Floor3;
                break;
            case "2":
                floorURL = "02_thesecondfloor.png";
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
            System.out.println(floorURL);
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
        nodePopUpPane.toBack();
        currentFloor = floor;
        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
        updateFloorImg(floor);
//        double x = 0;
//        double y = 0;
//        double mapScale = findPathImgView.getImage().getWidth() / findPathImgView.getFitWidth();
        mapImgPane.reset();
        if (hasPath) {
            generateNodesAndEdges(nodesOnPath, c);
        } else {
            displayAllNodes();
        }
        colorFloorsOnPath(nodesOnPath, currentFloor);
        setClickPathFind();
    }
    public void floor4BtnClick(ActionEvent actionEvent) {
        changeFloor("4");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor3BtnClick(ActionEvent actionEvent) {
        changeFloor("3");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor2BtnClick(ActionEvent actionEvent) {
        changeFloor("2");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor1BtnClick(ActionEvent actionEvent) {
        changeFloor("1");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void groundBtnClick(ActionEvent actionEvent) {
        changeFloor("G");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L1BtnClick(ActionEvent actionEvent) {
        changeFloor("L1");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L2BtnClick(ActionEvent actionEvent) {
        changeFloor("L2");
        if (addText != null)
            if (addText.getExpandedPane() != null)
                addText.getExpandedPane().setExpanded(false);
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void changeButtonColor(Button button){

        switch (currentFloor){
            case "4": currentFloorButton = Floor4;
                Floor4.setStyle("-fx-background-color: -success");
                Floor3.setStyle("-fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "3": currentFloorButton = Floor3;
                Floor4.setStyle(" -fx-background-color: -secondary");
                Floor3.setStyle("-fx-background-color: -success");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "2": currentFloorButton = Floor2;
                Floor4.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "1": currentFloorButton = Floor1;
                Floor4.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "G": currentFloorButton = Ground;
                Floor4.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "L1": currentFloorButton = L1;
                Floor4.setStyle(" -fx-background-color: -secondary");
                L1.setStyle("-fx-background-color: -success");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "L2": currentFloorButton = L2;
                Floor4.setStyle(" -fx-background-color: -secondary");
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
            // start by setting all floors to "off"
            Floor4.setStyle("-fx-background-color: -secondary");
            Floor3.setStyle("-fx-background-color: -secondary");
            Floor2.setStyle("-fx-background-color: -secondary");
            Floor1.setStyle("-fx-background-color: -secondary");
            Ground.setStyle("-fx-background-color: -secondary");
            L1.setStyle("-fx-background-color: -secondary");
            L2.setStyle("-fx-background-color: -secondary");

        /*
        groundLabel.setVisible(false);
        l1Label.setVisible(false);
        l2Label.setVisible(false);
        flr3Label.setVisible(false);
        flr2Label.setVisible(false);
        flr1Label.setVisible(false);
        */

            // get all floors on path
//        LinkedList<String> allFloors = new LinkedList<>();

            for (int i = 0; i < node_onPath.size(); i++) {
                String floor = node_onPath.get(i).getFloor();
                if (allFloors.size() < 7) {
                    if (!allFloors.contains(floor)) {
                        allFloors.add(floor);
                    }
                }
            }

            for (int i = 0; i < allFloors.size(); i++) {
                String floor = allFloors.get(i);
                switch (floor) {
                    case "4":
                        Floor4.setStyle("-fx-background-color: -primary");
                        break;
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

                if ((!(floor.equals(currentFloor))) && (!(allFloors.contains(floor)))) {
                    switch (floor) {
                        case "4":
                            Floor4.setStyle("-fx-background-color: -secondary");
                            break;
                        case "3":
                            Floor3.setStyle("-fx-background-color: -secondary");
                            break;
                        case "2":
                            Floor2.setStyle("-fx-background-color: -secondary");
                            break;
                        case "1":
                            Floor1.setStyle("-fx-background-color: -secondary");
                            break;
                        case "G":
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
                case "4":
                    Floor4.setStyle("-fx-background-color: -success");
                    break;
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
    }

    public void searchBtnClick(ActionEvent actionEvent) {
        findLocationNodeID = autocompletesearchbarController.getNodeID();
        changeFloor(autocompletesearchbarController.getNodeFloor());
        displayAllNodes();
        searchWrapper.setVisible(false);
    }
    /*

    CLICK TO FIND PATH


     */

    /**
     * Adds and removes the event filter
     * @author Fay Whittall
     */
    public void setClickPathFind(){
        for(Node n : nodes){
            Circle c = nodeCircles.get(n.getID());
            if(hasPath){
                c.removeEventFilter(MouseEvent.MOUSE_PRESSED, nodeClickPathfindHandler);
            }
            else {
                c.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeClickPathfindHandler);
                c.setCursor(Cursor.HAND);
            }
        }
    }

    /**
     * Allows for pathfinding just by clicking on a node
     * @author Fay Whittall
     */
    EventHandler nodeClickPathfindHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getTarget();
                Node n = (Node) circle.getProperties().get("node");
                if(n.getX() < 2500 && n.getY() < 1700){
                    nodePopUpPane.relocate(me.getX() + 490, me.getY() + 50);
                }
                else if(n.getX() >= 2500 && n.getY() < 1700){
                    nodePopUpPane.relocate(me.getX() - 438 + 490, me.getY() + 50);
                }
                else if(n.getX() < 2500 && n.getY() >= 1700){
                    nodePopUpPane.relocate(me.getX() + 490, me.getY() - 115 + 50);
                }
                else{
                    nodePopUpPane.relocate(me.getX() - 438 + 490, me.getY() - 115 + 50);
                }
                nodePopUpPane.setVisible(true);
                popUpLongName.setText(n.getLongName());
                setStartBtn.setOnAction((event) -> {
                    searchController_origController.setLocation(n);
                });
                goFromNodeBtn.setOnAction((event) -> {
                    searchController_destController.setLocation(n);
                    makePath();
                });
                mapImgPane.addEventFilter(MouseEvent.MOUSE_PRESSED, nodePopUpRemoveHandler);
                findPathBar.addEventFilter(MouseEvent.MOUSE_PRESSED, nodePopUpRemoveHandler);
                setStartBtn.removeEventFilter(MouseEvent.MOUSE_PRESSED, nodePopUpRemoveHandler);
                goFromNodeBtn.removeEventFilter(MouseEvent.MOUSE_PRESSED, nodePopUpRemoveHandler);
                nodePopUpPane.toFront();
            }
        }
    };

    EventHandler nodePopUpRemoveHandler = new EventHandler<MouseEvent>(){
        public void handle(javafx.scene.input.MouseEvent me){
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                nodePopUpPane.setVisible(false);
                pathfindingScreen.removeEventFilter(MouseEvent.MOUSE_PRESSED, nodePopUpRemoveHandler);
            }
        }
    };

    public void robotButtonClick(ActionEvent actionEvent){
        Main.screenController.setScreen(EnumScreenType.ROBOTSCREEN);
    }

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


