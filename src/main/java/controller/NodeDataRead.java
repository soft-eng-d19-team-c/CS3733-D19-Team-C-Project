package controller;

import javafx.collections.FXCollections;
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
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NodeDataRead implements Initializable {
    @FXML
    private TableView<Node> table;
    @FXML
    private TableColumn nodeID;
    @FXML
    private TableColumn xcoord;
    @FXML
    private TableColumn ycoord;
    private ObservableList<Node> data;

    @FXML
    public void editButtonClicked(ActionEvent e) {
        try {
            Parent newSceneRoot = FXMLLoader.load(getClass().getResource("/views/editThing.fxml"));
            this.table.getScene().setRoot(newSceneRoot);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodeID.setCellValueFactory(new PropertyValueFactory("ID"));
        xcoord.setCellValueFactory(new PropertyValueFactory("x"));
        ycoord.setCellValueFactory(new PropertyValueFactory("y"));
        DataTable dt = new DataTable();
        this.data = dt.getAllData();
//        for (Node n : allData) {
            // add row to table
//        }
        table.setItems(this.data);
        table.refresh();
    }
}
