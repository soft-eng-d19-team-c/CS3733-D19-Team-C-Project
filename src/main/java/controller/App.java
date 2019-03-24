package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ryan LaMarche.
 */
public class App implements Initializable {
    @FXML
    private Button roomButton;
    @FXML
    private Button restroomButton;
    @FXML
    private Button foodButton;
    @FXML
    private Button fullscreenButton;
    @FXML
    private Button downloadButton;

    /**
     * Changes scene root fxml file from app to pathfinding.
     * @param e the event that is triggered when roomButton is clicked.
     */
    @FXML
    public void roomButtonClick(ActionEvent e) {
        try {
            Parent newSceneRoot = FXMLLoader.load(getClass().getResource("/views/downloadscreen.fxml"));
            this.roomButton.getScene().setRoot(newSceneRoot);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * Changes scene root fxml file from app to pathfinding.
     * @param e the event that is triggered when downloadButton is clicked.
     */

    @FXML
    public void downloadButtonClick(ActionEvent e) {
        try {
            Parent newSceneRoot = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
            this.downloadButton.getScene().setRoot(newSceneRoot);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    /**
     *
     * @param e the event that is triggered when bathroomButton is clicked.
     */
    @FXML
    public void restroomButtonClick(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You clicked on the bathroom button!");
        a.showAndWait();
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
