package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.InternalTransportationService;

import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class InternalTransportationController  extends Controller implements Initializable {

    @FXML private TextField description;
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTimePicker timeField;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateField.setValue(null);
        timeField.setValue(null);
        description.setText(null);

    }
    public void sumbitBtnClick(ActionEvent actionEvent) {
       // Timestamp dateTimeSubmitted = new Timestamp(System.currentTimeMillis());
        LocalDate date = dateField.getValue();
        LocalTime time = timeField.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
        Timestamp pickUpTime = new Timestamp(cal.getTimeInMillis());
       // ExternalTransportationRequest sr = new ExternalTransportationRequest(pickupLocation, dest, dateTimeSubmitted, dateTimePickup);

        InternalTransportationService internalTransportationServiceRequest = new InternalTransportationService( searchController_origController.getNodeID(), searchController_destController.getNodeID(), description.getText() , pickUpTime);
        //internalTransportationServiceRequest.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);

    }

}
