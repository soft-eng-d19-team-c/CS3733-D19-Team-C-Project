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

public class ViewServiceRequests extends Controller implements Initializable {
    //@FXML
    //private TextField type;
    @FXML
    private JFXTextField type;
    @FXML private JFXTextArea description;

    @FXML private AutocompleteSearchBar autoCompleteTextController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //serviceRequests.getAll(serviceRequests);
        //like initialize fcn in nodeTable

    }


    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {

        ServiceRequest serviceRequest = new ServiceRequest(type.getText(), autoCompleteTextController.getNodeID(), description.getText());
        serviceRequest.update();
        Main.screenController.setScreen(EnumScreenType.VIEWSERVICES);
    }


    public void editServiceButton(ActionEvent actionEvent) {
        //nothing
        //do we want this?
    }
}
