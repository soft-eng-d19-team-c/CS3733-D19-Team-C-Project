package model;

import java.util.Comparator;

public class PathValueComparator implements Comparator<PathValue> {
    public int compare(PathValue a, PathValue b) {
        return Double.compare(a.getTotalCost(), b.getTotalCost());
    }
}