package controller;

import base.EnumScreenType;
import base.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class Book extends Controller implements Initializable {


    //@FXML
    //private TextField type;

    //create booking object to be updated


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void bookRoomButtonClick(ActionEvent actionEvent) {
        //needs to update some sort of schedule saying which rooms are booked for certain times
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

}
