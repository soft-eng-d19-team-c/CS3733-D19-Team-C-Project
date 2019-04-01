package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.ServiceRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceRequestController extends Controller implements Initializable {

    @FXML private JFXTextField type;
    @FXML private JFXTextArea description;

    @FXML private AutocompleteSearchBar autoCompleteTextController;



    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void submitButtonClick(ActionEvent actionEvent) {
        ServiceRequest serviceRequest = new ServiceRequest(type.getText(), autoCompleteTextController.getNodeID(), description.getText());
        serviceRequest.insert();
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
