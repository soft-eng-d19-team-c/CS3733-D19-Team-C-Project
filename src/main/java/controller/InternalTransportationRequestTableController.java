package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.InternalTransportationService;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class InternalTransportationRequestTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn locationFrom;
    @FXML private TableColumn locationTo;
    @FXML private TableColumn pickupTime;
    @FXML private TableColumn requestedBy;
    @FXML private TableColumn Description;
    @FXML private TableColumn isCompleted;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<InternalTransportationService> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //serviceRequests.getAll(serviceRequests);
        //like initialize fcn in nodeTable

        locationFrom.setCellValueFactory(new PropertyValueFactory("NodeID"));
        locationTo.setCellValueFactory(new PropertyValueFactory("NodeIDDest"));
        pickupTime.setCellValueFactory(new PropertyValueFactory("PickUpTime"));
        requestedBy.setCellValueFactory(new PropertyValueFactory("RequestedBy"));
        Description.setCellValueFactory(new PropertyValueFactory("Description"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

        updateTable();

    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        InternalTransportationService sr = (InternalTransportationService) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = InternalTransportationService.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
