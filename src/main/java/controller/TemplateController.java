package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TemplateController extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            do stuff that you want to do when the page loads.
            For example if I wanted to get a Node object from the screen controller.
                   Node n = (Node) Main.screenController.getData("nodeKey");
            It is important to cast anything that you get from the screen controller data,
            because this returns an Object
        */
    }

    // --------------------------------------------------------------------------
    /*
        If I wanted to pass data to the next screen / controller, I need to put it in a HashMap<String, Object>

    public void buttonClick(ActionEvent event) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key", 12);
        Main.screenController.setScreen(EnumScreenType.PAGENAME, hashMap);
    }

    If I wanted to pass nothing to the next screen / controller, but I just want to change pages.

    public void buttonClick(ActionEvent event) {
        Main.screenController.setScreen(EnumScreenType.PAGENAME);
    }

    */
}
