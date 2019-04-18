package controller;

import base.Main;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import model.SecurityRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class SecurityRequestController extends Controller implements Initializable {
    @FXML private JFXTextArea description;
    @FXML private CheckBox urgentCheckBox;
    @FXML private AutocompleteSearchBarController autoCompleteTextController;
    @FXML
    private ImageView backgroundimage;

    @FXML
    private NavController navController;


    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        description.setText(null);
        urgentCheckBox.setSelected(false);
        autoCompleteTextController.setLocation(null);
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    //save the service request to the database, to late view
    //set screen back to the dashboard
    public void submitButtonClick(ActionEvent e) {
        SecurityRequest securityRequest = new SecurityRequest(urgentCheckBox.isSelected(), autoCompleteTextController.getNodeID(), description.getText());
        securityRequest.insert();

        Main.screenController.goBack();
    }
}
