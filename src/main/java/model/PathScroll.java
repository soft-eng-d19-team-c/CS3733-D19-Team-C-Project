package model;

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
        int a;
        int b;
        if(oldPosition < newPosition){
            a = oldPosition;
            b = newPosition;
        }
        else{
            b = oldPosition;
            a = newPosition;
        }
        Node[] nodesInRange = new Node[b - a];
        int indexOfNodesInRange = 0;
        for(int i = a; i <= b; i++){
            nodesInRange[indexOfNodesInRange] = this.nodesOnPath[i];
            indexOfNodesInRange++;
        }
        return nodesInRange;
    }
}
