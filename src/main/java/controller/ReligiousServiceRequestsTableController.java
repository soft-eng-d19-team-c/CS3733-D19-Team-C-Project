package controller;

import base.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.ReligiousService;

import java.net.URL;
import java.util.ResourceBundle;

public class ReligiousServiceRequestsTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn timeSubmitted;
    @FXML private TableColumn locationCol;
    @FXML private TableColumn Description;
    @FXML private TableColumn isCompleted;
    @FXML private ImageView backgroundimage;
    @FXML private AutocompleteSearchBarController autoCompleteTextController;
    @FXML private NavController navController;

    private ObservableList<ReligiousService> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //SANITATIONREQUESTS.getAll(SANITATIONREQUESTS);
        //like initialize fcn in nodeTable
        navController.setActiveTab(NavTypes.ADMINVIEW);
        backgroundimage.setImage(Main.screenController.getBackgroundImage());
        locationCol.setCellValueFactory(new PropertyValueFactory("NodeID"));
        Description.setCellValueFactory(new PropertyValueFactory("Description"));
        timeSubmitted.setCellValueFactory(new PropertyValueFactory("DateTimeSubmitted"));
        isCompleted.setCellValueFactory(new PropertyValueFactory("IsComplete"));

        updateTable();

    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        ReligiousService sr = (ReligiousService) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = ReligiousService.getAllServiceRequests();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
