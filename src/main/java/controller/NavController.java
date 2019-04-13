package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class NavController extends Controller implements Initializable {
    @FXML
    public JFXButton mapButton;
    public JFXButton roomSearch;
    public JFXButton serviceRequest;
    public JFXButton bookRoom;
    public JFXButton adminView;
    public JFXButton loginButton;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void mapButtonClick(ActionEvent actionEvent) {
    }

    public void roomSearchButton(ActionEvent actionEvent) {
    }

    public void serviceRequestButton(ActionEvent actionEvent) {
    }

    public void bookRoomClick(ActionEvent actionEvent) {
    }

    public void adminViewClick(ActionEvent actionEvent) {
    }

    public void loginButtonClick(ActionEvent actionEvent) {
    }
}
