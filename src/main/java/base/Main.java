package base;

import model.AuthException;
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
    public static AuthenticationManager auth;
    public static void main(String[] args) {
        auth = new AuthenticationManager();
        database = new Database(true, false);
        info = new ApplicationInformation("CHALL007L1");
        try {
            user = new User("guest", "guest");
        } catch (AuthException e) {
            e.printStackTrace();
        }
        MainFXML app = new MainFXML();
        app.main(args);
    }
}
