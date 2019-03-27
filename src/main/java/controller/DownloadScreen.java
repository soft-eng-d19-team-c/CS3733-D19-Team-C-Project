package controller;

import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.DataTable;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreen extends Controller implements Initializable {
    @FXML
    private Button downloadButton;

    private DataTable dataTable;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dataTable = (DataTable) Main.screenController.getData("table");
    }

    public void downloadButtonClick(ActionEvent actionEvent) {
        this.dataTable.printToCsv();
        Main.screenController.setScreen(EnumScreenType.NODETABLE);
    }
}
