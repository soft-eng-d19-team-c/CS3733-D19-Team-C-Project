package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //does not currently actually log you in
    public void loginButtonClick(ActionEvent actionEvent) {
        Main.user.setPermissions("employee");
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
