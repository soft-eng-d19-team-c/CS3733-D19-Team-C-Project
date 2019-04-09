package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.GiftStoreRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class GiftStoreTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn issue;
    @FXML private TableColumn locationCol;
    @FXML private TableColumn Description;
    @FXML private TableColumn isCompleted;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<GiftStoreRequest> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //serviceRequests.getAll(serviceRequests);
        //like initialize fcn in nodeTable

        issue.setCellValueFactory(new PropertyValueFactory("Type"));
        locationCol.setCellValueFactory(new PropertyValueFactory("NodeID"));
        Description.setCellValueFactory(new PropertyValueFactory("Description"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

       // updateTable();

    }

    /*
    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        GiftStoreRequest sr = (GiftStoreRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = GiftStoreRequest.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
    */
}
