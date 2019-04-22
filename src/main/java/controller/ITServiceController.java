package controller;

import base.Main;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import model.ITRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ITServiceController extends Controller implements Initializable {
    @FXML private JFXTextArea description;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;



    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        description.setText(null);
        autoCompleteTextController.setLocation((String) null);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
    }

    //save the service request to the database, to late view
    //set screen back to the dashboard
    public void submitButtonClick(ActionEvent actionEvent) {

        //Date dateTimeSubmitted = new Date().getTime()

        ITRequest ITRequest = new ITRequest(description.getText(), autoCompleteTextController.getNodeID());
        ITRequest.insert();

        Main.screenController.goBack();
    }
}

