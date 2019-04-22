package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FeedbackAnalyticsController extends Controller implements Initializable {
    @FXML private AnchorPane anchorP;

    private ObservableList<PieChart.Data> pieChartData;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location,resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // let's make some graphs
    }

}
