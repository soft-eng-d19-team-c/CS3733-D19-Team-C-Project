package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import base.EnumScreenType;
import base.Main;
import model.Node;
import model.ServiceRequest;
import model.User;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ServiceRequestController extends Controller implements Initializable {

    //@FXML
    //private TextField type;


    @FXML private String type;
    @FXML private Node location;
    @FXML private String description;
    @FXML private Date dateTimeSubmitted;
    @FXML private Date dateTimeResolved;
    @FXML private boolean isComplete;
    @FXML private User completedBy;
    @FXML private User requestedBy;

    ServiceRequest serviceRequest = new ServiceRequest(type, location,  description, dateTimeSubmitted, dateTimeResolved, isComplete,completedBy, requestedBy);



    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void submitButtonClick(ActionEvent actionEvent) {
        serviceRequest.update();
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }
}
