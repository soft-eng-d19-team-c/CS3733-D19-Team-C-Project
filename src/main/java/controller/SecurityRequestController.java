package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import model.SecurityRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class SecurityRequestController extends Controller implements Initializable {
    @FXML private JFXTextArea description;
    @FXML private CheckBox urgentCheckBox;
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
    public void submitButtonClick(ActionEvent e) {
        SecurityRequest securityRequest = new SecurityRequest(urgentCheckBox.isSelected(), autoCompleteTextController.getNodeID(), description.getText());
        securityRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}