package controller;

import base.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.FloristServiceRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class FloristServiceRequestTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn startNode;
    @FXML private TableColumn endNode;
    @FXML private TableColumn dateTimeSubmitted;
    @FXML private TableColumn Description;
    @FXML private TableColumn isCompleted;
    @FXML private ImageView backgroundImage;
    @FXML private NavController navController;

    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<FloristServiceRequest> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navController.setActiveTab(NavTypes.ADMINVIEW);
        backgroundImage.setImage(Main.screenController.getBackgroundImage());

        startNode.setCellValueFactory(new PropertyValueFactory("StartNodeID"));
        endNode.setCellValueFactory(new PropertyValueFactory("EndNodeID"));
        Description.setCellValueFactory(new PropertyValueFactory("Description"));
        dateTimeSubmitted.setCellValueFactory(new PropertyValueFactory("DateTimeSubmitted"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

        updateTable();

    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        FloristServiceRequest sr = (FloristServiceRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = FloristServiceRequest.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
