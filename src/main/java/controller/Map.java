package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.DataTable;
import model.Node;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Map implements Initializable {
    @FXML
    private ImageView mapImg;
    @FXML
    private Pane imInPane;

    private DataTable dt;

    private String floorPlan;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+this.floorPlan+"_NoIcons.png"))));

            dt = new DataTable();
            LinkedList<Node> list = dt.getProjectCNodesByFloor(this.floorPlan);

            for (Node n : list){
                System.out.println(n);
                Circle circle = new Circle();
                circle.setCenterX(n.getX()/4.0);
                circle.setCenterY(n.getY()/4.0);
//                circle.setRadius(2.5);
                circle.setRadius(1.0 + (Math.random() * 5.0));
                double r = Math.random();
                double g = Math.random();
                double b = Math.random();
                Color color = new Color(r, g, b, 1.0);
                circle.setFill(color);
                imInPane.getChildren().add(circle);
            }


        });
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }
}
