package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ExternalTransportationRequest;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ExternalTransportationServiceRequestsTableController extends Controller implements Initializable {
    @FXML private TableView dataTable;
    @FXML private TableColumn idCol;
    @FXML private TableColumn locationCol;
    @FXML private TableColumn destination;
    @FXML private TableColumn isCompleted;
    private ObservableList<ExternalTransportationRequest> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory("ID"));
        locationCol.setCellValueFactory(new PropertyValueFactory("NodeID"));
        destination.setCellValueFactory(new PropertyValueFactory("Destination"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsCompleted"));
        updateTable();
    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        ExternalTransportationRequest sr = (ExternalTransportationRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = ExternalTransportationRequest.getAllServiceRequests();
        dataTable.setItems(data);
        dataTable.refresh();
    }
}
