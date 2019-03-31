package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
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
