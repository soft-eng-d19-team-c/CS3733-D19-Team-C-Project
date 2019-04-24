package controller;

import base.Main;
import com.sun.javafx.charts.Legend;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.Feedback;
import model.PathAnalytics;
import model.SearchAnalytics;

import java.net.URL;
import java.util.ResourceBundle;

public class FeedbackAnalyticsController extends Controller implements Initializable {
    @FXML private AnchorPane anchorP;
    @FXML private NavController navController;
    @FXML private ImageView backgroundimage;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location,resources);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.ADMINVIEW);
        anchorP.getChildren().remove(2, anchorP.getChildren().size());

        // pie chart stuff
        PieChart pc = new PieChart(Feedback.getPieChartData());
//        for (PieChart.Data d : pc.getData()) {
//            switch (d.getName()) {
//                case "Happy":
//                    d.getNode().setStyle("-fx-pie-color: green;");
//                    break;
//                case "Neutral":
//                    d.getNode().setStyle("-fx-pie-color: yellow;");
//                    break;
//                case "Sad":
//                    d.getNode().setStyle("-fx-pie-color: red;");
//                    break;
//            }
//        }
        pc.setLabelsVisible(false);
        pc.setTitle("User Feedback");
        pc.setLayoutX(700);
        pc.setLayoutY(150);
        anchorP.getChildren().add(pc);

        // bar chart stuff
        CategoryAxis x = new CategoryAxis();
        x.setTickLabelFill(Color.TRANSPARENT);
        NumberAxis y = new NumberAxis();
        x.setLabel("Path");
        y.setLabel("Frequency");
        BarChart bc = new BarChart<>(x, y);
        bc.setTitle("Most Frequently Used Paths");
        bc.getData().addAll(PathAnalytics.getPathAnalyticsData());
        bc.setLayoutX(400);
        bc.setLayoutY(575);
        anchorP.getChildren().add(bc);

        // bar chart stuff
        CategoryAxis x1 = new CategoryAxis();
        x1.setTickLabelFill(Color.TRANSPARENT);
        NumberAxis y1 = new NumberAxis();
        x1.setLabel("Location");
        y1.setLabel("Frequency");
        BarChart bc1 = new BarChart<>(x1, y1);
        bc1.setTitle("Most Searched for Locations");
        bc1.getData().addAll(SearchAnalytics.getPathAnalyticsData());
        bc1.setLayoutX(900);
        bc1.setLayoutY(575);
        anchorP.getChildren().add(bc1);

        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

}
