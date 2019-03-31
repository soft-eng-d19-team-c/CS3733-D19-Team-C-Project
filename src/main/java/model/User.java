package model;

//A user of the kiosk system

public class User {
    private String username;
    private String permissions;

    public User(String username, String permissions) {
        this.username = username;
        this.permissions = permissions; //eg guest, employee, dev
    }

    public String getUsername() {
        return username;
    }
}
