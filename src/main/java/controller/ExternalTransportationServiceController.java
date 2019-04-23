package controller;

import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.ExternalTransportationRequest;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ExternalTransportationServiceController extends Controller implements Initializable {
    @FXML private Text errText;
    @FXML private JFXTextArea destinationField;
    @FXML
    private AutocompleteSearchBarController acSearchStartController;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private JFXTimePicker timeField;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;




    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateField.setValue(null);
        timeField.setValue(null);
        destinationField.setText(null);
        acSearchStartController.setLocation((String) null);
        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
    }

    public void saveBtnClick(ActionEvent actionEvent) {
        if (dateField.getValue() == null || timeField.getValue() == null){
            errText.setText("ERROR: Please enter the time");
            return;
        }
        String pickupLocation = acSearchStartController.getNodeID();
        String dest = destinationField.getText();
        Timestamp dateTimeSubmitted = new Timestamp(System.currentTimeMillis());
        LocalDate date = dateField.getValue();
        LocalTime time = timeField.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
        Timestamp dateTimePickup = new Timestamp(cal.getTimeInMillis());
        ExternalTransportationRequest sr = new ExternalTransportationRequest(pickupLocation, dest, dateTimeSubmitted, dateTimePickup);
        sr.insert();
        Main.screenController.goBack();
    }
}
