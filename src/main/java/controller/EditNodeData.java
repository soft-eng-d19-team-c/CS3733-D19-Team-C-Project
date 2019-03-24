package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditNodeData implements Initializable {
    @FXML
    private TextField testInput;
    Node nodeData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // nothing here we write a custom method yo
        Platform.runLater(() -> {
            testInput.setText(nodeData.getID());
        });
    }

    public void setNodeData(Node dataCursor) {
        this.nodeData = dataCursor;
    }
}
