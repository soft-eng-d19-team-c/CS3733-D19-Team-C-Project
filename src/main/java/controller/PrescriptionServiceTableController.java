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

        ID.setCellValueFactory(new PropertyValueFactory("ID"));
        patientID.setCellValueFactory(new PropertyValueFactory("patientID"));
        requesterID.setCellValueFactory(new PropertyValueFactory("requesterID"));
        resolverID.setCellValueFactory(new PropertyValueFactory("resolverID"));
        drug.setCellValueFactory(new PropertyValueFactory("drug"));
        timeOrdered.setCellValueFactory(new PropertyValueFactory("timeOrdered"));
        timeDelivered.setCellValueFactory(new PropertyValueFactory("timeDelivered"));

        data = PrescriptionService.getAllPrescriptionServices();
        System.out.println(data);

        dataTable.setItems(data);
        dataTable.refresh();

    }

    private void updateTable() {

    }
}
