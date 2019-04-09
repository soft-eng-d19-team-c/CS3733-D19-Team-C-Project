package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        Platform.runLater(() -> inputChanged(null));
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
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONS);
    }
}
