package model;

import java.util.Arrays;

public class PathScroll {
    Node[] nodesOnPath;
    int oldPosition;

    public PathScroll(Node[] nodesOnPath) {
        this.nodesOnPath = nodesOnPath;
        this.oldPosition = 0;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    /**
     *
     * @return array of nodes to be changed in the map
     * @author Fay Whittall
     */
    public Node[] getNodesInRange(int newPosition){
        return Arrays.copyOfRange(this.nodesOnPath, Math.min(oldPosition, newPosition), Math.max(oldPosition, newPosition));
    }

    public int getOldPosition() {
        return this.oldPosition;
    }
}
