package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.FloristServiceRequest;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class FloristServiceRequestController extends Controller implements Initializable {
    @FXML
    private AutocompleteSearchBarController acSearchStartController;
    @FXML
    private AutocompleteSearchBarController acSearchEndController;
    @FXML
    private TextField description;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        description.setText(null);
    }


    public void submitBtnClick(ActionEvent actionEvent) {
        String startNodeID = acSearchStartController.getNodeID();
        String endNodeID = acSearchEndController.getNodeID();
        String desc = description.getText();
        Timestamp dateTimeSubmitted = new Timestamp(System.currentTimeMillis());

        FloristServiceRequest newServiceRequest = new FloristServiceRequest(startNodeID, endNodeID, desc, dateTimeSubmitted);

        newServiceRequest.insert();
        Main.screenController.setScreen(EnumScreenType.EMPDASHBOARD);
    }
}
