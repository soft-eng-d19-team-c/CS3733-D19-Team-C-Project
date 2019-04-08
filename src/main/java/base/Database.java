package base;

import java.io.*;
import java.net.URL;
import java.sql.*;

import static java.lang.Integer.parseInt;

/**
 * This class is to store the database connection as well as
 * build the database the first time the application is run.
 * @author Ryan LaMarche
 */
public final class Database {
    Connection connection;
    public Database() {
        this(false, false);
    }
    public Database(boolean importData, boolean overwriteData) {
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
            String createServiceRequestsTable = "create table SERVICEREQUESTS (ID int generated always as identity, NODEID varchar(255) not null constraint SERVICEREQUESTS_NODES_NODEID_fk references NODES (NODEID) on update no action on delete cascade, DESCRIPTION varchar(2000), TYPE varchar(255), DATETIMESUBMITTED timestamp, DATETIMECOMPLETED timestamp, USERCOMPLETEDBY varchar(32) constraint SERVICEREQUESTS_EMPLOYEES_USERNAME_fk references EMPLOYEES (USERNAME) on update no action on delete cascade)";
            String ServiceRequestsTableUINDEX = "create unique index SERVICEREQUESTS_ID_uindex on SERVICEREQUESTS (ID)";
            String ServiceRequestsTablePK = "alter table SERVICEREQUESTS add constraint SERVICEREQUESTS_pk primary key (ID)";
            // create table of possible locations to book
            String createBookingLocationsTable = "create table BOOKINGLOCATIONS(ID varchar(255) not null, TYPE varchar(255), TITLE varchar(255))";
            String BookingLocationsTableUINDEX = "create unique index BOOKINGLOCATIONS_ID_uindex on BOOKINGLOCATIONS (ID)";
            String BookingLocationsTablePK = "alter table BOOKINGLOCATIONS add constraint BOOKINGLOCATIONS_pk primary key (ID)";
            //create ITServiceRequestsTable
            String createITServiceRequestsTable = "create table ITSERVICEREQUESTS(ID int generated always as identity, type varchar(255), description varchar(2000), dateTimeSubmitted Timestamp, dateTimeCompleted Timestamp, userRequestedBy varchar(64) constraint ITSERVICEREQUESTS_USERS_USERNAME_fk references USERS (USERNAME) on update no action on delete no action, userCompletedBy varchar(64) constraint ITSERVICEREQUESTS_USERS_USERNAME_fk2 references USERS (USERNAME) on update no action on delete no action, nodeID varchar(255) constraint ITSERVICEREQUESTS_NODES_NODEID_fk references NODES(NODEID) on update no action on delete no action)";
            String ITServiceRequestsTableUINDEX = "create unique index ITSERVICEREQUESTS_ID_uindex on ITSERVICEREQUESTS (ID)";
            String ITServiceRequestsTablePK = "alter table ITSERVICEREQUESTS add constraint ITSERVICEREQUESTS_pk primary key (ID)";
            // create bookings table
            String createBookingsTable = "create table BOOKINGS (ID int generated always as identity, LOCATION varchar(255) not null constraint BOOKING_BOOKINGLOCATIONS_ID_fk references BOOKINGLOCATIONS (ID) on update no action on delete cascade, DESCRIPTION varchar(2000), DATETIMESTART timestamp, DATETIMEEND timestamp, USERCOMPLETEDBY varchar(32) constraint BOOKINGS_EMPLOYEES_USERNAME_fk references EMPLOYEES (USERNAME) on update no action on delete cascade)";
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
                tableStmt.executeUpdate(createBookingLocationsTable);
                tableStmt.executeUpdate(BookingLocationsTableUINDEX);
                tableStmt.executeUpdate(BookingLocationsTablePK);
                tableStmt.executeUpdate(createBookingsTable);
                tableStmt.executeUpdate(BookingsTableUINDEX);
                tableStmt.executeUpdate(BookingsTablePK);
                tableStmt.executeUpdate(createITServiceRequestsTable);
                tableStmt.executeUpdate(ITServiceRequestsTableUINDEX);
                tableStmt.executeUpdate(ITServiceRequestsTablePK);
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    // table exists
                    System.out.println("Database schema already exists.");
                } else {
                    e.printStackTrace();
                }
            }

            // ----------------------------------------------------------------------------
            // read in data from CSVs to build the database

            // import nodes
            System.out.println("Attempting to import nodes from /data/nodes.csv...");
            URL csvFile = getClass().getResource("/data/nodes.csv");
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
                            if (overwriteData) {
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
            System.out.println("Attempting to import edges from /data/edges.csv...");
            csvFile = getClass().getResource("/data/edges.csv");
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
                            if (overwriteData) {
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

            // import bookingLocations
            System.out.println("Attempting to import booking locations from /data/booking_locations.csv...");
            csvFile = getClass().getResource("/data/booking_locations.csv");
            try {
                br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
                br.readLine(); // throw away header
                while ((line = br.readLine()) != null) {
                    String[] bookingLocationData = line.split(cvsSplitBy); // split by comma
                    // get fields
                    String bookingLocationID = bookingLocationData[0];
                    String type = bookingLocationData[1];
                    String title = bookingLocationData[2];
                    // prepare the insert sql statement with room to insert variables
                    PreparedStatement ps = null;
                    String sqlCmd = "insert into BOOKINGLOCATIONS (ID, TYPE, TITLE) values (?, ?, ?)";
                    try {
                        ps = this.getConnection().prepareStatement(sqlCmd);
                        ps.setString(1, bookingLocationID);
                        ps.setString(2, type);
                        ps.setString(3, title);
                        ps.execute();
                    } catch (SQLException e) {
                        if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert
                            if (overwriteData) {
                                sqlCmd = "update BOOKINGLOCATIONS set TYPE = ?, TITLE = ? where ID = ?";
                                try {
                                    ps = this.getConnection().prepareStatement(sqlCmd);
                                    ps.setString(1, type);
                                    ps.setString(2, title);
                                    ps.setString(3, bookingLocationID);
                                    ps.executeUpdate();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
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

            String sqlCmd = "insert into EMPLOYEES (USERNAME, PASSWORD, PERMISSIONS) values (?, ?, ?)";
            try {
                PreparedStatement ps = this.getConnection().prepareStatement(sqlCmd);
                ps.setString(1, "username@example.com");
                ps.setString(2, null);
                ps.setString(3, "developer");
                ps.execute();
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    if (overwriteData) {
                        sqlCmd = "update EMPLOYEES set PASSWORD = ?, PERMISSIONS = ? where USERNAME = ?";
                        try {
                            PreparedStatement ps = this.getConnection().prepareStatement(sqlCmd);
                            ps.setString(1, "username@example.com");
                            ps.setString(2, null);
                            ps.setString(3, "developer");
                            ps.executeUpdate();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

        } // end if importData
    } // end constructor

    public Connection getConnection() {
        return this.connection;
    }
}
