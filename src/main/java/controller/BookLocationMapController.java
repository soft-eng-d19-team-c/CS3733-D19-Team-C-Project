package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import model.BookableLocation;
import model.Booking;

import java.awt.print.Book;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class BookLocationMapController extends Controller implements Initializable {
    @FXML private ImageView backgroundImage;
    @FXML private JFXTimePicker endTimePicker;
    @FXML private JFXDatePicker endDatePicker;
    @FXML private JFXTimePicker startTimePicker;
    @FXML private JFXDatePicker startDatePicker;

    @FXML private JFXTextArea description;
    @FXML private ComboBox<BookableLocation> locationBox;
    @FXML private Text errorText;
    @FXML private ImageView mapImgView;
    @FXML private Pane circlePane;

    private LinkedList<Booking> bookings = new LinkedList<>();
    private LinkedList<Booking> bookinge = new LinkedList<>();
    private ObservableList<BookableLocation> bookingLocations;
    @FXML private NavController navController;
    private HashMap<String, Integer> bookingLocationsIndexMap = new HashMap<>();
    private int bookableLocationId;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location,resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.setImage(Main.screenController.getBackgroundImage());
        navController.setActiveTab(NavTypes.BOOKROOM);
        startDatePicker.setValue(LocalDate.now());
        startTimePicker.setValue(LocalTime.now());
        endTimePicker.setValue(null);
        endDatePicker.setValue(null);
        mapImgView.fitWidthProperty().bind(circlePane.widthProperty());
        mapImgView.fitHeightProperty().bind(circlePane.heightProperty());
        bookingLocations = BookableLocation.getAllBookingLocations();
        bookRandomDesks();
        locationBox.setItems(bookingLocations);
        locationBox.setCellFactory(locationBoxFactory);
        locationBox.setButtonCell(locationBoxFactory.call(null));
        Platform.runLater(() -> inputChanged(null));
        errorText.setText("");


    }

    @SuppressWarnings("Duplicates")
    public void inputChanged(ActionEvent event) {
        LocalDate sd = startDatePicker.getValue();
        LocalTime st = startTimePicker.getValue();
        LocalDate ed = endDatePicker.getValue();
        LocalTime et = endTimePicker.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(sd.getYear(), sd.getMonthValue() - 1, sd.getDayOfMonth(), st.getHour(), st.getMinute());
        Timestamp ts = new Timestamp(cal.getTimeInMillis());
        Timestamp te;
        if (ed != null && et != null){
            Calendar cal2 = Calendar.getInstance();
            cal2.set(ed.getYear(), ed.getMonthValue() - 1, ed.getDayOfMonth(), et.getHour(), et.getMinute());
            te = new Timestamp(cal2.getTimeInMillis());
            bookinge = Booking.getBookingsDuring(te);
        }
        bookings = Booking.getBookingsDuring(ts);
        draw();
        errorText.setText("");
    }

    @SuppressWarnings("Duplicates")
    private void draw() {
//        System.out.println("Draw");
        bookingLocations = BookableLocation.getAllBookingLocations();
        bookings = Booking.getBookingsDuring(new Timestamp(System.currentTimeMillis()));

//        circlePane.getChildren().clear();
        bookableLocationId = 0;
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
            c.setRadius(8.0);
            c.getProperties().put("BookableLocation", b);
            bookingLocationsIndexMap.put(b.getID(), bookableLocationId);
            bookableLocationId ++;
            c.addEventFilter(MouseEvent.MOUSE_PRESSED, selectlocationHandler);
            Tooltip t = new Tooltip(b.getTitle());
            Tooltip.install(c, t);
            c.setCursor(Cursor.HAND);
            circlePane.getChildren().add(c);
        }
        for (Booking b : bookings) {
            int x = b.getBookedLocation().getX();
            int y = b.getBookedLocation().getY();
            Circle c = new Circle();
            c.setCenterX(mapX + x / mapScale);
            c.setCenterY(mapY + y / mapScale);
            c.setFill(Color.RED);
            c.setRadius(8.0);
            Tooltip t = new Tooltip(b.getBookedLocation().getTitle());
            Tooltip.install(c, t);
            circlePane.getChildren().add(c);
        }
        for (Booking b : bookinge) {
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
        errorText.setText("");
        if (locationBox.getValue() == null){
            errorText.setText("Error: Please select a classroom to book.");
            return;
        }
        if (endDatePicker.getValue() == null || endTimePicker.getValue() == null) {
            errorText.setText("Error: End Time must not be empty");
            return;
        }
        //needs to update some sort of schedule saying which rooms are booked for certain times
        String bookingLocation = locationBox.getValue().getID();

        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimePicker.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimePicker.getValue();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        startDateTime.until(endDateTime, HOURS);
        int lengthInMillis = (int)startDateTime.until(endDateTime, HOURS) * 60 * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
        Timestamp tsStart = new Timestamp(cal.getTimeInMillis());
        Timestamp tsEnd = Timestamp.valueOf(endDateTime);
        if (tsEnd.before(tsStart)){
            errorText.setText("ERROR: The end time must be set to after the start time");
            return;
        }
        if (tsStart.before(Calendar.getInstance().getTime())){
            errorText.setText("Error: The time you are trying to book has already past");
            return;
        }
        System.out.println("1");

        Booking b = new Booking(bookingLocation, description.getText(), tsStart, tsEnd, Main.user.getUsername(), 0);
        System.out.println("2");

        if(b.hasConflicts()){
            errorText.setText("Error: There is a booking that conflicts with this time. Try another time or location.");
        } else {
            b.insert();
            System.out.println("3");
            Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONSCALENDAR);
        }
    }

    public void goCalendarBtnClick(ActionEvent actionEvent) {
        Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONSCALENDAR);
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

    @SuppressWarnings("Duplicates")
    EventHandler selectlocationHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent me) {
            System.out.println("testing");
            if (me.getButton().equals(MouseButton.PRIMARY)) {
                Circle circle = (Circle) me.getTarget();
                BookableLocation b = (BookableLocation) circle.getProperties().get("BookableLocation");
                System.out.println("Bookable id: " + b.getTitle());
//                locationBox.getSelectionModel().select(bookingLocationsIndexMap.get(b.getID()));
                locationBox.setValue(b);

//                HashMap<String, Object> hm = new HashMap<>();
//                hm.put("node", n);
//                Main.screenController.setScreen(EnumScreenType.NODEEDIT, hm);
            }
        }
    };


    @SuppressWarnings("Duplicates")
    private void bookRandomDesks(){
        LinkedList<BookableLocation> desks = new LinkedList<>();

        // get all desks
        for (BookableLocation b : bookingLocations){
            if (b.getType().equals("DESK") || b.getType().equals("BANK")){
                desks.add(b);
            }
        }

        // Create random bookings
        for (BookableLocation b : desks){
            // 10% chance of booking a desk
            if (Math.random() < 0.1){
                String bookingLocation = b.getID();
                LocalDate date = LocalDate.now();
                LocalTime localTime = LocalTime.now();

                int length = (int) (Math.random() * 15.0 + 5.0);


                int lengthInMillis = length * 1000; // random time between 5 to 30 seconds
                Calendar cal = Calendar.getInstance();
                cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
                Timestamp tsStart = new Timestamp(cal.getTimeInMillis());
                Timestamp tsEnd = new Timestamp(cal.getTimeInMillis() + lengthInMillis);
                Booking booking = new Booking(bookingLocation, description.getText(), tsStart, tsEnd, Main.user.getUsername(), 0);
                if(!booking.hasConflicts()){
                    booking.insert();
//                    System.out.println("Booked desk: " + b.getTitle() + " for " + length + " seconds");
                } else {
//                    System.out.println("Conflict when booking desk: " + b.getTitle());
                }
            }
        }

    }

    public void startSimulation(ActionEvent actionEvent){
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bookRandomDesks();
                draw();
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        new Thread(()->{
            try {
                Thread.sleep(1000 * 60 * 2); // waits two minutes and stops the timeline
//                System.out.println("Stopping timer");
                fiveSecondsWonder.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
