package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.ITRequest;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ITServiceController extends Controller implements Initializable {
    @FXML private JFXTextArea description;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;



    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    //save the service request to the database, to late view
    //set screen back to the dashboard
    public void submitButtonClick(ActionEvent actionEvent) {

        //Date dateTimeSubmitted = new Date().getTime()

        ITRequest ITRequest = new ITRequest(description.getText(), autoCompleteTextController.getNodeID());
        ITRequest.insert();

        Main.screenController.setScreen(EnumScreenType.EMPDASHBOARD);
    }
}

