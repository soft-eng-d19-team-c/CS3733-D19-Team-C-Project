package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NavController extends Controller implements Initializable {
    @FXML public JFXButton mapButton;
    @FXML public JFXButton roomSearch;
    @FXML public JFXButton serviceRequest;
    @FXML public JFXButton bookRoom;
    @FXML public JFXButton adminView;
    @FXML public JFXButton loginButton;
    @FXML public Pane mapSelectBar;
    @FXML public Pane roomSelectBar;
    @FXML public Pane serviceSelectBar;
    @FXML public Pane bookSelectBar;
    @FXML public Pane adminSelectBar;
    @FXML public Pane loginSelectBar;

    private LinkedList<Pane> selectBars;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectBars = new LinkedList<>();
        selectBars.add(mapSelectBar);
        selectBars.add(roomSelectBar);
        selectBars.add(serviceSelectBar);
        selectBars.add(bookSelectBar);
        selectBars.add(adminSelectBar);
        selectBars.add(loginSelectBar);
    }

    public void mapButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.PATHFINDING);
    }

    public void roomSearchButton(ActionEvent actionEvent) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("showSearch", true);
        Main.screenController.setScreen(EnumScreenType.PATHFINDING, hm);
    }

    public void serviceRequestButton(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SERVICESDASHBOARD);
    }

    public void bookRoomClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONMAP);
    }

    public void adminViewClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.ADMIN);
    }

    public void loginButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.LOGIN);
    }



    public void setActiveTab(NavTypes tab) {
        for (Pane p : selectBars)
            p.setVisible(false);
        switch (tab) {
            case MAP:
                this.mapSelectBar.setVisible(true);
                break;
            case LOGIN:
                this.loginSelectBar.setVisible(true);
                break;
            case BOOKROOM:
                this.bookSelectBar.setVisible(true);
                break;
            case FINDROOM:
                this.roomSelectBar.setVisible(true);
                break;
            case ADMINVIEW:
                this.adminSelectBar.setVisible(true);
                break;
            case SERVICEREQUESTS:
                this.serviceSelectBar.setVisible(true);
                break;
            default:
                System.err.println("bad tab");
        }
        if (Main.user.checkPermissions("employee") || Main.user.checkPermissions("developer")) {
            bookRoom.setVisible(true);
            adminView.setVisible(true);
        } else {
            bookRoom.setVisible(false);
            adminView.setVisible(false);
        }
    }

    public void aboutButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.ABOUT);
    }
}
