package base;

import model.User;

public class Main {
    public static Facade screenController;
    public static Database database;
    public static User user;
    public static void main(String[] args) {
        database = new Database();
        user = new User("username@example.com", "guest");
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
