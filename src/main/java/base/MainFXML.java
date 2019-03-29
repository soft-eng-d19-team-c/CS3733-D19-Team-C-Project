package base;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Ryan LaMarche.
 */
public class MainFXML extends Application {
    /**
     * Starting routine for base.MainFXML view.
     * @param s the Stage to start on.
     */
    @Override
    public void start(Stage s) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(EnumScreenType.NODETABLE.getPath())));
            s.setScene(scene);
            s.getIcons().add(new Image("img/icon.png"));
            s.setMaximized(true);
            s.setTitle("BWH Navigation Kiosk");
            s.show();
            Main.screenController = new Facade(s.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method / entry point for launching the base.MainFXML instance.
     * @param args arguments provided to main method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
