package model;

import javafx.scene.control.Label;

import java.util.LinkedList;

public class ScrollBarLabel {
    int location;
    Label label = new Label();
    String floor;

    public ScrollBarLabel(int location, String floor) {
        this.location = location;
        this.floor = floor;

        switch (floor){
            case "G":
                this.label = new Label("G");
                break;
            case "L1":
                this.label = new Label("L1");
                break;
            case "L2":
                this.label = new Label("L2");
                break;
            case "1":
                this.label = new Label("1");
                break;
            case "2":
                this.label = new Label("2");
                break;
            case "3":
                this.label = new Label("3");
                break;
            case "4":
                this.label = new Label("4");
                break;
        }
    }

    public int getLocation() {
        return location;
    }

    public Label getLabel() {
        return label;
    }

    public String getFloor() {
        return floor;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
