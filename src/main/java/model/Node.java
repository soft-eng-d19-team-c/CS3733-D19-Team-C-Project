package model;

public class Node {
    private String ID;
    private int x;
    private int y;

    public String getID() {
        return ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node(String ID, int x, int y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }

}
