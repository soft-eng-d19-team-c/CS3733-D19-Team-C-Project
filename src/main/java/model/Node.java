package model;

public class Node {
    private String ID;
    private int x;
    private int y;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;

    public String getID() {
        return ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getFloor() { return floor; }

    public String getBuilding() { return building; }

    public String getNodeType() { return nodeType; }

    public String getLongName() { return longName; }

    public String getShortName() { return shortName; }

    public Node(String ID, int x, int y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }

}
