package model;

//A user of the kiosk system

public class User {
    private String username;
    private String permissions;

    public User(String username, String permissions) {
        this.username = username;
        this.permissions = permissions; //eg guest, employee, dev
    }

    //gets the username of User as a string
    public String getUsername() {
        return username;
    }

    //Sets the permission level of the user
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
