package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.HashMap;
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
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.FINDPATH, hm);
    }

    public void viewServicesButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.VIEWSERVICES);
    }

    public void bookRoomButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.BOOKROOM);
    }

    public void editMapButtonClick(ActionEvent actionEvent) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.MAP, hm);
    }

    public void bookedLocationsButtonClick(ActionEvent e) {
        Main.screenController.setScreen(EnumScreenType.BOOKEDLOCATIONS);
    }
}
