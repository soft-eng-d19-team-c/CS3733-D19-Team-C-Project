package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import model.AuthException;
import base.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML public Label loggedOut;
    @FXML
    private TextField uname;
    @FXML
    private PasswordField password;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;
    @FXML private Label badCredentials;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
        uname.setText(null);
        password.setText(null);
        badCredentials.setVisible(false);
        loggedOut.setVisible(false);

        navController.setActiveTab(NavTypes.LOGIN);

        if (Main.screenController.getData("loggedout") != null && (Boolean) Main.screenController.getData("loggedout")) {
            loggedOut.setVisible(true);
        }

        password.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                loginButtonClick(new ActionEvent());
            }
        });

    }

    //does not currently actually log you in
    public void loginButtonClick(ActionEvent actionEvent) {
        try {
            Main.user = new User(uname.getText(), password.getText());
        } catch (AuthException e) {
            loggedOut.setVisible(false);
            badCredentials.setVisible(true);
            return;
        }
        Main.screenController.setScreen(EnumScreenType.PATHFINDING);
    }
}
