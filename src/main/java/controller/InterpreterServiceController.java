package controller;

import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import model.InterpreterRequest;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class InterpreterServiceController extends Controller implements Initializable {
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTimePicker timeField;
    @FXML private AutocompleteSearchBarController acSearchController;
    @FXML private JFXTextArea description;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;



    //String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateField.setValue(null);
        timeField.setValue(null);
        description.setText(null);
        acSearchController.setLocation(null);
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    //save the service request to the database, to late view
    //set screen back to the dashboard
    public void saveBtnClick(ActionEvent actionEvent) {
        Timestamp dateTimeRequest = new Timestamp(System.currentTimeMillis());
        LocalDate date = dateField.getValue();
        LocalTime time = timeField.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
        InterpreterRequest interpreterRequest = new InterpreterRequest(acSearchController.getNodeID(), dateTimeRequest, description.getText());
        interpreterRequest.insert();
        Main.screenController.goBack();
    }
}
