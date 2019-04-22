package controller;

import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.InternalTransportationService;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class InternalTransportationController  extends Controller implements Initializable {

    @FXML private Text errText;
    @FXML private TextArea description;
    @FXML private JFXDatePicker dateField;
    @FXML private JFXTimePicker timeField;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;
    @FXML private ImageView backgroundimage;
    @FXML private NavController navController;
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navController.setActiveTab(NavTypes.SERVICEREQUESTS);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
        dateField.setValue(null);
        timeField.setValue(null);
        description.setText(null);
        searchController_origController.setLocation((String) null);
        searchController_destController.setLocation((String) null);

    }
    public void submitBtnClick(ActionEvent actionEvent) {
        if (dateField.getValue() == null || timeField.getValue() == null){
            errText.setText("ERROR: Please enter the time");
            return;
        }
        LocalDate date = dateField.getValue();
        LocalTime time = timeField.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), time.getHour(), time.getMinute());
        Timestamp pickUpTime = new Timestamp(cal.getTimeInMillis());

        InternalTransportationService sr = new InternalTransportationService(searchController_origController.getNodeID(), searchController_destController.getNodeID(), description.getText(), pickUpTime);
        sr.insert();

        Main.screenController.goBack();

    }

}
