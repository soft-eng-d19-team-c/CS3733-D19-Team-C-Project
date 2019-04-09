package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.ReligiousService;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ReligiousServiceController extends Controller implements Initializable {

    @FXML
    private AutocompleteSearchBarController searchLocationController;
    @FXML
    private TextArea serviceDescription;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void submitButtonClick(ActionEvent actionEvent) {
        ReligiousService religiousServiceRequest = new ReligiousService(searchLocationController.getNodeID(), serviceDescription.getText());
        religiousServiceRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

}
