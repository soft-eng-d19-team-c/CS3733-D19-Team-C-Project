package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ITRequest;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ITServiceRequestTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn locationCol;
    @FXML private TableColumn Description;
    @FXML private TableColumn userRequestedBy;
    @FXML private TableColumn isCompleted;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<ITRequest> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //SANITATIONREQUESTS.getAll(SANITATIONREQUESTS);
        //like initialize fcn in nodeTable

        locationCol.setCellValueFactory(new PropertyValueFactory("NodeID"));
        Description.setCellValueFactory(new PropertyValueFactory("Description"));
        userRequestedBy.setCellValueFactory(new PropertyValueFactory("Requested By"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

        updateTable();

    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        ITRequest sr = (ITRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = ITRequest.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
