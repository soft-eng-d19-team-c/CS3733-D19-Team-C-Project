package model;

import java.util.LinkedList;

public class TextInfo {
    private LinkedList<String> floorStrings = new LinkedList<>();
    private LinkedList<String> currentFloors;
    private int NumberOfAccordions;


    public LinkedList<String> getFloorStrings() {
        return floorStrings;
    }

    public LinkedList<String> getCurrentFloors() {
        return currentFloors;
    }

    public int getNumberOfAccordions() {
        return NumberOfAccordions;
    }


    public TextInfo(LinkedList<String> floorStrings, LinkedList<String> currentFloors) {
        this.floorStrings = floorStrings;
        this.currentFloors = currentFloors;
        NumberOfAccordions = currentFloors.size();
    }
}
