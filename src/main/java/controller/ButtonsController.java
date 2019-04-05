package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonsController extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    //partial for home button (takes you to the dashboard)
    public void homeButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    //partial for logout button (takes to back ot the login screen)
    public void logOutButtonClick(ActionEvent actionEvent) {
         Main.screenController.setScreen(EnumScreenType.LOGIN);
    }

    //partial for return button (akes you back to previous page)
    public void returnButtonClick(ActionEvent actionEvent) {
        Main.screenController.goBack();
    }

}
