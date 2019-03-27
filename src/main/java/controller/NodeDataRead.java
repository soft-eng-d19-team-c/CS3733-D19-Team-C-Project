package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DataTable;
import model.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NodeDataRead implements Initializable {
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
    DataTable dt;
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
        try {
            // try to load the FXML file and send the data to the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/editTableRow.fxml"));
            // try to change scene content
            Parent newRoot = loader.load();
            EditNodeData controller = loader.getController();
            controller.setNodeData(dataCursor);
            dataTable.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Route to download page with data.
     * @param e
     */
    public void downloadButtonClicked(ActionEvent e) {
        try {
            // try to load the FXML file and send the data to the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/downloadscreen.fxml"));
            // try to change scene content
            Parent newRoot = loader.load();
            DownloadScreen controller = loader.getController();
            controller.setDataTable(dt);
            dataTable.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void mapBtnL1Click(ActionEvent e) {
        try {
            // try to load the FXML file and send the data to the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/map.fxml"));
            // try to change scene content
            Parent newRoot = loader.load();
            Map controller = loader.getController();
            controller.setFloorPlan("L1");
            dataTable.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void mapBtnL2Click(ActionEvent e) {
        try {
            // try to load the FXML file and send the data to the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/map.fxml"));
            // try to change scene content
            Parent newRoot = loader.load();
            Map controller = loader.getController();
            controller.setFloorPlan("L2");
            dataTable.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Starting routing for this jfx view.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodeID.setCellValueFactory(new PropertyValueFactory("ID"));
        xCoord.setCellValueFactory(new PropertyValueFactory("X"));
        yCoord.setCellValueFactory(new PropertyValueFactory("Y"));
        floor.setCellValueFactory(new PropertyValueFactory("Floor"));
        building.setCellValueFactory(new PropertyValueFactory("Building"));
        nodeType.setCellValueFactory(new PropertyValueFactory("NodeType"));
        longName.setCellValueFactory(new PropertyValueFactory("LongName"));
        shortName.setCellValueFactory(new PropertyValueFactory("ShortName"));

        dt = new DataTable();
        this.data = dt.getAllData();
        // create edit buttons for each row in the table and append
        dataTable.setItems(this.data);
        dataTable.refresh();
    }
}
