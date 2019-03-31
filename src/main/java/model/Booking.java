package model;

import java.util.Date;

public class Booking {
    private String nodeID;
    private Date startTime;
    private Date endTime;
    private User user;
    private String ID;


    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public String getID() {
        return ID;
    }

    public void setID() {
        this.ID = this.nodeID + "_" + this.user.getUsername() + "_" + this.startTime.toString();
    }
}
