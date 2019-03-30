package base;

public class Main {
    public static Facade screenController;
    public static Database database;
    public static void main(String[] args) {
        database = new Database();
        MainFXML app = new MainFXML();
        app.main(args);
    }
}
