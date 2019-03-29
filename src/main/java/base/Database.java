package base;

import java.io.*;
import java.net.URL;
import java.sql.*;

import static java.lang.Integer.parseInt;

public final class Database {
    Connection connection;
    public Database(boolean importData) {
        System.out.println("Attempting to connect to the embedded database...");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Java DB Driver not found");
            e.printStackTrace();
            return;
        }
        System.out.println("Driver registered");
        try {
            this.connection = DriverManager.getConnection("jdbc:derby:TeamC;create=true");
        } catch (SQLException e) {
            System.err.println("Connection failed");
            e.printStackTrace();
            return;
        }
        System.out.println("Successfully connected to database");

        // ------------------------------------------------------------------------
        // Let's build a database.
        // table exists sql state => X0Y32
        if (importData) {
            // create nodes table
            String createNodesTable = "create table NODES (NODEID varchar(255) not null, XCOORD int, YCOORD int, FLOOR varchar(255), BUILDING varchar(255), NODETYPE varchar(255), LONGNAME varchar(255), SHORTNAME varchar(255))";
            String NodesTableUINDEX = "create unique index NODES_NODEID_uindex on NODES (NODEID)";
            String NodesTablePK = "alter table NODES add constraint NODES_pk primary key (NODEID)";
            // create edges table
            String createEdgesTable = "create table EDGES (EDGEID varchar(255) not null, STARTNODE varchar(255) not null constraint EDGES_NODES_STARTNODE_fk references NODES (NODEID) on update no action on delete cascade, ENDNODE varchar(255) not null constraint EDGES_NODES_ENDNODE_fk references NODES (NODEID) on update no action on delete cascade)";
            String EdgesTableUINDEX = "create unique index EDGES_EDGEID_uindex	on EDGES (EDGEID)";
            String EdgesTablePK = "alter table EDGES add constraint EDGES_pk primary key (EDGEID)";
            // create employees table
            String createEmployeesTable = "create table EMPLOYEES (USERNAME varchar(32) not null, PASSWORD varchar(1000), PERMISSIONS varchar(255))";
            String EmployeesTableUINDEX = "create unique index EMPLOYEES_USERNAME_uindex on EMPLOYEES (USERNAME)";
            String EmployeesTablePK = "alter table EMPLOYEES add constraint EMPLOYEES_pk primary key (USERNAME)";
            // create servicerequests table
            String createServiceRequestsTable = "create table SERVICEREQUESTS (ID int generated always as identity, NODEID varchar(255) not null constraint SERVICEREQUESTS_NODES_NODEID_fk references NODES (NODEID) on update no action on delete cascade, DESCRIPTION varchar(2000), DATETIMESUBMITTED timestamp, DATETIMECOMPLETED timestamp, USERCOMPLETEDBY varchar(32) constraint SERVICEREQUESTS_EMPLOYEES_USERNAME_fk references EMPLOYEES (USERNAME) on update no action on delete cascade)";
            String ServiceRequestsTableUINDEX = "create unique index SERVICEREQUESTS_ID_uindex on SERVICEREQUESTS (ID)";
            String ServiceRequestsTablePK = "alter table SERVICEREQUESTS add constraint SERVICEREQUESTS_pk primary key (ID)";
            // create bookings table
            String createBookingsTable = "create table BOOKINGS (ID int generated always as identity, LOCATION varchar(255) not null, DESCRIPTION varchar(2000), DATETIMESTART timestamp, DATETIMEEND timestamp, USERCOMPLETEDBY varchar(32) constraint BOOKINGS_EMPLOYEES_USERNAME_fk references EMPLOYEES (USERNAME) on update no action on delete cascade)";
            String BookingsTableUINDEX = "create unique index BOOKINGS_ID_uindex on BOOKINGS (ID)";
            String BookingsTablePK = "alter table BOOKINGS add constraint BOOKINGS_pk primary key (ID)";
            try {
                Statement tableStmt = this.getConnection().createStatement();
                tableStmt.executeUpdate(createNodesTable);
                tableStmt.executeUpdate(NodesTableUINDEX);
                tableStmt.executeUpdate(NodesTablePK);
                tableStmt.executeUpdate(createEdgesTable);
                tableStmt.executeUpdate(EdgesTableUINDEX);
                tableStmt.executeUpdate(EdgesTablePK);
                tableStmt.executeUpdate(createEmployeesTable);
                tableStmt.executeUpdate(EmployeesTableUINDEX);
                tableStmt.executeUpdate(EmployeesTablePK);
                tableStmt.executeUpdate(createServiceRequestsTable);
                tableStmt.executeUpdate(ServiceRequestsTableUINDEX);
                tableStmt.executeUpdate(ServiceRequestsTablePK);
                tableStmt.executeUpdate(createBookingsTable);
                tableStmt.executeUpdate(BookingsTableUINDEX);
                tableStmt.executeUpdate(BookingsTablePK);
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    // table exists
                    System.out.println("table exists");
                } else {
                    e.printStackTrace();
                }
            }

            // ----------------------------------------------------------------------------
            // read in data from CSVs to build the database

            // import nodes
            System.out.println("Attempting to import nodes from /data/nodesv2.csv...");
            URL csvFile = getClass().getResource("/data/nodesv2.csv");
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
                    String sqlCmd = "insert into NODES (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
                            sqlCmd = "update NODES set XCOORD = ?, YCOORD = ?, FLOOR = ?, BUILDING = ?, NODETYPE = ?, LONGNAME = ?, SHORTNAME = ? where NODEID = ?";
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

            // import edges

            System.out.println("Attempting to import edges from /data/edgesv2.csv...");
            csvFile = getClass().getResource("/data/edgesv2.csv");
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
                    String sqlCmd = "insert into EDGES (EDGEID, STARTNODE, ENDNODE) values (?, ?, ?)";
                    try {
                        ps = this.getConnection().prepareStatement(sqlCmd);
                        ps.setString(1, edgeID);
                        ps.setString(2, startNode);
                        ps.setString(3, endNode);
                        ps.execute();
                    } catch (SQLException e) {
                        if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert
                            sqlCmd = "update EDGES set STARTNODE = ?, ENDNODE = ? where EDGEID = ?";
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
    } // end constructor

    public Connection getConnection() {
        return this.connection;
    }
}
