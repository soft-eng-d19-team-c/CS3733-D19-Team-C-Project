package model;

import base.Database;
import base.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserTest {
    Database db;


    @Before
    public void setUp() throws Exception {
        db = new Database(false, false);
        String sqlStr = "select * from USERHASPERMISSIONS LEFT JOIN USERPERMISSIONS ON USERHASPERMISSIONS.PERMISSIONS = USERPERMISSIONS.PERMISSIONS INNER JOIN USERS ON USERHASPERMISSIONS.USERNAME = USERS.USERNAME;";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlStr);
//            ps.setString();
        }

        catch (SQLException e){

            e.printStackTrace();

        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUsername() {
    }

    @Test
    public void checkPermissions() {
    }

    @Test
    public void logout() {
    }
}