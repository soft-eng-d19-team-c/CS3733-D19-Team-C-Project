package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.PathAnalytics;
import model.SearchAnalytics;

import java.net.URL;
import java.util.ResourceBundle;

public class FeedbackAnalyticsController extends Controller implements Initializable {
    @FXML private AnchorPane anchorP;
    @FXML private NavController navController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location,resources);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.ADMINVIEW);
        anchorP.getChildren().remove(1, anchorP.getChildren().size());
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
        bc.setLayoutY(550);
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
        bc1.setLayoutY(550);
        anchorP.getChildren().add(bc1);
    }

}
