package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;

public class DataTable {
    private ObservableList<Node> data;
    private Connection connection;

    public DataTable() {
        this.data = FXCollections.observableArrayList();

        try {
            this.connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
 // ~~~~~~~~~~~~~~~~Tests used ~~~~~~~~~~~~~~~~~~~~
        /*
        System.out.println(getDataById("BHALL00802"));
        Node temp = getDataById("BHALL00802");
        temp.setX(-10);
        temp.setY(-20);
        temp.setBuilding("building asdklfsdklfhaskldfj1");
        temp.setFloor("floor 2asd.,fsad;lf");
        temp.setNodeType("node type teasd,jfsadj.fhas.dst");
        temp.setShortName("short nameasd.,fn,asd.fn");
        temp.setLongName("LOOOOOOOOOONG nasad.fsdafjme");
        temp.update();
        System.out.println(getDataById("BHALL00802"));

        printToCsv();
        */
    }

    // Goes through the database and collects all of the data
    public ObservableList<Node> getAllData() {
        //Node temp = new Node();
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM PROTOTYPENODES";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                Node temp = ResultSetToNode(rs);
                //System.out.println(ID);

                this.data.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public LinkedList<Node> getProjectCNodesByFloor(String floor){
        LinkedList<Node> list = new LinkedList<>();
        try {

            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM PROJECTCNODES WHERE FLOOR = '"+floor+"'";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                Node temp = ResultSetToNode(rs);
                    list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Node getDataById(String ID) {

        try {
            //Statement stmt = connection.createStatement();
            String str = "SELECT * FROM PROTOTYPENODES WHERE NODEID = ?";
            PreparedStatement ps = connection.prepareStatement(str);
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return ResultSetToNode(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // This function is used to parse result sets into a node
    private Node ResultSetToNode(ResultSet rs){

        try {
            String ID = rs.getString("NodeID");
            int x = rs.getInt("xcoord");
            int y = rs.getInt("ycoord");
            String floor = rs.getString("floor");
            String building = rs.getString("building");
            String nodeType = rs.getString("nodetype");
            String longName = rs.getString("longname");
            String shortName = rs.getString("shortname");
            return new Node(ID, x, y, floor, building, nodeType, longName, shortName, connection);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // This function print a csv file of the prototypenodes table to the current directory
    // Returns true if it worked and false otherwise
    // This works by building a large string and then writing it to a file
    public boolean printToCsv() {
        ObservableList<Node> nodes = getAllData();
        String fileName = "prototypenodes.csv";

        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            StringBuilder sb = new StringBuilder();
            // add headers

            sb.append("nodeid,");
            sb.append("xcoord,");
            sb.append("ycoord,");
            sb.append("floor,");
            sb.append("building,");
            sb.append("nodetype,");
            sb.append("longname,");
            sb.append("shortname\n");

            // add data for each node
            for (Node n : nodes) {
                sb.append(n.getID() + ",");
                sb.append(n.getX() + ",");
                sb.append(n.getY() + ",");
                sb.append(n.getFloor() + ",");
                sb.append(n.getBuilding() + ",");
                sb.append(n.getNodeType() + ",");
                sb.append(n.getLongName() + ",");
                sb.append(n.getShortName() + "\n");
            }

            writer.write(sb.toString());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // print header
        // print each node


        return false;
    }

}
