package controller;

import base.EnumScreenType;
import base.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.Booking;
import model.BookableLocation;
import model.DateTimeMaker;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class BookingController extends Controller implements Initializable {

    @FXML
    private ComboBox<BookableLocation> locationBox;
    @FXML
    private ComboBox<String> startTimeBox;
    @FXML
    private ComboBox<String> endTimeBox;
    @FXML
    private DatePicker datePicker;

    //create booking object to be updated
    private ObservableList<BookableLocation> bookableLocations;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] times = {"12:00am", "12:30am", "1:00am", "1:30am", "2:00am", "2:30am", "3:00am", "3:30am", "4:00am", "4:30am", "5:00am", "5:30am", "6:00am", "6:30am", "7:00am", "7:30am", "8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm", "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm", "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"};
        startTimeBox.getItems().addAll(times);
        endTimeBox.getItems().addAll(times);
        bookableLocations = BookableLocation.getAllBookingLocations();
        locationBox.setItems(bookableLocations);
        locationBox.setCellFactory(locationBoxFactory);
        locationBox.setButtonCell(locationBoxFactory.call(null));
    }



    public void bookRoomButtonClick(ActionEvent actionEvent) {
        //needs to update some sort of schedule saying which rooms are booked for certain times
        String bookingLocation = locationBox.getValue().getID();
        LocalDate date = datePicker.getValue();

        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 0, 0);
        Timestamp tsStart = new Timestamp(cal.getTimeInMillis());
        DateTimeMaker.addTime(startTimeBox.getValue(), tsStart); // modifies ts
        Timestamp tsEnd = new Timestamp(cal.getTimeInMillis());
        DateTimeMaker.addTime(endTimeBox.getValue(), tsEnd); // modifies ts



        Booking b = new Booking(bookingLocation, "", tsStart, tsEnd, Main.user, 0);
        b.insert();

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    private Callback<ListView<BookableLocation>, ListCell<BookableLocation>> locationBoxFactory = new Callback<ListView<BookableLocation>, ListCell<BookableLocation>>() {
        @Override
        public ListCell<BookableLocation> call(ListView<BookableLocation> param) {
            return new ListCell<BookableLocation>(){
                @Override
                public void updateItem(BookableLocation item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item!=null && !empty){
                        setText(item.getTitle());
                    }
                }
            };
        }
    };

}
