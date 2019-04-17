package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceRequestsTablesDashboardController extends Controller implements Initializable {

    @FXML
    private NavController navController;
    @FXML private ImageView backgroundimage;



    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.ADMINVIEW);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());

    }

    /**
     * Routing method for sanitation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void sanitationButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SANITATIONSERVICETABLE);
    }

    /**
     * Routing method for interpreter.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void interpreterButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.INTERPRETERSERVICETABLE);
    }

    /**
     * Routing method for internal transportation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void internalTransButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.INTERNALTRANSPORTATIONSERVICETABLE);
    }

    /**
     * Routing method for external transportation.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void externalTransButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.EXTERNALTRANSPORTATIONSERVICETABLE);
    }

    /**
     * Routing method for security.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void securityButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.SECURITYSERVICETABLE);
    }

    /**
     * Routing method for prescription.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void prescriptionButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.PRESCRIPTIONSERVICETABLE);
    }

    /**
     * Routing method for IT.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void ITButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.ITSERVICETABLE);
    }

    /**
     * Routing method for gift store.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void giftStoreButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.GIFTSTORESERVICETABLE);
    }

    /**
     * Routing method for religious.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void religiousButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.RELIGIOUSSERVICETABLE);
    }

    /**
     * Routing method for florist.
     * @author Ryan LaMarche
     * @param actionEvent
     */
    public void floristButtonClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.FLORISTSERVICETABLE);
    }
}
