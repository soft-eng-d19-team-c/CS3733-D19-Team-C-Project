package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import model.BookableLocation;
import model.Booking;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import static model.Booking.getAllBooking;

public class BookingController extends Controller implements Initializable {

    @FXML private JFXDatePicker datePicker;
    @FXML private Agenda bookingAgenda;

    private Callback<Agenda.LocalDateTimeRange, Agenda.Appointment> whatsGoingonThisWeek;
    private ObservableList<Booking> Bookings = getAllBooking();

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setValue(LocalDate.now());
        bookingAgenda.createDefaultSkin();
        loadWeekSchedule();
    }

    private void loadWeekSchedule(){
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
                            .withDescription(b.getLocation() + "\n" + b.getDescription()));
        }
//        bookingAgenda.newAppointmentCallbackProperty().setValue(whatsGoingonThisWeek);
    }

    public void nextWeekBtnClick(ActionEvent actionEvent) {

    }

    public void prevWeekBtnClick(ActionEvent actionEvent) {

    }
}
