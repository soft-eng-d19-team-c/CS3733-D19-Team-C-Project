package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.InternalTransportationService;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class InternalTransportationController  extends Controller implements Initializable {

    @FXML private TextField description;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        description.setText(null);
    }
    public void sumbitBtnClick(ActionEvent actionEvent) {

        InternalTransportationService internalTransportationServiceRequest = new InternalTransportationService( searchController_origController.getNodeID(), searchController_destController.getNodeID(), description.getText());
        internalTransportationServiceRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);

    }

}
