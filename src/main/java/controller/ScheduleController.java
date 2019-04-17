package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController extends Controller implements Initializable {
    @FXML
    private NavController navController;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navController.setActiveTab(NavTypes.BOOKROOM);
    }
}
