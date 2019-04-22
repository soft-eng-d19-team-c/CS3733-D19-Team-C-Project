package base;

import model.Node;

import java.util.HashMap;
import java.util.LinkedList;

public final class SearchParameters {
    private String floor;
    private LinkedList<String> type;
    private HashMap<String, LinkedList<String>> typeTranslator;
    private HashMap<LinkedList<String>, LinkedList<Node>> listCache; //holds all of the lists of nodes for faster access

    /**
     * the constructor. creates the type translator and the list cache
     */
    public SearchParameters() {
        this.listCache = new HashMap<>();
        this.typeTranslator = new HashMap<>();
        //create linked lists
        LinkedList<String> deptLL = new LinkedList<>();
        deptLL.add("DEPT");
        LinkedList<String> restLL = new LinkedList<>();
        restLL.add("REST");
        LinkedList<String> foodLL = new LinkedList<>();
        foodLL.add("RETL");
        LinkedList<String> servLL = new LinkedList<>();
        servLL.add("SERV");
        LinkedList<String> infoLL = new LinkedList<>();
        infoLL.add("INFO");
        LinkedList<String> elevLL = new LinkedList<>();
        elevLL.add("ELEV");
        LinkedList<String> stairsLL = new LinkedList<>();
        stairsLL.add("STAI");
        LinkedList<String> exitLL = new LinkedList<>();
        exitLL.add("EXIT");
        LinkedList<String> labsLL = new LinkedList<>();
        labsLL.add("LABS");
        LinkedList<String> workspaceLL = new LinkedList<>();
        workspaceLL.add("WORKZONE");
        workspaceLL.add("PANTRY");
        LinkedList<String> classLL = new LinkedList<>();
        classLL.add("CLASS");
        classLL.add("AMPHITHEATER");
        classLL.add("MISSIONROOM");
        LinkedList<String> confLL = new LinkedList<>();
        confLL.add("CONF");
        //add to type translator
        typeTranslator.put("Departments", deptLL);
        typeTranslator.put("Restrooms", restLL);
        typeTranslator.put("Food", foodLL);
        typeTranslator.put("Services", servLL);
        typeTranslator.put("Information", infoLL);
        typeTranslator.put("Elevators", elevLL);
        typeTranslator.put("Stairs", stairsLL);
        typeTranslator.put("Exits",exitLL);
        typeTranslator.put("Labs", labsLL);
        typeTranslator.put("Workspaces", workspaceLL);
        typeTranslator.put("Classrooms", classLL);
        typeTranslator.put("Conference Rooms", confLL);
        typeTranslator.put("All Locations", null);
    }

    /**
     * sets the floor, returns the filtered list of nodes
     * @param floor
     * @param nodes
     * @return the list of nodes with the given filters
     * @author Fay Whittall
     */
    public LinkedList<Node> setFloor(String floor, LinkedList<Node> nodes) {
        this.floor = floor;
        if(floor.equals("Ground"))
            this.floor = "G";
        return filter(nodes);
    }

    /**
     * sets the type, returns the filtered list of nodes
     * @param type
     * @param nodes
     * @return the list of nodes with the given filters
     * @author Fay Whittall
     */
    public LinkedList<Node> setType(String type, LinkedList<Node> nodes) {
        this.type = typeTranslator.get(type);
        return filter(nodes);
    }

    /**
     * filters all nodes that are searchable, looks in the cache if it can, if not manually filteres
     * @param nodes
     * @return filtered list of nodes
     * @author Fay Whittall
     */
    public LinkedList<Node> filter(LinkedList<Node> nodes){
        LinkedList<String> key = new LinkedList<>();
        key.add(this.floor);
        if(this.type != null){
            for(String t: this.type){
                key.add(t);
            }
        }
        if(listCache.containsKey(key)){
            return listCache.get(key);
        }
        else{
            LinkedList<Node> filteredNodes = filterByFloor(filterByType(nodes));
            listCache.put(key, filteredNodes);
            return filteredNodes;
        }
    }

    public LinkedList<Node> filterByFloor(LinkedList<Node> nodes){
        if(this.floor.equals("Any Floor"))
            return nodes;
        LinkedList<Node> filteredNodes = new LinkedList<>();
        for(Node n : nodes){
            if(n.getFloor().equals(this.floor)){
                filteredNodes.add(n);
            }
        }
        return filteredNodes;
    }

    public LinkedList<Node> filterByType(LinkedList<Node> nodes){
        if(this.type == null)
            return nodes;
        LinkedList<Node> filteredNodes = new LinkedList<>();
        for(Node n : nodes){
            if(this.type.contains(n.getNodeType())){
                filteredNodes.add(n);
            }
        }
        return filteredNodes;
    }
}
