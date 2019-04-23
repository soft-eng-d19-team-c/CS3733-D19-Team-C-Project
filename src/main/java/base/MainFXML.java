package base;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * The Main class for the JavaFX application. This class spawns the JavaFX application and creates
 * the Facade and IdleMonitor, as well as starting up the home screen of the application.
 * @author Ryan LaMarche
 */
public class MainFXML extends Application {
    /**
     * Starting routine for base.MainFXML view.
     * @author Ryan LaMarche
     * @param s the Stage to start on.
     */
    @Override
    public void start(Stage s) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(EnumScreenType.BOOKLOCATIONSCALENDAR.getPath())));
            s.setScene(scene);
            Main.screenController = new Facade(s.getScene());
            Main.idleMonitor = new IdleMonitor(Duration.seconds(30),
                () -> {
                    Main.user.logout();
                    Main.screenController.clearHistory();
                    Main.screenController.setScreen(EnumScreenType.BOOKLOCATIONSCALENDAR);
            }, true);
            Main.idleMonitor.register(scene, Event.ANY);
            s.getIcons().add(new Image("img/icon.png"));
            s.setMaximized(true);
            s.setTitle("BWH Navigation Kiosk");
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method / entry point for launching the base.MainFXML instance.
     * @author Ryan LaMarche
     * @param args arguments provided to main method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
