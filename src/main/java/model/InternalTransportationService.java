package model;

import java.util.Date;

public class InternalTransportationService {
    private String nodeID; //location of request
    private String nodeIDDest; //location of transportation destination
    private String description; //what is the reason for transport
    private Date dateTimeSubmitted;
    private Date dateTimeResolved;
    private boolean isComplete;
    private User completedBy;
    private User requestedBy;
    private int ID;

    public InternalTransportationService (int ID, String description, Date dateTimeSubmitted, Date dateTimeResolved, String nodeID, String nodeIDDest) {
        this.nodeID = nodeID;
        this.nodeIDDest = nodeIDDest;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = dateTimeResolved != null;
        this.completedBy = null;
        this.requestedBy = null;
        this.ID = ID;
    }
}
