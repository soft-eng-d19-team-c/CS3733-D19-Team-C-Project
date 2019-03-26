package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Node;

import java.io.IOException;
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


    private Node nodeData;

    private boolean strshorterthan255 (String str){
        return (str.length() < 255);
    }

    public void backButtonClick(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/emptyTable.fxml"));
            Parent newRoot = loader.load();
            nodeID.getScene().setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void saveButtonClick(ActionEvent e) {
        /*
            TODO
            1. collect data from input fields
            2. parse and sanitize data
            3. trigger update
            4. return to previous page if update returns true
         */
        try{
            int X = Integer.parseInt(xCoord.getText());
            int Y = Integer.parseInt(yCoord.getText());
            nodeData.setX(X);
            nodeData.setY(Y);
        }
        catch (NumberFormatException exp){
            return;
        }

        if (strshorterthan255(building.getText())){ nodeData.setBuilding(building.getText()); } else {
            return;
        }
        if (strshorterthan255(floor.getText())){ nodeData.setFloor(floor.getText()); } else {
            return;
        }
        if (strshorterthan255(longName.getText())){ nodeData.setLongName(longName.getText()); } else {
            return;
        }
        if (strshorterthan255(shortName.getText())){ nodeData.setShortName(shortName.getText()); } else {
            return;
        }
        if (strshorterthan255(nodeType.getText())){ nodeData.setNodeType(nodeType.getText()); } else {
            return;
        }
        int updateflag;
        updateflag = nodeData.update();
        if (updateflag > 0) {
            backButtonClick(e);
        }
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
