package base;

public enum EnumAlgorithm {
    ASTAR("AStar"),
    DIJKSTRA("Dijkstra's Algorithm"),
    BFS("Breadth First Search"),
    DFS("Depth First Search");


    protected String name;

    EnumAlgorithm(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
