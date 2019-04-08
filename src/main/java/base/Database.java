package base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
            String createUsersTable = "create table USERS (username varchar(64) not null, password varchar(1000))";
            String UsersTableUINDEX = "create unique index USERS_username_uindex on USERS (username)";
            String UsersTablePK = "alter table USERS add constraint USERS_pk primary key (username)";
            // create servicerequests table
            String createServiceRequestsTable = "create table SERVICEREQUESTS (ID int generated always as identity, NODEID varchar(255) not null constraint SERVICEREQUESTS_NODES_NODEID_fk references NODES (NODEID) on update no action on delete cascade, DESCRIPTION varchar(2000), TYPE varchar(255), DATETIMESUBMITTED timestamp, DATETIMECOMPLETED timestamp, USERCOMPLETEDBY varchar(64) constraint SERVICEREQUESTS_USERS_USERNAME_fk references USERS (USERNAME) on update no action on delete cascade)";
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
            String createBookingsTable = "create table BOOKINGS (ID int generated always as identity, LOCATION varchar(255) not null constraint BOOKING_BOOKINGLOCATIONS_ID_fk references BOOKINGLOCATIONS (ID) on update no action on delete cascade, DESCRIPTION varchar(2000), DATETIMESTART timestamp, DATETIMEEND timestamp, USERCOMPLETEDBY varchar(64) constraint BOOKINGS_USERS_USERNAME_fk references USERS (USERNAME) on update no action on delete cascade)";
            String BookingsTableUINDEX = "create unique index BOOKINGS_ID_uindex on BOOKINGS (ID)";
            String BookingsTablePK = "alter table BOOKINGS add constraint BOOKINGS_pk primary key (ID)";
            //create security requests
            String createSecurityRequestsTable = "create table SECURITYREQUESTS(ID int generated always as identity, ISURGENT BOOLEAN not null,LOCATION VARCHAR(255) not null constraint SECURITYREQUESTS_NODES_NODEID_FK references NODES (NODEID) on delete cascade, DESCRIPTION VARCHAR(1000), TIMESUBMITTED TIMESTAMP not null, TIMECOMPLETED TIMESTAMP, REQUESTEDBY VARCHAR(64) constraint SECURITYREQUESTS_USERS_USERNAME_fk references USERS (USERNAME), COMPLETEDBY VARCHAR(64) constraint SECURITYREQUESTS_USERS_USERNAME_fk2 references USERS (USERNAME))";
            String SecurityRequestsTableUIndex = "create unique index SECURITYREQUESTS_ID_UINDEX on SECURITYREQUESTS (ID)";
            String SecurityRequestsTablePK = "alter table SECURITYREQUESTS add constraint SECURITYREQUESTS_PK primary key (ID)";
            // create florist service requests table
            String createFloristRequestsTable = "create table FLORISTSERVICEREQUESTS(ID int generated always as identity, STARTNODEID varchar(255) constraint FLORISTSERVICEREQUESTS_NODES_NODEID_fk references NODES, ENDNODEID varchar(255) constraint FLORISTSERVICEREQUESTS_NODES_NODEID_fk_2 references NODES(NODEID), DATETIMESUBMITTED timestamp, DATETIMERESOLVED timestamp, USERRESOLVEDBY varchar(64) constraint FLORISTSERVICEREQUESTS_USERS_USERNAME_fk references USERS (USERNAME), DESCRIPTION varchar(1000))";
            String FloristRequestsTableUINDEX = "create unique index FLORISTSERVICEREQUESTS_ID_uindex on FLORISTSERVICEREQUESTS (ID)";
            String FloristRequestsTablePK = "alter table FLORISTSERVICEREQUESTS add constraint FLORISTSERVICEREQUESTS_pk primary key (ID)";

            // creating prescription services table
            String createPrescriptionServiceTable = "create table PRESCRIPTIONSERVICE (ID int generated always as identity, PATIENTID VARCHAR(255), REQUESTERID varchar(255), RESOLVERID varchar(255), DRUG varchar(255), TIMEORDERED TIMESTAMP, TIMEDELIVERED TIMESTAMP)";
            String PrescriptionUINDEX = "create unique index PRESCRIPTIONSERVICE_ID_uindex on PRESCRIPTIONSERVICE (ID)";
            String PrescriptionTablePK = "alter table PRESCRIPTIONSERVICE add constraint PRESCRIPTIONSERVICE_pk primary key (ID)";

            // create internal transportation service request
            String createInternalTransportationServiceRequestTable = "create table INTERNALTRANSPORTATION(ID int generated always as identity, NODEID VARCHAR(255) not null constraint INTERNALTRANSPORTATION_NODES_NODEID_fk references NODES (NODEID) on update no action on delete no action, NODEIDDEST VARCHAR(255) not null constraint INTERNALTRANSPORTATION_NODES_NODEIDDEST_fk references NODES (NODEID) on update no action on delete no action, DESCRIPTION VARCHAR(2000), DATETIMESUBMITTED DATE not null, DATETIMERESOLVED DATE, ISCOMPLETE BOOLEAN  not null, REQUESTEDBY VARCHAR(255), COMPLETEDBY VARCHAR(64) references USERS (USERNAME) on update no action on delete cascade)";
            String InternalTransportationServiceRequestTableUINDEX = "create unique index INTERNALTRANSPORTATION_ID_UINDEX on INTERNALTRANSPORTATION (ID)";
            String InternalTransportationServiceRequestTablePK = " alter table INTERNALTRANSPORTATION add constraint INTERNALTRANSPORTATION_PK primary key (ID)";
            // create user permissions
            String createUserPermissionsTable = "create table USERPERMISSIONS(PERMISSIONS varchar(255) not null)";
            String UserPermissionsTableUINDEX = "create unique index USERPERMISSIONS_PERMISSIONS_uindex on USERPERMISSIONS (PERMISSIONS)";
            String UserPermissionsTablePK = "alter table USERPERMISSIONS add constraint USERPERMISSIONS_pk primary key (PERMISSIONS)";
            // create user has permissions
            String createUserHasPermissionsTable = "create table USERHASPERMISSIONS(ID int generated always as identity, USERNAME varchar(64) not null constraint USERHASPERMISSIONS_USERS_USERNAME_fk references USERS (USERNAME) on delete cascade, PERMISSIONS varchar(255) not null constraint USERHASPERMISSIONS_PERMISSIONS_PERMISSIONS_fk references USERPERMISSIONS (PERMISSIONS) on delete cascade)";
            String UserHasPermissionsTableUINDEX = "create unique index USERHASPERMISSIONS_ID_uindex on USERHASPERMISSIONS (ID)";
            String UserHasPermissionsTablePK = "alter table USERHASPERMISSIONS add constraint USERHASPERMISSIONS_pk primary key (ID)";

            // SERVICE REQUESTS
            String createExternalTransportationRequestTable = "create table EXTERNALTRANSPORTATIONREQUESTS(ID int generated always as identity, PICKUPLOCATION varchar(255) not null constraint EXTERNALTRANSPORTATIONREQUESTS_NODES_NODEID_fk references NODES (NODEID) on delete no action, DESTINATION varchar(1000) not null, DATETIMESUBMITTED timestamp, DATETIMEPICKUP timestamp, DATETIMERESOLVED timestamp, USERCOMPLETEDBY varchar(64) constraint EXTERNALTRANSPORTATIONREQUESTS_USERS_USERNAME_fk references USERS (USERNAME) on update no action on delete cascade)";
            String ExternalTransportationRequestTableUINDEX = "create unique index EXTERNALTRANSPORTATIONREQUESTS_ID_uindex on USERHASPERMISSIONS (ID)";
            String ExternalTransportationRequestTablePK = "alter table EXTERNALTRANSPORTATIONREQUESTS add constraint EXTERNALTRANSPORTATIONREQUESTS_pk primary key (ID)";

            try {
                Statement tableStmt = this.getConnection().createStatement();
                tableStmt.executeUpdate(createPrescriptionServiceTable);
                tableStmt.executeUpdate(PrescriptionUINDEX);
                tableStmt.executeUpdate(PrescriptionTablePK);
                tableStmt.executeUpdate(createNodesTable);
                tableStmt.executeUpdate(NodesTableUINDEX);
                tableStmt.executeUpdate(NodesTablePK);
                tableStmt.executeUpdate(createEdgesTable);
                tableStmt.executeUpdate(EdgesTableUINDEX);
                tableStmt.executeUpdate(EdgesTablePK);
                tableStmt.executeUpdate(createUsersTable);
                tableStmt.executeUpdate(UsersTableUINDEX);
                tableStmt.executeUpdate(UsersTablePK);
                tableStmt.executeUpdate(createUserPermissionsTable);
                tableStmt.executeUpdate(UserPermissionsTableUINDEX);
                tableStmt.executeUpdate(UserPermissionsTablePK);
                tableStmt.executeUpdate(createUserHasPermissionsTable);
                tableStmt.executeUpdate(UserHasPermissionsTableUINDEX);
                tableStmt.executeUpdate(UserHasPermissionsTablePK);
                tableStmt.executeUpdate(createServiceRequestsTable);
                tableStmt.executeUpdate(ServiceRequestsTableUINDEX);
                tableStmt.executeUpdate(ServiceRequestsTablePK);
                tableStmt.executeUpdate(createBookingLocationsTable);
                tableStmt.executeUpdate(BookingLocationsTableUINDEX);
                tableStmt.executeUpdate(BookingLocationsTablePK);
                tableStmt.executeUpdate(createBookingsTable);
                tableStmt.executeUpdate(BookingsTableUINDEX);
                tableStmt.executeUpdate(BookingsTablePK);
                tableStmt.executeUpdate(createSecurityRequestsTable);
                tableStmt.executeUpdate(SecurityRequestsTableUIndex);
                tableStmt.executeUpdate(SecurityRequestsTablePK);
                tableStmt.executeUpdate(createITServiceRequestsTable);
                tableStmt.executeUpdate(ITServiceRequestsTableUINDEX);
                tableStmt.executeUpdate(ITServiceRequestsTablePK);
                tableStmt.executeUpdate(createFloristRequestsTable);
                tableStmt.executeUpdate(FloristRequestsTableUINDEX);
                tableStmt.executeUpdate(FloristRequestsTablePK);
                tableStmt.executeUpdate(createInternalTransportationServiceRequestTable);
                tableStmt.executeUpdate(InternalTransportationServiceRequestTableUINDEX);
                tableStmt.executeUpdate(InternalTransportationServiceRequestTablePK);
                tableStmt.executeUpdate(createExternalTransportationRequestTable);
                tableStmt.executeUpdate(ExternalTransportationRequestTableUINDEX);
                tableStmt.executeUpdate(ExternalTransportationRequestTablePK);

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


            // import user permissions
            System.out.println("Attempting to import user permissions from /data/user_permissions.csv...");
            csvFile = getClass().getResource("/data/user_permissions.csv");
            try {
                br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
                br.readLine(); // throw away header
                while ((line = br.readLine()) != null) {
                    String[] userPermissionsData = line.split(cvsSplitBy); // split by comma
                    // get fields
                    String permissions = userPermissionsData[0];
                    // prepare the insert sql statement with room to insert variables
                    PreparedStatement ps = null;
                    String sqlCmd = "insert into USERPERMISSIONS (PERMISSIONS) values (?)";
                    try {
                        ps = this.getConnection().prepareStatement(sqlCmd);
                        ps.setString(1, permissions);
                        ps.execute();
                    } catch (SQLException e) {
                        if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert

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

            // import users
            System.out.println("Attempting to import users from /data/users.csv...");
            csvFile = getClass().getResource("/data/users.csv");
            try {
                br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
                br.readLine(); // throw away header
                while ((line = br.readLine()) != null) {
                    String[] user = line.split(cvsSplitBy); // split by comma
                    // get fields
                    String username = user[0];
                    String password = user[1];
                    // prepare the insert sql statement with room to insert variables
                    PreparedStatement ps = null;
                    String sqlCmd = "insert into USERS (USERNAME, PASSWORD) values (?, ?)";
                    try {
                        ps = this.getConnection().prepareStatement(sqlCmd);
                        ps.setString(1, username);
                        ps.setString(2, password);
                        ps.execute();
                    } catch (SQLException e) {
                        if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert

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

            // import users has permissions
            System.out.println("Attempting to import users' permissions from /data/user_has_permissions.csv...");
            csvFile = getClass().getResource("/data/user_has_permissions.csv");
            try {
                br = new BufferedReader(new InputStreamReader(csvFile.openStream()));
                br.readLine(); // throw away header
                while ((line = br.readLine()) != null) {
                    String[] userHasPermissions = line.split(cvsSplitBy); // split by comma
                    // get fields
                    String username = userHasPermissions[0];
                    String permissions = userHasPermissions[1];
                    // prepare the insert sql statement with room to insert variables
                    PreparedStatement ps = null;
                    String sqlCmd = "update USERHASPERMISSIONS set USERNAME = ?, PERMISSIONS = ? where USERNAME = ? and PERMISSIONS = ?";
                    try {
                        ps = this.getConnection().prepareStatement(sqlCmd);
                        ps.setString(1, username);
                        ps.setString(2, permissions);
                        ps.setString(3, username);
                        ps.setString(4, permissions);
                        int count = ps.executeUpdate();
                        if (count == 0) { // try to update, insert if it is not already there
                            sqlCmd = "insert into USERHASPERMISSIONS (USERNAME, PERMISSIONS) values(?,?)";
                            try {
                                ps = this.getConnection().prepareStatement(sqlCmd);
                                ps.setString(1, username);
                                ps.setString(2, permissions);
                                ps.execute();
                            } catch (SQLException e1) {

                            }
                        }
                    } catch (SQLException e) {
                        if (e.getSQLState().equals("23505")) { // duplicate key, update instead of insert

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

        } // end if importData
    } // end constructor

    public Connection getConnection() {
        return this.connection;
    }
}
