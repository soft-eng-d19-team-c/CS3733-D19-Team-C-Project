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
         * Read in data from CSV to database for prototype.
         *
         * This is a little messy by derby doesn't support MERGE statements or IF EXISTS statements the way I'm used to doing them.
         * We first try to insert the prototype node from the CSV.
         * If we get an error because there is a duplicate key, we just update those fields instead.
         */
        URL csvFile = Main.class.getResource("/data/PrototypeNodes.csv");
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
            br.readLine(); // throw away header
            while ((line = br.readLine()) != null) {
                String[] nodeData = line.split(cvsSplitBy); // split by comma
                // get fields
                String nodeID = nodeData[0];
                int xcoord = parseInt(nodeData[1]);
                int ycoord = parseInt(nodeData[2]);
                String floor = nodeData[3];
                String building = nodeData[4];
                String nodeType = nodeData[5];
                String longName = nodeData[6];
                String shortName = nodeData[7];
                // prepare the insert sql statement with room to insert variables
                PreparedStatement ps = null;
                String sqlCmd = "INSERT INTO PROTOTYPENODES (NodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    ps = connection.prepareStatement(sqlCmd);
                    ps.setString(1, nodeID);
                    ps.setInt(2, xcoord);
                    ps.setInt(3, ycoord);
                    ps.setString(4, floor);
                    ps.setString(5, building);
                    ps.setString(6, nodeType);
                    ps.setString(7, longName);
                    ps.setString(8, shortName);
                    ps.execute();
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert
                        sqlCmd = "UPDATE PrototypeNodes SET xcoord = ?, ycoord = ?, floor= ?, building= ?, nodeType = ?, longName = ?, shortName = ? WHERE NodeID = ?";
                        try {
                            ps = connection.prepareStatement(sqlCmd);
                            ps.setInt(1, xcoord);
                            ps.setInt(2, ycoord);
                            ps.setString(3, floor);
                            ps.setString(4, building);
                            ps.setString(5, nodeType);
                            ps.setString(6, longName);
                            ps.setString(7, shortName);
                            ps.setString(8, nodeID);
                            ps.executeUpdate();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        e.printStackTrace();
                    }
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
        MainFXML app = new MainFXML();
        app.main(args);

    }
}
