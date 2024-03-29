package controller;

import base.Main;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import model.BookableLocation;
import model.Booking;
import model.BookingCalendar;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class BookingController extends Controller implements Initializable {
    @FXML private Text errorText;
    BookingCalendar bookingCalendar = new BookingCalendar();
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private ComboBox locationBox;
    @FXML private Agenda agenda;
    @FXML private CalendarPicker calendar;
    @FXML private TextArea description;
    @FXML private NavController navController;
    private BookingCalendar.Appointment selectedAppointment;
    private ObservableList<BookableLocation> bookingLocations;
    private HashMap<String, BookableLocation> mapStringBookableLocation = new HashMap<>();

    //    @FXML public ImageView backgroundImage;
//    @FXML public NavController navController;
//    @FXML private JFXDatePicker datePicker;
//    @FXML private Agenda bookingAgenda;
//    private Callback<Agenda.LocalDateTimeRange, Agenda.Appointment> whatsGoingonThisWeek;
//    private ObservableList<Booking> Bookings;
//    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        backgroundImage.setImage(Main.screenController.getBackgroundImage());
//        navController.setActiveTab(NavTypes.ADMINVIEW);
//        Bookings = getAllBooking();
//        datePicker.setValue(LocalDate.now());
//        loadWeekSchedule();
//    }
//    private void loadWeekSchedule(){
//        bookingAgenda.appointments().remove(0, bookingAgenda.appointments().size());
//        for (Booking b : Bookings){
//            LocalDateTime st = b.getDateFromLocal();
//            LocalDateTime ft = b.getDateToLocal();
//            Calendar calst = Calendar.getInstance();
//            calst.set(st.getYear(), st.getMonthValue() - 1, st.getDayOfMonth(), st.getHour(), st.getMinute());
//            Calendar calft = Calendar.getInstance();
//            calft.set(ft.getYear(), ft.getMonthValue() - 1, ft.getDayOfMonth(), ft.getHour(), ft.getMinute());
//            bookingAgenda.appointments().add(
//                    new Agenda.AppointmentImpl()
//                            .withStartTime(calst)
//                            .withEndTime(calft)
//                            .withSummary(b.getDescription())
//                            .withLocation(b.getLocation())
//            );
//        }
//    }
//    private void getBColor(){ }
//    public void nextWeekBtnClick(ActionEvent actionEvent) {
//        bookingAgenda.setDisplayedLocalDateTime(bookingAgenda.getDisplayedLocalDateTime().plusWeeks(1));
//        datePicker.setValue(bookingAgenda.getDisplayedLocalDateTime().toLocalDate());
//    }
//    public void prevWeekBtnClick(ActionEvent actionEvent) {
//        bookingAgenda.setDisplayedLocalDateTime(bookingAgenda.getDisplayedLocalDateTime().minusWeeks(1));
//        datePicker.setValue(bookingAgenda.getDisplayedLocalDateTime().toLocalDate());
//    }
//    public void dateChange(ActionEvent actionEvent) {
//        bookingAgenda.setDisplayedLocalDateTime(LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth()).atTime(12,00));
//    }

    public String getGroup (String location){
        System.out.println(location);
        String result = "";
        if (location == "CLASS1"){result = "group1";}
        else if (location.equals("CLASS2")){result = "group2";}
        else if (location.equals("CLASS3")){result = "group3";}
        else if (location.equals("CLASS4")){result = "group4";}
        else if (location.equals("CLASS5")){result = "group5";}
        else if (location.equals("CLASS6")){result = "group6";}
        else if (location.equals("CLASS7")){result = "group7";}
        else if (location.equals("CLASS8")){result = "group8";}
        else if (location.equals("WZ1")){result = "group9";}
        else if (location.equals("WZ2")){result = "group10";}
        else if (location.equals("WZ3")){result = "group11";}
        else if (location.equals("WZ4")){result = "group12";}
        else if (location.equals("WZ5")){result = "group13";}
        else if (location.equals("AM1")){result = "group14";}
        else {result = "group0";}
        return result;
    }

    @FXML
    void addAppointment(ActionEvent event) {
        int id;
        Date selected = calendar.getCalendar().getTime();
        LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (startTime.getValue() == null || endTime.getValue() == null || locationBox.getValue() == null){
            errorText.setText("ERROR: please fill in the required fields");
            return;
        }
        LocalDateTime time_start = LocalDateTime.of(date, startTime.getValue());
        if (time_start.isBefore(LocalDateTime.now())){
            errorText.setText("ERROR: you can only schedule for the future");
            return;
        }
        if (endTime.getValue().isBefore(startTime.getValue())){
            errorText.setText("ERROR: Please set the end time after the start time. ");
            return;
        }
        BookableLocation b = (BookableLocation) locationBox.getValue();
        String location_s = b.getID();
        String summary_s = b.getTitle();
        Booking booking = new Booking(location_s, description.getText(), Timestamp.valueOf(startTime.getValue().atDate(date)), Timestamp.valueOf(endTime.getValue().atDate(date)), Main.user.getUsername(), 0);
        if(booking.hasConflicts()) {
            errorText.setText("Error: There is a booking that conflicts with this time. Try another time or location.");
            return;
        }
        Agenda.AppointmentImplLocal newAppointment = new BookingCalendar().new Appointment()
                .withStartLocalDateTime(startTime.getValue().atDate(date))
                .withEndLocalDateTime(endTime.getValue().atDate(date))
                .withDescription(description.getText())
                .withLocation(location_s)
                .withSummary(summary_s)
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(getGroup(location_s)));
        id = bookingCalendar.addNewAppointment(newAppointment);
        System.out.println(newAppointment.getAppointmentGroup().toString());
        BookingCalendar.Appointment a = (BookingCalendar.Appointment)newAppointment;
        a.setId(id);
        agenda.appointments().add(a);
        agenda.refresh();
        updateAppointment();
        errorText.setText("");
    }

    @FXML
    void deleteAppointment(ActionEvent event) {
        System.out.println(selectedAppointment.getId());
        bookingCalendar.deleteAppointment(selectedAppointment.getId());
        updateAgenda();
        agenda.refresh();
        startTime.setValue(LocalTime.now());
        endTime.setValue(null);
        locationBox.setValue(null);
        description.setText(null);
        errorText.setText("");
    }

    @FXML
    void updateAppointment(ActionEvent event) {
        Date selected;
        if (startTime.getValue().isBefore(LocalTime.now()) || endTime.getValue().isBefore(LocalTime.now())){
            errorText.setText("ERROR: you can only schedule for the future");
            return;
        }
        if (endTime.getValue().isBefore(startTime.getValue())){
            errorText.setText("ERROR: Please set the end time after the start time. ");
            return;
        }
        if( calendar.getCalendar()==null){
            selected = Date.from(selectedAppointment.getStartLocalDateTime().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }else {
            selected = calendar.getCalendar().getTime();
        }
        LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        BookableLocation b = (BookableLocation) locationBox.getValue();
        String location_s = b.getID();
        Booking booking = new Booking(location_s, description.getText(), Timestamp.valueOf(startTime.getValue().atDate(date)), Timestamp.valueOf(endTime.getValue().atDate(date)), Main.user.getUsername(), 0);
        if(booking.hasConflicts()) {
            errorText.setText("Error: There is a booking that conflicts with this time. Try another time or location.");
            return;
        }
        selectedAppointment.setStartLocalDateTime(startTime.getValue().atDate(date));
        selectedAppointment.setEndLocalDateTime(endTime.getValue().atDate(date));
        selectedAppointment.setDescription(description.getText());
        selectedAppointment.setLocation(location_s);
        selectedAppointment.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(getGroup(location_s)));
        bookingCalendar.updateAppointment(selectedAppointment);
        errorText.setText("");
        updateAgenda();
        agenda.refresh();
    }

    private void updateAppointment() { }

    private void updateAgenda(){
        agenda.localDateTimeRangeCallbackProperty().set(param -> {
             List<BookingCalendar.Appointment> list = bookingCalendar.getAppointments(param.getStartLocalDateTime(), param.getEndLocalDateTime());
                    agenda.appointments().clear();
                    agenda.appointments().addAll(list);
                    return null;
                }
        );
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendar.setCalendar(Calendar.getInstance());
        try { updateAgenda(); }
        catch (Exception e){ e.printStackTrace(); }
        bookingLocations = BookableLocation.getAllBookingLocations();
        for (BookableLocation b : bookingLocations){
            mapStringBookableLocation.put(b.getID(), b);
        }
        locationBox.setItems(bookingLocations);
        locationBox.setCellFactory(locationBoxFactory);
        locationBox.setButtonCell(locationBoxFactory.call(null));
        agenda.setAllowDragging(true);
        agenda.setAllowResize(true);
        navController.setActiveTab(NavTypes.ADMINVIEW);
        agenda.setEditAppointmentCallback( (appointment) -> {
            return null;
        });
        agenda.newAppointmentCallbackProperty().set((localDateTimeRange) -> {
            Agenda.AppointmentImplLocal appointmentImplLocal = new BookingCalendar().new Appointment()
                    .withStartLocalDateTime(localDateTimeRange.getStartLocalDateTime())
                    .withEndLocalDateTime(localDateTimeRange.getEndLocalDateTime())
                    .withLocation("AM1")
                    .withSummary("Amphiteather")
                    .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(getGroup("AM1")));
            int id = bookingCalendar.addNewAppointment(appointmentImplLocal);
            BookingCalendar.Appointment a = (BookingCalendar.Appointment)appointmentImplLocal;
            a.setId(id);
            return a;
        });
        calendar.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (calendar.getCalendar() == null){
                errorText.setText("ERROR!");
            }
            Date cal = calendar.getCalendar().getTime();
            LocalDate ld = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime lt = LocalTime.NOON;
            agenda.setDisplayedLocalDateTime(LocalDateTime.of(ld, lt));
            updateAgenda();
            errorText.setText("");
            startTime.setValue(LocalTime.now());
            endTime.setValue(null);
            locationBox.setValue(null);
            description.setText(null);
        });
        agenda.appointmentChangedCallbackProperty().set(param ->{
                    bookingCalendar.updateAppointment((BookingCalendar.Appointment)param);
                    return null;
                }
        );
        agenda.actionCallbackProperty().set(param ->
                {
                    selectedAppointment = (BookingCalendar.Appointment)param;
                    startTime.setValue(selectedAppointment.getStartLocalDateTime().toLocalTime());
                    endTime.setValue(selectedAppointment.getEndLocalDateTime().toLocalTime());
                    description.setText(selectedAppointment.getDescription());
                    locationBox.setValue(mapStringBookableLocation.get(selectedAppointment.getLocation()));
//                    locationBox.setValue(selectedAppointment.getSummary());
                    return null;
                }
        );
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
