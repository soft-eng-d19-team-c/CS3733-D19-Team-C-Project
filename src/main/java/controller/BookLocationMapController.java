package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.BookableLocation;
import model.Booking;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class BookLocationMapController extends Controller implements Initializable {
    @FXML private JFXTextField duration;
    @FXML private ComboBox<BookableLocation> locationBox;
    @FXML private Text errorText;
    @FXML private JFXDatePicker datePicker;
    @FXML private JFXTimePicker timePicker;
    @FXML private ImageView mapImgView;
    @FXML private Pane circlePane;

    private LinkedList<Booking> bookings;
    private ObservableList<BookableLocation> bookingLocations;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location,resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setValue(LocalDate.now());
        timePicker.setValue(LocalTime.now());
        mapImgView.fitWidthProperty().bind(circlePane.widthProperty());
        mapImgView.fitHeightProperty().bind(circlePane.heightProperty());
        bookingLocations = BookableLocation.getAllBookingLocations();
        locationBox.setItems(bookingLocations);
        locationBox.setCellFactory(locationBoxFactory);
        locationBox.setButtonCell(locationBoxFactory.call(null));
        Platform.runLater(() -> inputChanged(null));
        errorText.setText("");
    }

    @SuppressWarnings("Duplicates")
    public void inputChanged(ActionEvent event) {
        LocalDate d = datePicker.getValue();
        LocalTime t = timePicker.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(d.getYear(), d.getMonthValue() - 1, d.getDayOfMonth(), t.getHour(), t.getMinute());
        Timestamp ts = new Timestamp(cal.getTimeInMillis());
        bookings = Booking.getBookingsDuring(ts);
        draw();
    }

    @SuppressWarnings("Duplicates")
    private void draw() {
        circlePane.getChildren().remove(1, circlePane.getChildren().size());
        // image real size / image display size => scale
        double mapX = mapImgView.getLayoutX();
        double mapY = mapImgView.getLayoutY();
        double mapScale = mapImgView.getImage().getWidth() / mapImgView.boundsInParentProperty().get().getWidth();
        for (BookableLocation b : bookingLocations) {
            int x = b.getX();
            int y = b.getY();
            Circle c = new Circle();
            c.setCenterX(mapX + x / mapScale);
            c.setCenterY(mapY + y / mapScale);
            c.setFill(Color.GREEN);
            c.setRadius(10.0);
            Tooltip t = new Tooltip(b.getTitle());
            Tooltip.install(c, t);
            circlePane.getChildren().add(c);
        }
        for (Booking b : bookings) {
            int x = b.getBookedLocation().getX();
            int y = b.getBookedLocation().getY();
            Circle c = new Circle();
            c.setCenterX(mapX + x / mapScale);
            c.setCenterY(mapY + y / mapScale);
            c.setFill(Color.RED);
            c.setRadius(10.0);
            Tooltip t = new Tooltip(b.getBookedLocation().getTitle());
            Tooltip.install(c, t);
            circlePane.getChildren().add(c);
        }
    }

    public void bookRoomBtnClick(ActionEvent actionEvent) {
        if (locationBox.getValue() == null){
            errorText.setText("Error: Please select a classroom to book.");
            return;
        }
        //needs to update some sort of schedule saying which rooms are booked for certain times
        String bookingLocation = locationBox.getValue().getID();

        LocalDate date = datePicker.getValue();
        LocalTime localTime = timePicker.getValue();
        String inputDuration = duration.getText();
        for (char c : inputDuration.toCharArray()) {
            if (!Character.isDigit(c)) {
                errorText.setText("Error: Duration must be a valid number.");
                return;
            }
        }
        int lengthInMillis = Integer.parseInt(inputDuration) * 60 * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        Timestamp tsStart = new Timestamp(cal.getTimeInMillis());
        Timestamp tsEnd = new Timestamp(cal.getTimeInMillis() + lengthInMillis);
        Booking b = new Booking(bookingLocation, "", tsStart, tsEnd, Main.user.getUsername(), 0);
        if(b.hasConflicts()){
            errorText.setText("Error: There is a booking that conflicts with this time. Try another time or location.");
        } else {
            b.insert();
            Main.screenController.setScreen(EnumScreenType.DASHBOARD);
        }
    }

    public void goCalendarBtnClick(ActionEvent actionEvent) {

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
