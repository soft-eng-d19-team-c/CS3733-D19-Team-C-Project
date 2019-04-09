package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.twilio.type.PhoneNumber;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
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
import javafx.util.Duration;
import model.*;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class PathfindingController extends Controller implements Initializable {
    @FXML private ToggleButton dancebtn;
    @FXML private ImageView findpathmap;
    @FXML private AnchorPane findpathview;
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



    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Node> node_onPath;
    private HashMap<String, Circle> nodeCircles;

    private LinkedList<Button> allButtons = new LinkedList<Button>();

    private Color black;
    private Color somecolor;

    private String findLocationNodeID;
    private String currentFloor;
    private boolean hasPath;
    private Button currentButton;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hasPath = false;
        currentFloor = (String) Main.screenController.getData("floor");
        allButtons.add(Floor3);
        allButtons.add(Floor2);
        allButtons.add(Floor1);
        allButtons.add(Ground);
        allButtons.add(L1);
        allButtons.add(L2);
        updateFloorImg(currentFloor);
        Platform.runLater(() -> {
            displayAllNodes();
            changeColor2(currentButton);
        });
    }

    private void generateNodes(LinkedList<Node> nodes) {
        String prev = null;
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        for (Node n : nodes) {
            if (n.getFloor().equals(currentFloor)) { // checks if node is on the current floor
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / mapScale);
                circle.setCenterY(mapY + n.getY() / mapScale);
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
                    mapImgPane.getChildren().add(line);
                }
                mapImgPane.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void dancePartyNode(LinkedList<Node> nodes) {
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        if (dancebtn.isSelected()) {
            String prev = null;
            double mapX = findpathmap.getLayoutX();
            double mapY = findpathmap.getLayoutY();
            double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
            for (Node n : nodes) {
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
                if (prev != null) {
                    drawDancingline(prev, n.getID());
                }
                mapImgPane.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
            }
            for (Circle c : nodeCircles.values()) {
                c.toFront();
            }
        } else {
            generateNodes(nodes);
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
        mapImgPane.getChildren().add(line);
    }

    private void drawNodes(LinkedList<Node> nodes_p, LinkedList<Edge> edges_p, Color c) {
        Color w = new Color(1, 1, 1, 1);
        findpathview.setBackground(new Background(new BackgroundFill(w, null, null)));
        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        findpathmap.setEffect(reset);
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();

        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];

        for (Node n : nodes_p) {
            generateNode(n, orgSceneX, orgSceneY, mapX, mapY, c);
        }
        for (Edge e : edges_p) {
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
        mapImgPane.getChildren().add(line);
        return line;
    }


    private void generateNode(Node n, double[] orgSceneX, double[] orgSceneY, double mapX, double mapY, Color c) {
        orgSceneX[0] = -1;
        orgSceneY[0] = -1;
        Circle circle = new Circle();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        circle.setCenterX(mapX + n.getX() / mapScale);
        circle.setCenterY(mapY + n.getY() / mapScale);
        circle.setRadius(3.0);
        circle.setFill(c);
        mapImgPane.getChildren().add(circle);
        nodeCircles.put(n.getID(), circle);
    }

    public void gobtnclick(ActionEvent actionEvent) {
        String orgi_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        node_onPath = Main.info.getAlgorithm().findPath(orgi_nodeID, dest_nodeID);
        somecolor = new Color(0, 1, 1, 1);
//        drawNodes(node_onPath, somecolor);
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        Node startNode = Node.getNodeByID(searchController_origController.getNodeID());
        currentFloor = startNode.getFloor();
        updateFloorImg(startNode.getFloor());
        generateNodes(node_onPath);
        Node last = Node.getNodeByID(searchController_destController.getNodeID());
        node_onPath.add(last);
        System.out.println(node_onPath);
        phoneNumberBtn.setDisable(false);
        dancebtn.setVisible(true);
        hasPath = true;
    }

    public void sendTextClick(ActionEvent actionEvent) {
        String phoneNumber = phoneNumberInput.getText();
        PathToText pathToText = new PathToText(node_onPath);
        String path = pathToText.getDetailedPath();
        pathToText.SmsSender(path, new PhoneNumber("+1" + phoneNumber));

    }

    private Color randomColorGenerator() {
        Random rand = new Random();
        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();
        Color color = new Color(r, g, b, 1);
        return color;
    }

    public void Dancebtnclick(ActionEvent actionEvent) {
        dancePartyNode(node_onPath);
    }

    public void updateFloorImg(String floor) {
        String floorURL;
        switch (floor) {
            case "3":
                floorURL = "3_NoIcons.png";
                break;
            case "2":
                floorURL = "2_NoIcons.png";
                break;
            case "1":
                floorURL = "1_NoIcons.png";
                break;
            case "G":
                floorURL = "00_thegroundfloor.png";
                break;
            case "L1":
                floorURL = "L1_NoIcons.png";
                break;
            case "L2":
                floorURL = "L2_NoIcons.png";
                break;
            default:
                System.out.println("Error in PathfindingController.updateFloorImg invalid floor");
                floorURL = "01_thefirstfloor.png";
        }


        findpathmap.setImage(new Image(String.valueOf(getClass().getResource("/img/" + floorURL))));
        findpathmap.fitWidthProperty().bind(mapImgPane.widthProperty());
    }


    public void displayAllNodes() {
        searchController_destController.refresh();
        searchController_origController.refresh();

        findLocationNodeID = (String) Main.screenController.getData("nodeID");

        nodeCircles = new HashMap<>();
        nodes = Node.getNodesByFloor(currentFloor);
        edges = Edge.getEdgesByFloor(currentFloor);
        black = new Color(0, 0, 0, 1);
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
    }

    public void changeFloor(String floor) {
        currentFloor = floor;
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        updateFloorImg(floor);
        if (hasPath) {
            generateNodes(node_onPath);
        } else {
            displayAllNodes();
        }
    }

    public void floor3BtnClick(ActionEvent actionEvent) {
        changeFloor("3");
        changeColor2(Floor3);
    }

    public void floor2BtnClick(ActionEvent actionEvent) {
        changeFloor("2");
        changeColor2(Floor2);
    }

    public void floor1BtnClick(ActionEvent actionEvent) {
        changeFloor("1");
        changeColor2(Floor1);
    }

    public void groundBtnClick(ActionEvent actionEvent) {
        changeFloor("G");
        changeColor2(Ground);
    }

    public void L1BtnClick(ActionEvent actionEvent) {
        changeFloor("L1");
        changeColor2(L1);
    }

    public void L2BtnClick(ActionEvent actionEvent) {
        changeFloor("L2");
        changeColor2(L2);
    }

    public void changeColor(Button button){
        for(int i =1; i <= allButtons.size(); i++ ){
            if(!(allButtons.get(i) == button)){
               // System.out.println("s");
                button.setOnAction((ActionEvent e) -> {
                    button.setStyle("-fx-background-color:-secondary");
                });
            }else{
                button.setOnAction((ActionEvent e) -> {
                  //  System.out.println("f");
                    button.setStyle(" -fx-background-color: -primary");
                });
            }
        }
    }

    public void changeColor2(Button button){
        switch (currentFloor){
            case "3": currentButton = Floor3;
                Floor3.setStyle(" -fx-background-color: -primary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "2": currentButton = Floor2;
                Floor2.setStyle(" -fx-background-color: -primary");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "1": currentButton = Floor1;
                Floor1.setStyle(" -fx-background-color: -primary");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "Ground": currentButton = Ground;
                Ground.setStyle(" -fx-background-color: -primary");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");
                break;
            case "L1": currentButton = L1;
                L1.setStyle(" -fx-background-color: -primary");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L2.setStyle(" -fx-background-color: -secondary");

                break;
            case "L2": currentButton = L2;
                L2.setStyle(" -fx-background-color: -primary");
                Floor3.setStyle(" -fx-background-color: -secondary");
                Floor2.setStyle(" -fx-background-color: -secondary");
                Floor1.setStyle(" -fx-background-color: -secondary");
                Ground.setStyle(" -fx-background-color: -secondary");
                L1.setStyle(" -fx-background-color: -secondary");

                break;
        }
    }
}


