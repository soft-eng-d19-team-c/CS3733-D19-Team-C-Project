import java.io.*;
import java.net.URL;
import java.sql.*;

import static java.lang.Integer.parseInt;

/**
 * @author Ryan LaMarche.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("-------Embedded Java DB Connection Testing --------");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            System.out.println("For IntelliJ do the following:");
            System.out.println("File | Project Structure, Modules, Dependency tab");
            System.out.println("Add by clicking on the green plus icon on the right of the window");
            System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.out.println("Select the folder java/jdk1.8.xxx/db/lib where xxx is the version.");
            System.out.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

        System.out.println("Java DB driver registered!");
        Connection connection = null;

        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
        System.out.println("Java DB connection established!");
        /**
         * read in data from CSV to the database
         */
        URL csvFile = Main.class.getResource("/data/PrototypeNodes.csv");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
            br.readLine(); // throw away header
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] nodeData = line.split(cvsSplitBy);
                String nodeID = nodeData[0];
                int xcoord = parseInt(nodeData[1]);
                int ycoord = parseInt(nodeData[2]);
                String floor = nodeData[3];
                String building = nodeData[4];
                String nodeType = nodeData[5];
                String longName = nodeData[6];
                String shortName = nodeData[7];
                Statement stmt = null;
                PreparedStatement ps = null;
                try {
                    stmt = connection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String sqlCmd = " MERGE INTO PrototypeNodes AS target USING Nodes AS source ON target.NodeID = ?"+
                                " WHEN MATCHED THEN"+
                                    " UPDATE SET NodeID = ?, xcoord = ?, ycoord = ?, floor= ?, building= ?, nodeType = ?, longName = ?, shortName = ?"
                                +" WHEN NOT MATCHED THEN" +
                                    " INSERT (NodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
                try {
                    ps = connection.prepareStatement(sqlCmd);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ps.setString(1, nodeID);
                    ps.setString(2, nodeID);
                    ps.setInt(3, xcoord);
                    ps.setInt(4, ycoord);
                    ps.setString(5, floor);
                    ps.setString(6, building);
                    ps.setString(7, nodeType);
                    ps.setString(8, longName);
                    ps.setString(9, shortName);
                    ps.setString(10, nodeID);
                    ps.setInt(11, xcoord);
                    ps.setInt(12, ycoord);
                    ps.setString(13, floor);
                    ps.setString(14, building);
                    ps.setString(15, nodeType);
                    ps.setString(16, longName);
                    ps.setString(17, shortName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ps.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // creates new MainFXML object that loads main.fxml on a new thread, starting at its main method.
//        MainFXML app = new MainFXML();
//        app.main(args);

    }
}
