package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import model.ReligiousService;

import java.net.URL;
import java.util.ResourceBundle;

public class ReligiousServiceController extends Controller implements Initializable {

    @FXML
    private AutocompleteSearchBarController autoCompleteTextController;
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
        ReligiousService religiousServiceRequest = new ReligiousService(autoCompleteTextController.getNodeID(), serviceDescription.getText());
        religiousServiceRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

}
