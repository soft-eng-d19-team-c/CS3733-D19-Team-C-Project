package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan LaMarche.
 */
public class App implements Initializable {
    @FXML
    private Button roomButton;
    @FXML
    private Button bathroomButton;
    @FXML
    private Button foodButton;
    @FXML
    private Button fullscreenButton;

    /**
     *
     * @param e the event that is triggered when roomButton is clicked.
     */
    @FXML
    public void roomButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the room button!");
        a.showAndWait();
//        this.roomButton.setText("test");
    }
    /**
     *
     * @param e the event that is triggered when bathroomButton is clicked.
     */
    @FXML
    public void bathroomButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the bathroom button!");
        a.showAndWait();
//        this.bathroomButton.setText("test");
    }

    /**
     *
     * @param e the event that is triggered when foodButton is clicked.
     */
    @FXML
    public void foodButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the food button!");
        a.showAndWait();
//        this.foodButton.setText("test");
    }

    /**
     * Changes the stage to fullscreen mode.
     * @param e the event that is triggered when fullscreenButton is clicked.
     */
    @FXML
    public void fullscreenButtonClick(ActionEvent e) {
        ((Stage)this.fullscreenButton.getScene().getWindow()).setFullScreen(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // empty
    }

}
