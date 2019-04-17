package controller;

import base.EnumScreenType;
import base.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EditNodeDataController extends Controller implements Initializable {
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
    @FXML private NavController navController;

    private Node nodeData;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    private boolean strshorterthan255 (String str){
        return (str.length() < 255);
    }

    public void backButtonClick(ActionEvent e) {
        Main.screenController.goBack();
    }


    public void saveButtonClick(ActionEvent e) {
        try {
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
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("floor", "L1");
            Main.screenController.setScreen(EnumScreenType.EDITMAP, hm);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodeData = (Node) Main.screenController.getData("node");
        navController.setActiveTab(NavTypes.ADMINVIEW);

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


}
