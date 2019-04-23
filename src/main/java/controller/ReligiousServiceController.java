package controller;

import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import model.ReligiousService;

import java.net.URL;
import java.util.ResourceBundle;

public class ReligiousServiceController extends Controller implements Initializable {

    @FXML
    private AutocompleteSearchBarController autoCompleteTextController;
    @FXML
    private TextArea serviceDescription;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceDescription.setText(null);
        autoCompleteTextController.setLocation((String) null);
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    public void submitButtonClick(ActionEvent actionEvent) {
        ReligiousService religiousServiceRequest = new ReligiousService(autoCompleteTextController.getNodeID(), serviceDescription.getText());
        religiousServiceRequest.insert();

        Main.screenController.goBack();
    }

}
