package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminDashboardController extends Controller implements Initializable {
    @FXML
    private ImageView backgroundimage;
    @FXML private NavController navController;
    @FXML private JFXButton editMapButton;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.ADMINVIEW);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());

        if(Main.user.checkPermissions("developer")){
            editMapButton.setVisible(true);
        }else{
            editMapButton.setVisible(false);
        }
        }

    public void editMapButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.EDITMAP);
    }

    public void serviceRequestButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SERVICEREQUESTTABLESDASHBOARD);
    }

    public void bookedLocationsButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONSCALENDAR); //to Wenjng's schedule
    }
}
