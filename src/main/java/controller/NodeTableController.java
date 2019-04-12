package controller;

import base.EnumScreenType;
import base.Main;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.NodeDataTable;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class NodeTableController extends Controller implements Initializable {
    // get table
    @FXML
    private TableView<Node> dataTable;
    // get columns
    @FXML
    private TableColumn nodeID;
    @FXML
    private TableColumn xCoord;
    @FXML
    private TableColumn yCoord;
    @FXML
    private TableColumn floor;
    @FXML
    private TableColumn building;
    @FXML
    private TableColumn nodeType;
    @FXML
    private TableColumn longName;
    @FXML
    private TableColumn shortName;
    // data to put in the table, consists of model.Node class members
    NodeDataTable dt;
    private ObservableList<Node> data;

    private Node dataCursor;


    /**
     * Method to run when an edit button is clicked on the table.
     * Load new fxml page with data for that Node.
     *
     * @param e
     */
    public void editSelectedNode(ActionEvent e) {
        dataCursor = dataTable.getSelectionModel().getSelectedItem();
        if (dataCursor == null) {
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("node",  dataCursor);
        Main.screenController.setScreen(EnumScreenType.NODEEDIT, hashMap);
    }

    /**
     * Route to download page with data.
     * @param e
     */
    public void downloadButtonClicked(ActionEvent e) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("table", this.dt);
        Main.screenController.setScreen(EnumScreenType.DOWNLOADCSV, hashMap);
    }

    public void mapBtnL1Click(ActionEvent e) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("floor", "L1");
        Main.screenController.setScreen(EnumScreenType.EDITMAP, hm);
    }

    public void mapBtnL2Click(ActionEvent e) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("floor", "L2");
        Main.screenController.setScreen(EnumScreenType.EDITMAP, hm);
    }

    /**
     * Starting routing for this jfx view.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            nodeID.setCellValueFactory(new PropertyValueFactory("ID"));
            xCoord.setCellValueFactory(new PropertyValueFactory("X"));
            yCoord.setCellValueFactory(new PropertyValueFactory("Y"));
            floor.setCellValueFactory(new PropertyValueFactory("Floor"));
            building.setCellValueFactory(new PropertyValueFactory("Building"));
            nodeType.setCellValueFactory(new PropertyValueFactory("NodeType"));
            longName.setCellValueFactory(new PropertyValueFactory("LongName"));
            shortName.setCellValueFactory(new PropertyValueFactory("ShortName"));

            dt = new NodeDataTable();
            this.data = dt.getAllNodeData();
            // create edit buttons for each row in the table and append
            dataTable.setItems(this.data);
            dataTable.refresh();
        });
    }

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

}
