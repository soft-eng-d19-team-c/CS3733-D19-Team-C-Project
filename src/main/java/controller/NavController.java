package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
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
    @FXML public JFXButton logoutButton;

    private LinkedList<Pane> selectBars;

    // clock
    private int hour;
    private int minute;
    private String minuteAsString;
    @FXML private Label clock;

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

        Platform.runLater(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                minute = LocalDateTime.now().getMinute();
                minuteAsString = minute < 10 ? "0" + minute : "" + minute;
                hour = LocalDateTime.now().getHour();
                String suffix = hour < 12 ? " am" : " pm";
                hour %= 12;
                hour = hour == 0 ? 12 : hour;
                this.clock.setText(hour + ":" + (minuteAsString) + suffix);
            }),
                new KeyFrame(Duration.seconds(1))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        });


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
            case NONE:
            default:
                // hide all tabs
        }
        if (Main.user != null && (Main.user.checkPermissions("employee") || Main.user.checkPermissions("developer"))) {
            bookRoom.setVisible(true);
            adminView.setVisible(true);
            logoutButton.setVisible(true);
            loginButton.setVisible(false);
        } else {
            bookRoom.setVisible(false);
            adminView.setVisible(false);
            logoutButton.setVisible(false);
            loginButton.setVisible(true);
        }
    }

    public void aboutButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.ABOUT);
    }

    public void logoutButtonClick(ActionEvent actionEvent) {
        Main.user.logout();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("loggedout", true);
        Main.screenController.setScreen(EnumScreenType.LOGIN, hm, false);
    }

    public void feedbackBtnClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.FEEDBACK);
    }
}
