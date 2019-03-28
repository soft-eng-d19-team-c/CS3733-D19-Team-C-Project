package base;

/**
 * @author Ryan LaMarche.
 */
public class Main {
    public static Facade screenController;
    public static Database database;
    public static void main(String[] args) {
        database = new Database();
        // creates new base.MainFXML object that loads main.fxml on a new thread, starting at its main method.
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
