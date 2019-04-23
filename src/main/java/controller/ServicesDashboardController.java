package controller;

//import api.ServiceException;
import base.EnumScreenType;
import base.Main;
//import base.PrescriptionRequestAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the dashboard of all services.
 * @author Ryan LaMarche
 */
public class ServicesDashboardController extends Controller implements Initializable {

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

    /**
     * Routing method for sanitation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void sanitationButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SANITATIONSERVICE);
    }

    /**
     * Routing method for interpreter.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void interpreterButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.INTERPRETERSERVICE);
    }

    /**
     * Routing method for internal transportation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void internalTransButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.INTERNALTRANSPORTATIONSERVICE);
    }

    /**
     * Routing method for external transportation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void externalTransButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.EXTERNALTRANSPORTATIONSERVICE);
    }

    /**
     * Routing method for security.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void securityButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SECURITYSERVICE);
    }

    /**
     * Routing method for prescription.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void prescriptionButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.PRESCRIPTIONSERVICE);
    }

    public void prescriptionAPIButtonClick(ActionEvent actionEvent) {
//        PrescriptionRequestAPI api = new PrescriptionRequestAPI();
//        try {
//            api.run(0,0,1920,1080,null,null,null);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Routing method for IT.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void ITButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.ITSERVICE);
    }

    /**
     * Routing method for gift store.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void giftStoreButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.GIFTSTORESERVICE);
    }

    /**
     * Routing method for religious.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void religiousButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.RELIGIOUSSERVICE);
    }

    /**
     * Routing method for florist.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void floristButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.FLORISTSERVICE);
    }
}
