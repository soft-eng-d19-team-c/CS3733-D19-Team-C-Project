import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AppXML extends Application {
    @Override
    public void start(Stage s) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("views/app.fxml")));
            s.setScene(scene);
            s.getIcons().add(new Image("img/icon.png"));
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
