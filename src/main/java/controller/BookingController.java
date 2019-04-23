package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import model.BookableLocation;
import model.BookingCalendar;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController extends Controller implements Initializable {
    BookingCalendar bookingCalendar = new BookingCalendar();
    @FXML private ComboBox locationBox;
    @FXML private Agenda agenda;
    @FXML private CalendarPicker calendar;
    @FXML private LocalTimeTextField startTime;
    @FXML private LocalTimeTextField endTime;
    @FXML private TextArea description;
    private BookingCalendar.Appointment selectedAppointment;
    private ObservableList<BookableLocation> bookingLocations;

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

    @FXML
    void addAppointment(ActionEvent event) {
        int id;
        Date selected = calendar.getCalendar().getTime();
        LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        BookableLocation b = (BookableLocation) locationBox.getValue();
        String location_s = b.getID();
        Agenda.AppointmentImplLocal newAppointment = new BookingCalendar().new Appointment()
                .withStartLocalDateTime(startTime.getLocalTime().atDate(date))
                .withEndLocalDateTime(endTime.getLocalTime().atDate(date))
                .withDescription(description.getText())
                .withLocation(location_s)
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
        id = bookingCalendar.addNewAppointment(newAppointment);
        BookingCalendar.Appointment a = (BookingCalendar.Appointment)newAppointment;
        a.setId(id);
        agenda.appointments().add(a);
        agenda.refresh();
        updateAppointment();
    }

    @FXML
    void deleteAppointment(ActionEvent event) {
        System.out.println(selectedAppointment.getId());
        bookingCalendar.deleteAppointment(selectedAppointment.getId());
        updateAgenda();
        agenda.refresh();
    }

    @FXML
    void updateAppointment(ActionEvent event) {
        Date selected;
        if( calendar.getCalendar()==null){
            selected = Date.from(selectedAppointment.getStartLocalDateTime().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }else {
            selected = calendar.getCalendar().getTime();
        }
        LocalDate date = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        selectedAppointment.setStartLocalDateTime(startTime.getLocalTime().atDate(date));
        selectedAppointment.setEndLocalDateTime(endTime.getLocalTime().atDate(date));
        selectedAppointment.setDescription(description.getText());
        bookingCalendar.updateAppointment(selectedAppointment);
        updateAgenda();
        agenda.refresh();
    }

    private void updateAppointment() { }

    private void updateAgenda(){;
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
        try { updateAgenda(); }
        catch (Exception e){ e.printStackTrace(); }
        bookingLocations = BookableLocation.getAllBookingLocations();
        locationBox.setItems(bookingLocations);
        locationBox.setCellFactory(locationBoxFactory);
        locationBox.setButtonCell(locationBoxFactory.call(null));
        agenda.setAllowDragging(true);
        agenda.setAllowResize(true);
        BookableLocation b = (BookableLocation) locationBox.getValue();
        String location_s = b.getID();
        agenda.newAppointmentCallbackProperty().set((localDateTimeRange) -> {
            Agenda.AppointmentImplLocal appointmentImplLocal = new BookingCalendar().new Appointment()
                    .withStartLocalDateTime(localDateTimeRange.getStartLocalDateTime())
                    .withEndLocalDateTime(localDateTimeRange.getEndLocalDateTime())
                    .withLocation(location_s)
                    .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
            int id = bookingCalendar.addNewAppointment(appointmentImplLocal);
            BookingCalendar.Appointment a = (BookingCalendar.Appointment)appointmentImplLocal;
            a.setId(id);
            return a;
        });
        calendar.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Date cal = calendar.getCalendar().getTime();
            LocalDate ld = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime lt = LocalTime.NOON;
            agenda.setDisplayedLocalDateTime(LocalDateTime.of(ld, lt));
            updateAgenda();
        });
        agenda.appointmentChangedCallbackProperty().set(param ->{
                    bookingCalendar.updateAppointment((BookingCalendar.Appointment)param);
                    return null;
                }
        );
        agenda.actionCallbackProperty().set(param ->
                {
                    selectedAppointment = (BookingCalendar.Appointment)param;
                    startTime.setLocalTime(selectedAppointment.getStartLocalDateTime().toLocalTime());
                    endTime.setLocalTime(selectedAppointment.getEndLocalDateTime().toLocalTime());
                    description.setText(selectedAppointment.getDescription());
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
