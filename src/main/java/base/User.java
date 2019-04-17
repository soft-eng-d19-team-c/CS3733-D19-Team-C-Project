package base;

//A user of the kiosk system

import model.AuthException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class User {
    private String username;
    private LinkedList<String> permissions;

    /**
     * @author Ryan LaMarche
     * Create a user object with NO authentication or permissions
     * @param username Username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * @author Ryan LaMarche
     * Create a new authenticated user
     * @param username username
     * @param password password in plain text
     * @throws AuthException invalid username/password combination
     */
    public User(String username, String password) throws AuthException {
        String sqlStr = "select USERS.USERNAME, USERS.PASSWORD, USERPERMISSIONS.PERMISSIONS from USERS LEFT JOIN USERHASPERMISSIONS ON USERHASPERMISSIONS.USERNAME = USERS.USERNAME LEFT JOIN USERPERMISSIONS ON USERHASPERMISSIONS.PERMISSIONS = USERPERMISSIONS.PERMISSIONS where USERS.USERNAME = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {    // make sure there is at least one row so we can validate password
                if (Main.auth.checkPassword(password, rs.getString("PASSWORD"))) {
                    this.username = username;
                    this.permissions = new LinkedList<>();
                    this.permissions.add(rs.getString("PERMISSIONS"));
                    while (rs.next()) {
                        this.permissions.add(rs.getString("PERMISSIONS"));
                    }
                } else {
                    throw new AuthException("Incorrect username / password combination.");
                }

            } else {
                throw new AuthException("Incorrect username / password combination.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //gets the username of User as a string
    public String getUsername() {
        return username;
    }

    public boolean checkPermissions(String permission) {
        permission = permission.toUpperCase();
        return this.permissions != null && this.permissions.contains(permission);
    }

    public void logout() {
        Main.screenController.clearHistory();
        try {
            Main.user = new User("guest", "guest");
        } catch (AuthException e) {
            e.printStackTrace();
        }
    }
}
