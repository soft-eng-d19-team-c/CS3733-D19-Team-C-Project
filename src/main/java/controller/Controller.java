package controller;

import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller {
    public abstract void init(URL location, ResourceBundle resources);

    //to include the partials
    @FXML private ButtonsController buttonsController;
}
