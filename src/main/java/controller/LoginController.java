package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.AuthException;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    private TextField uname;
    @FXML
    private PasswordField password;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
        uname.setText(null);
        password.setText(null);

        navController.setActiveTab(NavTypes.LOGIN);
    }

    //does not currently actually log you in
    public void loginButtonClick(ActionEvent actionEvent) {
        try {
            Main.user = new User(uname.getText(), password.getText());
        } catch (AuthException e) {
            e.printStackTrace();
        }
        Main.screenController.setScreen(EnumScreenType.PATHFINDING);
    }
}
