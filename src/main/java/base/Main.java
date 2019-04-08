package base;

import model.PrescriptionService;
import model.SanitationRequest;
import model.User;

/**
 * The Main class of the application that stores the User, Facade, and Database objects.
 * This class also spawns the FXML application.
 * @author Ryan LaMarche
 */
public class Main {
    public static Facade screenController;
    public static Database database;
    public static User user;
    public static ApplicationInformation info;
    public static void main(String[] args) {
        database = new Database(true, false);
        user = new User("username@example.com", "developer");
        info = new ApplicationInformation(EnumAlgorithm.ASTAR, "CHALL007L1");
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
