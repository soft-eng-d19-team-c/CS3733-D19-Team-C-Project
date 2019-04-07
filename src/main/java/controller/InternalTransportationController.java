package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class InternalTransportationController  extends Controller implements Initializable {


    @FXML
    private AutocompleteSearchBar searchController_origController;
    @FXML private AutocompleteSearchBar searchController_destController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void sumbitBtnClick(ActionEvent actionEvent) {

    }

}
