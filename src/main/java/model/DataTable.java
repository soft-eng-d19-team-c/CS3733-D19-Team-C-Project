package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DataTable {
    private ObservableList<Node> data;
    private Connection connection;

    public DataTable() {
        this.data = FXCollections.observableArrayList();
//        this.data = FXCollections.observableArrayList(
//                new Node("id1", 1, 2),
//                new Node("New ID", 1242, 21)
//        );
        try {
            this.connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println(getDataById("BHALL00802"));
    }

    public ObservableList<Node> getAllData() {

//        //Node temp = new Node();
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM PROTOTYPENODES";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                Node temp = parseResultSet(rs);
                //System.out.println(ID);

                this.data.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean setData(Node node) {


        try {
            Statement stmt = connection.createStatement();
            String str = "UPDATE PROTOTYPENODES SET field=XCOORD WHERE ID=" + node.getID();
            return stmt.execute(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Node getDataById(String ID) {

        try {

            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM PROTOTYPENODES WHERE NODEID=" + ID;
            ResultSet rs = stmt.executeQuery(str);
            return parseResultSet(rs);



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Node parseResultSet(ResultSet rs){

        try {
            String ID = rs.getString("NodeID");
            int x = rs.getInt("xcoord");
            int y = rs.getInt("ycoord");
            String floor = rs.getString("floor");
            String building = rs.getString("building");
            String nodeType = rs.getString("nodetype");
            String longName = rs.getString("longname");
            String shortName = rs.getString("shortname");
            return new Node(ID, x, y, floor, building, nodeType, longName, shortName);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;


    }

}
