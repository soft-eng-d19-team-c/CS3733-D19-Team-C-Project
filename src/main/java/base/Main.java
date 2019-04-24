package base;

import model.AuthException;

/**
 * The Main class of the application that stores the User, Facade, and Database objects.
 * This class also spawns the FXML application.
 * @author Ryan LaMarche
 */
public class Main {
    public static Facade screenController;
    public static User user;
    public static ApplicationInformation info;
    public static AuthenticationManager auth;
    public static IdleMonitor idleMonitor;
    public static void main(String[] args) {
        auth = new AuthenticationManager();
        info = new ApplicationInformation("CHALL007L1");
        try {
            user = new User("guest", "guest");
        } catch (AuthException e) {
            e.printStackTrace();
        }
        MainFXML.main(args);
    }
}