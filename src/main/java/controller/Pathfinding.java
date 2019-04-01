package controller;

import base.EnumScreenType;
import base.Main;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import model.DataTable;
import model.Edge;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    @FXML
    private AnchorPane findpathview;
    @FXML
    private TextField origin;
    @FXML
    private TextField destination;
    @FXML
    private Button findpathbtn;
    @FXML
    private AutocompleteSearchBar search;
    @FXML
    private AutocompleteSearchBar search_0;

    private DataTable dt;

    private LinkedList<Node> nodeList;

    private HashMap<String, Edge> edgeList;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
/*        mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+ Main.screenController.getData("floor")+"_NoIcons.png"))));
        Platform.runLater(() -> {
            dt = new DataTable();
            nodeList = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
//            edgeList = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
            drawNodes();
        });
    }

    private void drawNodes() {
        Color c = new Color(1,1,1,1.0);
        findpathview.setBackground(new Background(new BackgroundFill(c, null, null)));
        dancePartyBtn.setTextFill(new Color(0, 0, 0, 1.0));

        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        mapImg.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        double mapX = mapImg.getLayoutX();
        double mapY = mapImg.getLayoutY();
        for (Node n : nodeList){
            Circle circle = new Circle();
            circle.setCenterX(mapX + n.getX()/4.0);
            circle.setCenterY(mapY + n.getY()/4.0);
            circle.setRadius(3.0);
            imInPane.getChildren().add(circle);
        }

        for (Edge e : edgeList.values()){
            Node startNode = dt.getDataById(e.getStartNode());
            Node endNode = dt.getDataById(e.getEndNode());

            if (startNode != null && endNode != null && startNode.getFloor().equals(endNode.getFloor())){

                Line line = new Line();

                line.setStartX(mapX + (startNode.getX() / 4));
                line.setStartY(mapY + (startNode.getY()) / 4);
                line.setEndX(mapX + (endNode.getX()) / 4);
                line.setEndY(mapY + (endNode.getY()) / 4);

                line.setStroke(new Color(0,0,0,1));

                imInPane.getChildren().add(line);
            }
        }
    }


*/
}
