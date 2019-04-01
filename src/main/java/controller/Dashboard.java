package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void serviceRequestButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.REQUESTSERVICE);
    }

    public void findRoomButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SELECTLOCATION);
    }

    public void employeeLoginButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.LOGIN);
    }

    public void mapButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.FINDPATH);
    }
}
