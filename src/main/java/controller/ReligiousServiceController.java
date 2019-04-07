package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.ReligiousServices;
import model.ServiceRequest;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ReligiousServiceController extends Controller implements Initializable {

    @FXML
    private AutocompleteSearchBar searchLocation;
    @FXML
    private TextArea serviceDescription;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void sumbitButtonClick(ActionEvent actionEvent) {
        ReligiousService religiousServiceRequest = new ReligiousService(searchLocation.getNodeID(), serviceDescription.getText());
        religoiousServiceRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

}
