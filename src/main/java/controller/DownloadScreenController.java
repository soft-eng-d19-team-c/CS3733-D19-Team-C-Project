package controller;

import base.EnumScreenType;
import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import model.NodeDataTable;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController extends Controller implements Initializable {

    private NodeDataTable dataTable;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dataTable = (NodeDataTable) Main.screenController.getData("table");
    }

    public void downloadButtonClick(ActionEvent actionEvent) {
        this.dataTable.printToCsv();
        Main.screenController.setScreen(EnumScreenType.NODETABLE);
    }
}
