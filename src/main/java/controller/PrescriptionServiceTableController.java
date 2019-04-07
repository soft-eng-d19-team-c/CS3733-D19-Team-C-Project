package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PrescriptionService;
import model.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class PrescriptionServiceTableController extends Controller implements Initializable {
    @FXML
    private TableView dataTable;

    @FXML private TableColumn ID;
    @FXML private TableColumn patientID;
    @FXML private TableColumn requesterID;
    @FXML private TableColumn resolverID;
    @FXML private TableColumn drug;
    @FXML private TableColumn timeOrdered;
    @FXML private TableColumn timeDelivered;



    @FXML private AutocompleteSearchBarController autoCompleteTextController;

    private ObservableList<PrescriptionService> data;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    //initialize the page with the service requests
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //serviceRequests.getAll(serviceRequests);
        //like initialize fcn in nodeTable

        ID.setCellValueFactory(new PropertyValueFactory("id"));
        ID.setCellValueFactory(new PropertyValueFactory("patientID"));
        ID.setCellValueFactory(new PropertyValueFactory("requesterID"));
        ID.setCellValueFactory(new PropertyValueFactory("resolverID"));
        ID.setCellValueFactory(new PropertyValueFactory("drug"));
        ID.setCellValueFactory(new PropertyValueFactory("timeOrdered"));
        ID.setCellValueFactory(new PropertyValueFactory("timeDelivered"));

        updateTable();

    }

    public void revolveRequestButtonClicked(javafx.event.ActionEvent actionEvent) {
        SanitationRequest sr = (SanitationRequest) dataTable.getSelectionModel().getSelectedItem();
        sr.resolve();
        updateTable();
    }

    private void updateTable() {
        data = PrescriptionService.getAllPrescriptionServices();

        dataTable.setItems(data);
        dataTable.refresh();
    }
}
