package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardController extends Controller implements Initializable {
    @FXML
    private ImageView backgroundimage;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    public void serviceRequestButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SERVICESDASHBOARD);
    }

    public void findRoomButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SEARCHLOCATION);
    }

    public void employeeLoginButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.LOGIN);
    }

    public void mapButtonClick(ActionEvent actionEvent) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.PATHFINDING, hm);
    }

    public void viewServicesButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SERVICEREQUESTTABLESDASHBOARD);
    }

    public void bookRoomButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONSCALENDAR);
    }

    public void editMapButtonClick(ActionEvent actionEvent) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.EDITMAP, hm);
    }

    public void bookedLocationsButtonClick(ActionEvent e) {
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONMAP);
    }
}
