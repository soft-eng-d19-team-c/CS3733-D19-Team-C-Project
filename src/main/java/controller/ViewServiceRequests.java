package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ServiceRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewServiceRequests extends Controller implements Initializable {
    //@FXML
    //private TextField type;
    @FXML
    private JFXTextField type;
    @FXML private JFXTextArea description;

    @FXML
    private TableView dataTable;

    @FXML private TableColumn issue;
    @FXML private TableColumn locationCol;
    @FXML private TableColumn Description;
    @FXML private TableColumn isCompleted;

    @FXML private AutocompleteSearchBar autoCompleteTextController;

    private ObservableList<ServiceRequest> data;

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

        updateTable();

    }


    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        ServiceRequest sr = (ServiceRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }


    public void editServiceButton(ActionEvent actionEvent) {
        //nothing
        //do we want this?
    }

    private void updateTable() {
        data = ServiceRequest.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
