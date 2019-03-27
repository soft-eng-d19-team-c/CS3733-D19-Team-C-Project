package controller;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.DataTable;
import model.Node;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Map implements Initializable {
    @FXML
    private ImageView mapImg;
    @FXML
    private Pane imInPane;
    @FXML
    private ToggleButton dancePartyBtn;

    private DataTable dt;

    private String floorPlan;

    private LinkedList<Node> list;

    public void backBtnClick(ActionEvent e) {
        try {
            // try to load the FXML file and send the data to the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/emptyTable.fxml"));
            // try to change scene content
            Parent newRoot = loader.load();
            imInPane.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void danceParty(ActionEvent e) {
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        if (dancePartyBtn.isSelected()) {
            double mapX = mapImg.getLayoutX();
            double mapY = mapImg.getLayoutY();
            for (Node n : list) {
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / 4.0);
                circle.setCenterY(mapY + n.getY() / 4.0);
                circle.setRadius(1.0 + (Math.random() * 5.0));
                double r = Math.random();
                double g = Math.random();
                double b = Math.random();
                Color color = new Color(r, g, b, 1.0);
                imInPane.getChildren().add(circle);
                r = Math.random();
                g = Math.random();
                b = Math.random();
                Color color2 = new Color(r, g, b, 1.0);
                FillTransition ft = new FillTransition(Duration.millis(250), circle, color, color2);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
            }
        } else {
            double mapX = mapImg.getLayoutX();
            double mapY = mapImg.getLayoutY();

            for (Node n : list){
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX()/4.0);
                circle.setCenterY(mapY + n.getY()/4.0);
                circle.setRadius(3.0);
                imInPane.getChildren().add(circle);
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+this.floorPlan+"_NoIcons.png"))));

            dt = new DataTable();
            list = dt.getProjectCNodesByFloor(this.floorPlan);

            double mapX = mapImg.getLayoutX();
            double mapY = mapImg.getLayoutY();

            for (Node n : list){
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX()/4.0);
                circle.setCenterY(mapY + n.getY()/4.0);
                circle.setRadius(3.0);
                imInPane.getChildren().add(circle);
            }


        });
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }
}
