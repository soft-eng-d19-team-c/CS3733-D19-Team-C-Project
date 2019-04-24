package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutPageController extends Controller implements Initializable {
    @FXML private NavController navController;
    @FXML private JFXButton creditsBtn;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.NONE);
        creditsBtn.toFront();
    }

    public void creditsBtnClick(ActionEvent e){
        Main.screenController.setScreen(EnumScreenType.CREDITS);
    }
}
