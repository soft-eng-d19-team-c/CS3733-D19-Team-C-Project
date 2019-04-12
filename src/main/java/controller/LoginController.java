package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    private TextField uname;
    @FXML
    private PasswordField password;
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //does not currently actually log you in
    public void loginButtonClick(ActionEvent actionEvent) {
        Main.user.tryLogin(uname.getText(), password.getText());
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
