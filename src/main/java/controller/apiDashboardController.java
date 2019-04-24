package controller;

import TeamCPrescriptionRequestAPI.base.PrescriptionRequestAPI;
import TeamCPrescriptionRequestAPI.model.ServiceException;
import base.Main;
import bishopfishapi.Emergency;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d19.teamD.Babysitting;
import edu.wpi.cs3733.d19.teamD.DBControllerAPI;
import floral.api.FloralApi;
import giftRequest.GiftRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class apiDashboardController extends Controller implements Initializable {

    @FXML
    private NavController navController;
    @FXML private ImageView backgroundimage;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    public void prescriptionServicesButtonClick(ActionEvent actionEvent){
        try {
            new PrescriptionRequestAPI().run(0,0,1920,1080, null, null, null);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void giftRequestButtonClick(ActionEvent actionEvent){
        GiftRequest gr = new GiftRequest();
        try {
            gr.run(0,0,1920,1080,null,null,null);
        } catch (Exception e){
            System.out.println("Failed to run API"); e.printStackTrace();
        }
    }
    

    public void FloralButtonClick(ActionEvent actionEvent){
        FloralApi floralApi = new FloralApi();
        try {
            floralApi.run(0, 0, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emergencyButtonClick(ActionEvent actionEvent){
        Emergency test = new Emergency();
        try {
            test.run(0,0,1920,1080,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
