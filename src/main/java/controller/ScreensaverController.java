package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreensaverController extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.EMPDASHBOARD);
    }
}
