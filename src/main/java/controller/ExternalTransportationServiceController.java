package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DateTimeMaker;
import model.ExternalTransportationRequest;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ExternalTransportationServiceController extends Controller implements Initializable {
    @FXML private JFXTextArea destinationField;
    @FXML
    private AutocompleteSearchBarController acSearchController;
    @FXML
    private GridPane formGridPane;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private JFXTimePicker timeField;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateField.setValue(null);
        timeField.setValue(null);
        destinationField.setText(null);
        Platform.runLater(() -> {
            double acSearchWidth = formGridPane.getColumnConstraints().get(1).getPercentWidth() / 100 * ((Stage) formGridPane.getScene().getWindow()).getWidth() - 100;
            acSearchController.acTextInput.setPrefWidth(acSearchWidth);
        });
    }

    public void saveBtnClick(ActionEvent actionEvent) {
        String pickupLocation = acSearchController.getNodeID();
        String dest = destinationField.getText();
        Timestamp dateTimeSubmitted = new Timestamp(System.currentTimeMillis());
        LocalDate date = dateField.getValue();
        LocalTime time = timeField.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
        Timestamp dateTimePickup = new Timestamp(cal.getTimeInMillis());
        ExternalTransportationRequest sr = new ExternalTransportationRequest(pickupLocation, dest, dateTimeSubmitted, dateTimePickup);
        System.out.println(sr.toString());
    }
}
