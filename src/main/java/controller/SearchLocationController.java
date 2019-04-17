package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchLocationController extends Controller implements Initializable {
    @FXML
    private AutocompleteSearchBarController acTextInputController;
    @FXML
    private NavController navController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acTextInputController.init(location, resources);

        navController.setActiveTab(NavTypes.FINDROOM);
    }


    public void searchLocation(ActionEvent actionEvent) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nodeID", acTextInputController.getNodeID());
        data.put("floor", acTextInputController.getNodeFloor());
        Main.screenController.setScreen(EnumScreenType.PATHFINDING, data);
    }
}
