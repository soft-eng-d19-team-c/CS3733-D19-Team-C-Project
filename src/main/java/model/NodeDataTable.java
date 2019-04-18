package model;

import base.Database;
import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NodeDataTable {
    private ObservableList<Node> nodeData;

    public NodeDataTable() {
        this.nodeData = FXCollections.observableArrayList();
    }

    // Goes through the database and collects all of the nodeData
    public ObservableList<Node> getAllNodeData() {
        this.nodeData = FXCollections.observableArrayList();
        try {
            Statement stmt = Database.getConnection().createStatement();
            String str = "SELECT * FROM NODES";
            ResultSet rs = stmt.executeQuery(str);

            while(rs.next()) {
                Node temp = ResultSetToNode(rs);
                this.nodeData.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nodeData;
    }

    public Node getDataById(String ID) {
        try {
            //Statement stmt = connection.createStatement();
            String str = "SELECT * FROM NODES WHERE NODEID = ?";
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
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
            return new Node(ID, x, y, floor, building, nodeType, longName, shortName);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }// This function is used to parse result sets into a edge

    // This function print a csv file of the prototypenodes table to the current directory
    // Returns true if it worked and false otherwise
    // This works by building a large string and then writing it to a file
    public boolean printToCsv() {
        ObservableList<Node> nodes = getAllNodeData();
        String fileName = "nodes.csv";

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

            // add nodeData for each node
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
        return false;
    }
}
