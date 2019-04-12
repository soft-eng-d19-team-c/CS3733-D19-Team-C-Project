package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Booking;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookingsController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;
    @FXML private TableColumn locationColumn;
    @FXML private TableColumn fromColumn;
    @FXML private TableColumn toColumn;
    @FXML private TableColumn userColumn;
    private ObservableList<Booking> data;



    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locationColumn.setCellValueFactory(new PropertyValueFactory("Location"));
        fromColumn.setCellValueFactory(new PropertyValueFactory("DateFrom"));
        toColumn.setCellValueFactory(new PropertyValueFactory("DateTo"));
        userColumn.setCellValueFactory(new PropertyValueFactory("Username"));

        data = Booking.getCurrentBookings();

        dataTable.setItems(data);

    }
}
