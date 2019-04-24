package controller;

import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.FloristServiceRequest;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class FloristServiceRequestController extends Controller implements Initializable {
    @FXML public Button submit;
    @FXML private AutocompleteSearchBarController acSearchStartController;
    @FXML private AutocompleteSearchBarController acSearchEndController;
    @FXML private TextField description;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acSearchStartController.setLocation((String) null);
        acSearchEndController.setLocation((String) null);
        description.setText(null);
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }


    public void submitBtnClick(ActionEvent actionEvent) {
        String startNodeID = acSearchStartController.getNodeID();
        String endNodeID = acSearchEndController.getNodeID();
        String desc = description.getText();
        Timestamp dateTimeSubmitted = new Timestamp(System.currentTimeMillis());

        FloristServiceRequest newServiceRequest = new FloristServiceRequest(startNodeID, endNodeID, desc, dateTimeSubmitted);

        newServiceRequest.insert();
        Main.screenController.goBack();
    }
}
