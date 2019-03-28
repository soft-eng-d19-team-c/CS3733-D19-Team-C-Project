package base;

import java.io.*;
import java.net.URL;
import java.sql.*;

import static java.lang.Integer.parseInt;

public class Database {
    Connection connection;

    public Database() {
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

        try {
            // substitute your database name for myDB
            this.connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
        System.out.println("Java DB connection established!");


        /**
         * Create the tables :(
         */

// TODO do this better lol
        String createTablesSQL1 = "create table PrototypeNodes (NodeID varchar(255) not null, xCoord int, yCoord int, floor varchar(255),building varchar(255),nodeType varchar(255),longName varchar(255),shortName varchar(255))";
        String createTablesSQL2 = "create unique index PrototypeNodes_NodeID_uindex on PrototypeNodes (NodeID)";
        String createTablesSQL3 = "alter table PrototypeNodes add constraint PrototypeNodes_pk primary key (NodeID)";
        String createTablesSQL4 = "create table PROJECTCNODES(NODEID VARCHAR(255) not null, XCOORD INTEGER not null, YCOORD INTEGER not null, FLOOR VARCHAR(255),	BUILDING VARCHAR(255), NODETYPE VARCHAR(255), LONGNAME VARCHAR(255), SHORTNAME VARCHAR(255))";
        String createTablesSQL5 = "create unique index PROJECTCNODES_NODEID_UINDEX	on PROJECTCNODES (NODEID)";
        String createTablesSQL6 = "create unique index SQL190327025706380 on PROJECTCNODES (NODEID)";
        String createTablesSQL7 = "alter table PROJECTCNODES add constraint PROJECTCNODES_PK primary key (NODEID)";
        String createTablesSQL8 = "create table PROJECTCEDGES(EDGEID VARCHAR(255) not null,STARTNODE VARCHAR(255),ENDNODE VARCHAR(255))";
        String createTablesSQL9 = "create unique index PROJECTCEDGES_EDGEID_UINDEX on PROJECTCEDGES (EDGEID)";
        String createTablesSQL10 = "create unique index SQL190327213030080 on PROJECTCEDGES (EDGEID)";
        String createTablesSQL11 = "alter table PROJECTCEDGES add constraint PROJECTCEDGES_PK primary key (EDGEID)";
        try {
            Statement tableStmt = this.getConnection().createStatement();
            tableStmt.executeUpdate(createTablesSQL1);
            tableStmt.executeUpdate(createTablesSQL2);
            tableStmt.executeUpdate(createTablesSQL3);
            tableStmt.executeUpdate(createTablesSQL4);
            tableStmt.executeUpdate(createTablesSQL5);
            tableStmt.executeUpdate(createTablesSQL6);
            tableStmt.executeUpdate(createTablesSQL7);
            tableStmt.executeUpdate(createTablesSQL8);
            tableStmt.executeUpdate(createTablesSQL9);
            tableStmt.executeUpdate(createTablesSQL10);
            tableStmt.executeUpdate(createTablesSQL11);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /**
         * Read in data from CSV to database for prototype.
     *
     * This is a little messy by derby doesn't support MERGE statements or IF EXISTS statements the way I'm used to doing them.
     * We first try to insert the prototype node from the CSV.
     * If we get an error because there is a duplicate key, we just update those fields instead.
     */
        URL csvFile = getClass().getResource("/data/PrototypeNodes.csv");
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
                    ps = this.getConnection().prepareStatement(sqlCmd);
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
                            ps = this.getConnection().prepareStatement(sqlCmd);
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



        // lets duplicate some code haha ...  . help me

        csvFile = getClass().getResource("/data/ProjectCNodes.csv");
        br = null;
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
                String sqlCmd = "INSERT INTO PROJECTCNODES (NodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try {
                    ps = this.getConnection().prepareStatement(sqlCmd);
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
                        sqlCmd = "UPDATE PROJECTCNODES SET xcoord = ?, ycoord = ?, floor= ?, building= ?, nodeType = ?, longName = ?, shortName = ? WHERE NodeID = ?";
                        try {
                            ps = this.getConnection().prepareStatement(sqlCmd);
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

        csvFile = getClass().getResource("/data/ProjectCEdges.csv");
        br = null;
        try {
            br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
            br.readLine(); // throw away header
            while ((line = br.readLine()) != null) {
                String[] edgeData = line.split(cvsSplitBy); // split by comma
                // get fields
                String edgeID = edgeData[0];
                String startNode = edgeData[1];
                String endNode = edgeData[2];
                // prepare the insert sql statement with room to insert variables
                PreparedStatement ps = null;
                String sqlCmd = "INSERT INTO PROJECTCEDGES (EDGEID, STARTNODE, ENDNODE) VALUES (?, ?, ?)";
                try {
                    ps = this.getConnection().prepareStatement(sqlCmd);
                    ps.setString(1, edgeID);
                    ps.setString(2, startNode);
                    ps.setString(3, endNode);
                    ps.execute();
                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert
                        sqlCmd = "UPDATE PROJECTCEDGES SET STARTNODE = ?, ENDNODE = ? WHERE EDGEID = ?";
                        try {
                            ps = this.getConnection().prepareStatement(sqlCmd);
                            ps.setString(1, startNode);
                            ps.setString(2, endNode);
                            ps.setString(3, edgeID);
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




    }

    public Connection getConnection() {
        return this.connection;
    }
}
