package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.InterpreterRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class InterpreterRequestTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn location;
    @FXML private TableColumn time;
    @FXML private TableColumn description;
    @FXML private TableColumn completedBy;
    @FXML private TableColumn isCompleted;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<InterpreterRequest> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //serviceRequests.getAll(serviceRequests);
        //like initialize fcn in nodeTable

        this.location.setCellValueFactory(new PropertyValueFactory("NodeID"));
        time.setCellValueFactory(new PropertyValueFactory("DateTimeRequest"));
        description.setCellValueFactory(new PropertyValueFactory("Description"));
        completedBy.setCellValueFactory(new PropertyValueFactory("CompletedBy"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

        updateTable();

    }

    public void revolveRequestButtonClicked(ActionEvent actionEvent) {
        InterpreterRequest sr = (InterpreterRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = InterpreterRequest.getAllServiceRequests();
        dataTable.setItems(data);
        dataTable.refresh();
    }
}
