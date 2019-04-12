package model;

//A user of the kiosk system

import base.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class User {
    private String username;
    private LinkedList<String> permissions;

    public User(String username) {
        this.username = username;
        this.permissions = new LinkedList<>();
        String sqlStr = "select USERHASPERMISSIONS.USERNAME, USERPERMISSIONS.PERMISSIONS from USERHASPERMISSIONS LEFT JOIN USERPERMISSIONS ON USERHASPERMISSIONS.PERMISSIONS = USERPERMISSIONS.PERMISSIONS where USERNAME = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.permissions.add(rs.getString("PERMISSIONS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //gets the username of User as a string
    public String getUsername() {
        return username;
    }

    public LinkedList<String> getPermissions() {
        return this.permissions;
    }

    public void tryLogin(String username, String password) {
        String sqlStr = "select * from USERS where USERNAME = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pwd = rs.getString("password");
                if (password.equals(pwd)) {
                    Main.user = new User(username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        Main.user = new User("guest");
    }
}
