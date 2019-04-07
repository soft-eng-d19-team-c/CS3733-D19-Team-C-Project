package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ExternalTransportationServiceController extends Controller implements Initializable {
    @FXML
    private AutocompleteSearchBarController acSearchController;
    @FXML
    private GridPane formGridPane;


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO clear all input fields
        Platform.runLater(() -> {
            double acSearchWidth = formGridPane.getColumnConstraints().get(1).getPercentWidth() / 100 * ((Stage) formGridPane.getScene().getWindow()).getWidth() - 100;
            acSearchController.acTextInput.setPrefWidth(acSearchWidth);
        });
    }
}
