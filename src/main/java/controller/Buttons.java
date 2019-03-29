package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import base.EnumScreenType;
import base.Main;
import jdk.management.cmm.SystemResourcePressureMXBean;
//import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Buttons extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void homeButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.NODETABLE);
    }

    public void logOutButtonClick(ActionEvent actionEvent) {
        System.out.println("logged out");
        // Main.screenController.setScreen(EnumScreenType.LOGIN);
    }

    public void returnButtonClick(ActionEvent actionEvent) {
        Main.screenController.goBack();
    }

}
