package controller;

import base.Main;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.twilio.type.PhoneNumber;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

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
    @FXML private Group zoomGroup;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;
    @FXML private NavController navController;
    @FXML private JFXTextArea phoneNumberInput;
    @FXML private JFXButton phoneNumberBtn;
    @FXML private JFXButton Floor3;
    @FXML private JFXButton Floor2;
    @FXML private JFXButton Floor1;
    @FXML private JFXButton Ground;
    @FXML private JFXButton L1;
    @FXML private JFXButton L2;
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
    private HashMap<String, Image> imageCache = new HashMap<>();
    private static final int MIN_PIXELS = 10;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.MAP);
        pathText.setText(null);
        searchController_destController.refresh();
        searchController_origController.refresh();
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
            if (!imageCache.containsKey("3_NoIcons.png")) {
                imageCache.put("3_NoIcons.png", new Image(String.valueOf(getClass().getResource("/img/3_NoIcons.png"))));
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
        findLocationNodeID = (String) Main.screenController.getData("nodeID");

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
            circle.setRadius(3.0);
            circle.setFill(black);
            zoomGroup.getChildren().add(circle);
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

        if (imageCache.containsKey(floorURL)) {
            setImageI(imageCache.get(floorURL));
        } else {
            Image newImage = new Image(String.valueOf(getClass().getResource("/img/" + floorURL)));
            imageCache.put(floorURL, newImage);
            setImageI(newImage);
        }
        findPathImgView.fitWidthProperty().bind(mapImgPane.widthProperty());
    }

    private void setImageI(Image image) {
        findPathImgView.setImage(image);

        //zoom
        double width = findPathImgView.getImage().getWidth();
        double height = findPathImgView.getImage().getHeight();

        findPathImgView.setPreserveRatio(true);
        reset(findPathImgView, width, height);

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        zoomGroup.setOnMousePressed(e -> {

            Point2D mousePress = imageViewToImage(findPathImgView, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        zoomGroup.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(findPathImgView, new Point2D(e.getX(), e.getY()));
            shift(findPathImgView, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(findPathImgView, new Point2D(e.getX(), e.getY())));
        });

        zoomGroup.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = findPathImgView.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(findPathImgView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            findPathImgView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        zoomGroup.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(findPathImgView, width, height);
            }
        });

        findPathImgView.setPreserveRatio(true);
        //mapImgPane.setCenter(findpathmap);

        findPathImgView.fitWidthProperty().bind(mapImgPane.widthProperty());
        findPathImgView.fitHeightProperty().bind(mapImgPane.heightProperty());
    }

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
// the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    private ImageView setImageView(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        double w;
        double h;

        double ratioX = imageView.getFitWidth() / imageView.getImage().getWidth();
        double ratioY = imageView.getFitHeight() / imageView.getImage().getHeight();

        double reducCoeff;
        if(ratioX >= ratioY) {
            reducCoeff = ratioY;
        } else {
            reducCoeff = ratioX;
        }

        w = imageView.getImage().getWidth() * reducCoeff;
        h = imageView.getImage().getHeight() * reducCoeff;

        imageView.setX((imageView.getFitWidth() - w) / 2);
        imageView.setY((imageView.getFitHeight() - h) / 2);

        return imageView;
    }

    public void changeFloor(String floor) {
        changeFloor(floor, Color.BLACK);
    }

    public void changeFloor(String floor, Color c) {
        currentFloor = floor;
        zoomGroup.getChildren().remove(1, zoomGroup.getChildren().size());
        updateFloorImg(floor);
        if (hasPath) {
            generateNodesAndEdges(nodesOnPath, c);
        } else {
            displayAllNodes();
        }
        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor3BtnClick(ActionEvent actionEvent) {
        changeFloor("3");
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor2BtnClick(ActionEvent actionEvent) {
        changeFloor("2");
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void floor1BtnClick(ActionEvent actionEvent) {
        changeFloor("1");
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void groundBtnClick(ActionEvent actionEvent) {
        changeFloor("G");
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L1BtnClick(ActionEvent actionEvent) {
        changeFloor("L1");
//        colorFloorsOnPath(nodesOnPath, currentFloor);
    }

    public void L2BtnClick(ActionEvent actionEvent) {
        changeFloor("L2");
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


