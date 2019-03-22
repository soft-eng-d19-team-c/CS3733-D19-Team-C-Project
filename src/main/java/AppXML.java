import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Ryan LaMarche.
 */
public class AppXML extends Application {
    private Stage primaryStage;
//    private HashMap<String, Parent> scenes = new HashMap<>();
    /**
     * Starting routine for AppXML view.
     * @param s the Stage to start on.
     */
    @Override
    public void start(Stage s) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/app.fxml")));
            s.setScene(scene);
            s.getIcons().add(new Image("img/icon.png"));
            s.setMaximized(true);
            s.setTitle("BWH Navigation Kiosk");
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.primaryStage = s;
    }

    /**
     * The main method / entry point for launching the AppXML instance.
     * @param args arguments provided to main method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
