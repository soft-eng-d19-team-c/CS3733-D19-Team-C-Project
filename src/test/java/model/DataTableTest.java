package model;

import base.Database;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataTableTest {
    Database db;
    DataTable dt;
    ResultSet rs;

    @Before
    public void setUp() throws Exception {
         db = new Database(true);
         Statement stmt = db.getConnection().createStatement();
         String str = "SELECT * FROM NODES";
         rs = stmt.executeQuery(str);
         dt = new DataTable();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllData() throws SQLException {
        ObservableList<Node> ObsL = dt.getAllNodeData(); // getting data from the database built in main
    }

    @Test
    public void getProjectCNodesByFloor() {
    }

    @Test
    public void getDataById() {
    }

    @Test
    public void printToCsv() {
    }

    @Test
    public void getProjectCEdges() {
    }
}