package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GiftStoreController extends Controller implements Initializable {
    @FXML private JFXTextField recipient;
    @FXML private JFXTextField sender;
    @FXML private AutocompleteSearchBarController autocompletetextcontroller;


    public void submitButtonClick(ActionEvent actionEvent) {

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    public JFXTextField getRecipient() {
        return recipient;
    }

    public void setRecipient(JFXTextField recipient) {
        this.recipient = recipient;
    }

    public JFXTextField getSender() {
        return sender;
    }

    public void setSender(JFXTextField sender) {
        this.sender = sender;
    }

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
