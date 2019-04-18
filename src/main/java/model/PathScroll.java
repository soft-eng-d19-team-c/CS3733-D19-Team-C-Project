package model;

import java.util.Arrays;
import java.util.LinkedList;

public class PathScroll {
    Node[] nodesOnPath;
    int oldPosition;

    public PathScroll(LinkedList<Node> nodesOnPath) {
        this.oldPosition = 0;
        int i = nodesOnPath.size();
        this.nodesOnPath = new Node[i];
        for (Node n : nodesOnPath)
            this.nodesOnPath[--i] = n;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Node[] getNodesOnPath() {
        return nodesOnPath;
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
