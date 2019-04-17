package controller;

import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import model.Booking;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import static model.Booking.getAllBooking;

public class BookingController extends Controller implements Initializable {

    @FXML public ImageView backgroundImage;
    @FXML public NavController navController;
    @FXML private JFXDatePicker datePicker;
    @FXML private Agenda bookingAgenda;

    private Callback<Agenda.LocalDateTimeRange, Agenda.Appointment> whatsGoingonThisWeek;
    private ObservableList<Booking> Bookings;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.setImage(Main.screenController.getBackgroundImage());
        navController.setActiveTab(NavTypes.BOOKROOM);
        Bookings = getAllBooking();
        datePicker.setValue(LocalDate.now());
//        bookingAgenda.createDefaultSkin();
//        bookingAgenda.skin

        loadWeekSchedule();
    }

    private void loadWeekSchedule(){
//        bookingAgenda.refresh();
        bookingAgenda.appointments().remove(0, bookingAgenda.appointments().size());
        for (Booking b : Bookings){
            LocalDateTime st = b.getDateFromLocal();
            LocalDateTime ft = b.getDateToLocal();
            Calendar calst = Calendar.getInstance();
            calst.set(st.getYear(), st.getMonthValue() - 1, st.getDayOfMonth(), st.getHour(), st.getMinute());
            Calendar calft = Calendar.getInstance();
            calft.set(ft.getYear(), ft.getMonthValue() - 1, ft.getDayOfMonth(), ft.getHour(), ft.getMinute());
            bookingAgenda.appointments().add(
                    new Agenda.AppointmentImpl()
                            .withStartTime(calst)
                            .withEndTime(calft)
                            .withSummary(b.getDescription())
                            .withLocation(b.getLocation())
            );
        }
//        bookingAgenda.newAppointmentCallbackProperty().setValue(whatsGoingonThisWeek);

//        bookingAgenda.setLocalDateTimeRangeCallback(new Callback<Agenda.LocalDateTimeRange, Void>() {
//            @Override
//            public Void call(Agenda.LocalDateTimeRange param) {
//                return null;
//            }
//        });


    }

    private void getBColor(){

    }

    public void nextWeekBtnClick(ActionEvent actionEvent) {
        bookingAgenda.setDisplayedLocalDateTime(bookingAgenda.getDisplayedLocalDateTime().plusWeeks(1));
        datePicker.setValue(bookingAgenda.getDisplayedLocalDateTime().toLocalDate());
    }

    public void prevWeekBtnClick(ActionEvent actionEvent) {
        bookingAgenda.setDisplayedLocalDateTime(bookingAgenda.getDisplayedLocalDateTime().minusWeeks(1));
        datePicker.setValue(bookingAgenda.getDisplayedLocalDateTime().toLocalDate());
    }

    public void dateChange(ActionEvent actionEvent) {
        bookingAgenda.setDisplayedLocalDateTime(LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth()).atTime(12,00));
    }
}
