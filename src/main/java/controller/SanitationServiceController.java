package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class SanitationServiceController extends Controller implements Initializable {
    @FXML private JFXTextField type;
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
        SanitationRequest sanitationRequest = new SanitationRequest(type.getText(), autoCompleteTextController.getNodeID(), description.getText());
        sanitationRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
