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
    @FXML
    private TextField nodeID;
    @FXML
    private TextField xcoord;
    @FXML
    private TextField ycoord;
    @FXML
    private TextField floor;
    @FXML
    private TextField building;
    @FXML
    private TextField nodeType;
    @FXML
    private TextField longName;
    @FXML
    private TextField shortName;


    Node nodeData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            testInput.setText(nodeData.getID());
            nodeID.setText(nodeData.getID());
            xcoord.setText(Integer.toString(nodeData.getX()));
            ycoord.setText(Integer.toString(nodeData.getY()));
            floor.setText(nodeData.getFloor());
            building.setText(nodeData.getBuilding());
            nodeType.setText(nodeData.getNodeType());
            longName.setText(nodeData.getLongName());
            shortName.setText(nodeData.getShortName());

        });
    }

    public void setNodeData(Node dataCursor) {
        this.nodeData = dataCursor;
    }
}
