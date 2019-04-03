package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Selectlocation extends Controller implements Initializable {
    @FXML private Button searchLocationbtn;
    @FXML
    private AutocompleteSearchBar acTextInputController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void searchLocation(ActionEvent actionEvent) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nodeID", acTextInputController.getNodeID());
//  TODO      data.put("floor", acTextInputController.getNodeFloor());
        data.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.FINDPATH, data);
    }
}
