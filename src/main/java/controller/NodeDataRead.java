package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import model.DataTable;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NodeDataRead implements Initializable {
    @FXML
    private TableView table;

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
        DataTable dt = new DataTable();
        LinkedList<String[]> allData = dt.getAllData();
        for (String [] s : allData) {
            // add row to table
        }
    }
}
