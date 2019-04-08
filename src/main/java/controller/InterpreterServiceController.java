package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import model.InterpreterRequest;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class InterpreterServiceController extends Controller implements Initializable {
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTimePicker timeField;
    @FXML private AutocompleteSearchBarController acSearchController;
    @FXML private JFXTextArea description;



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
        InterpreterRequest interpreterRequest = new InterpreterRequest(acSearchController.getNodeID(), description.getText());
//        interpreterRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    public void saveBtnClick(ActionEvent actionEvent) {
    }

    public void viewBtnClick(ActionEvent actionEvent) {
    }
}
