package controller;

import base.Main;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.util.Duration;
import model.DataTable;
import model.Node;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Map extends Controller implements Initializable {
    @FXML
    private AnchorPane bigPane;
    @FXML
    private ImageView mapImg;
    @FXML
    private Pane imInPane;
    @FXML
    private ToggleButton dancePartyBtn;

    private DataTable dt;

    private String floorPlan;

    private LinkedList<Node> list;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void backBtnClick(ActionEvent e) {
        Main.screenController.setScreen(EnumScreenType.NODETABLE);
    }

    public void danceParty(ActionEvent e) {
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
        if (dancePartyBtn.isSelected()) {
            double mapX = mapImg.getLayoutX();
            double mapY = mapImg.getLayoutY();
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.4);
            mapImg.setEffect(blackout);
            Color c = new Color(0,0,0,1.0);
            bigPane.setBackground(new Background(new BackgroundFill(c, null, null)));
            dancePartyBtn.setTextFill(new Color(1, 1, 1, 1.0));
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
            drawNodes();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapImg.setImage(new Image(String.valueOf(getClass().getResource("/img/"+ Main.screenController.getData("floor")+"_NoIcons.png"))));
        Platform.runLater(() -> {
            dt = new DataTable();
            list = dt.getProjectCNodesByFloor((String) Main.screenController.getData("floor"));
            dancePartyBtn.setSelected(false);
            drawNodes();
        });
    }

    private void drawNodes() {
        Color c = new Color(1,1,1,1.0);
        bigPane.setBackground(new Background(new BackgroundFill(c, null, null)));
        dancePartyBtn.setTextFill(new Color(0, 0, 0, 1.0));

        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        mapImg.setEffect(reset);
        imInPane.getChildren().remove(1, imInPane.getChildren().size());
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
