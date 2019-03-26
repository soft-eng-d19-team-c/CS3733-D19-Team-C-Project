package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class EditNodeData implements Initializable {
    @FXML
    private TextField nodeID;
    @FXML
    private TextField xCoord;
    @FXML
    private TextField yCoord;
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

    @FXML
    public void saveButtonClick(ActionEvent e) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            nodeID.setText(nodeData.getID());
            xCoord.setText(Integer.toString(nodeData.getX()));
            yCoord.setText(Integer.toString(nodeData.getY()));
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
