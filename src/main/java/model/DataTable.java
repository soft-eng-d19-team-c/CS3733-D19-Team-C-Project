package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DataTable {
    private ObservableList<Node> data;
    private Connection connection;

    public DataTable() {
        this.data = FXCollections.observableArrayList(
                new Node("id1", 1, 2),
                new Node("New ID", 1242, 21)
        );
        try {
            this.connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Node> getAllData() {
//        try {
//            Statement stmt = connection.createStatement();
//            String str = "SELECT * FROM PrototypeNodes";
//            ResultSet rs = stmt.executeQuery(str);
//            while(rs.next()) {
//                String ID = rs.getString("NodeID");
//                int x = rs.getInt("xcoord");
//                int y = rs.getInt("ycoord");
//
////                this.data.add();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return data;
    }

    public boolean setData(String[] data) {
        try {
            Statement stmt = connection.createStatement();
            String str = "UPDATE PROTOTYPENODES SET field=filedData WHERE ID="+ data[0];
            boolean rs = stmt.execute(str);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Node getDataById(String ID) {
        return null;
    }

}
