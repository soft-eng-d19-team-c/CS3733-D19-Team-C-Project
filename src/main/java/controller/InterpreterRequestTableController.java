package controller;

import base.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    @FXML private ImageView backgroundImage;
    @FXML private NavController navController;


    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<InterpreterRequest> data;

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
        backgroundImage.setImage(Main.screenController.getBackgroundImage());
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
