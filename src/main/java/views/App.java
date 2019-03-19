package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ResourceBundle;


public class App implements Initializable {
    @FXML
    private Button roomButton;
    @FXML
    private Button bathroomButton;
    @FXML
    private Button foodButton;

    @FXML
    public void roomButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the room button!");
        a.showAndWait();
//        this.roomButton.setText("test");
    }
    @FXML
    public void bathroomButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the bathroom button!");
        a.showAndWait();
//        this.bathroomButton.setText("test");
    }
    @FXML
    public void foodButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the food button!");
        a.showAndWait();
//        this.foodButton.setText("test");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // empty
    }

}
