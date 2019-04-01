package controller;

import base.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Window;
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
    private HashMap<String, Edge> edge_onPath;
    private HashMap<String, Circle> nodeCircles;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findpathmap.setImage(new Image(String.valueOf(getClass().getResource("/img/L1_NoIcons.png"))));
        Platform.runLater(() -> {
            dt = new DataTable();
            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("L1"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("L1"));
 //           drawNodes();
        });
    }

/*    private void drawNodes() {
        Color c = new Color(1, 1, 1, 1.0);
        findpathview.setBackground(new Background(new BackgroundFill(c, null, null)));
        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        findpathmap.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();
        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];
        for (Node n : nodes) {
            orgSceneX[0] = -1;
            orgSceneY[0] = -1;
            Circle circle = new Circle();
            double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
            circle.setCenterX(mapX + n.getX() / mapScale);
            circle.setCenterY(mapY + n.getY() / mapScale);
            circle.setRadius(3.0);
            circle.setCursor(Cursor.HAND);
            circle.getProperties().put("node", n);
            circle.setOnMouseDragged((MouseEvent me) -> {
                if (orgSceneX[0] == -1) {
                    orgSceneX[0] = me.getSceneX();
                }
                if (orgSceneY[0] == -1) {
                    orgSceneY[0] = me.getSceneY();
                }
                circle.toFront();
                double offsetX = me.getSceneX() - orgSceneX[0];
                double offsetY = me.getSceneY() - orgSceneY[0];
                circle.setCenterX(circle.getCenterX() + offsetX);
                circle.setCenterY(circle.getCenterY() + offsetY);
                orgSceneX[0] = me.getSceneX();
                orgSceneY[0] = me.getSceneY();
            });
            circle.setOnMouseReleased((MouseEvent me) -> {
                orgSceneX[0] = -1;
                orgSceneY[0] = -1;
            });
            imInPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
        }
    }*/
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
        System.out.println(node_onPath);
    }
}


