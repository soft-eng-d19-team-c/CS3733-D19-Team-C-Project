package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import model.PrescriptionService;

import java.net.URL;
import java.util.ResourceBundle;

public class PrescriptionServiceController extends Controller implements Initializable {

    @FXML private JFXTextArea patientID;
    @FXML private JFXTextArea prescriptionDescription;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;


//    @FXML private JFXButton viewTableButton;
//    @FXML private JFXTextArea description;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        patientID.clear();
        prescriptionDescription.clear();
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    public void submitButtonClick(ActionEvent actionEvent) {
        // should also check permissions in the future
        PrescriptionService prescriptionService = new PrescriptionService(patientID.getText(), Main.user.getUsername(), prescriptionDescription.getText());
        prescriptionService.insert();


        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
