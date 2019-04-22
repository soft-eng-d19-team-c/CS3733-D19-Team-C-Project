package model;

import java.util.HashMap;
import java.util.LinkedList;

public class SearchParameters {
    private String floor;
    private String type;
    private HashMap<String, LinkedList<String>> typeTranslator;

    public SearchParameters() {
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
        typeTranslator.put("All Lcoations", null);
    }

    public HashMap<String, LinkedList<String>> getTypeTranslator() {
        return typeTranslator;
    }

    public void setFloor(String floor) {
        this.floor = floor;
        System.out.println(floor);
    }

    public void setType(String type) {
        this.type = type;
        System.out.println(type);
    }
}
