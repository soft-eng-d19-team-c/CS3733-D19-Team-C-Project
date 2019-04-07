package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.PrescriptionService;

import java.net.URL;
import java.util.ResourceBundle;

public class PrescriptionServiceController extends Controller implements Initializable {

    @FXML private JFXTextArea patientID;
    @FXML private JFXTextArea prescriptionDescription;
//    @FXML private JFXButton viewTableButton;
//    @FXML private JFXTextArea description;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void submitButtonClick(ActionEvent actionEvent) {
        // should also check permissions in the future
        PrescriptionService prescriptionService = new PrescriptionService(patientID.getText(), Main.user.getUsername(), prescriptionDescription.getText());
        prescriptionService.insert();

        patientID.clear();
        prescriptionDescription.clear();
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    public void viewTable(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.PRESCRIPTIONSERVICETABLE);
    }



}
