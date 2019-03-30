package model;

import java.util.Date;

//Create service request, get service request, and have it talk to database

public class ServiceRequest {

    private String type;
    private Node location;
    private String description;
    private Date dateTimeSubmitted;
    private Date dateTimeResolved;
    private boolean isComplete;
    private User completedBy;
    private User requestedBy;

    public ServiceRequest(String type, Node location, String description, Date dateTimeSubmitted, Date dateTimeResolved, boolean isComplete, User completedBy, User requestedBy) {
        this.type = type;
        this.location = location;
        this.description = description;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.dateTimeResolved = dateTimeResolved;
        this.isComplete = isComplete;
        this.completedBy = completedBy;
        this.requestedBy = requestedBy;
    }

    //Determines amount of time task was completed in

    public Date computeTimeDiff(){

        long end = dateTimeResolved.getTime();
        long start = dateTimeSubmitted.getTime();
        return new Date(end - start);

    }

    public boolean update(){
        return false;
    }

}
