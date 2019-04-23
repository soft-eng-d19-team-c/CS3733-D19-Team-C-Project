package controller;

import base.Main;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.LevenshteinDistance;
import model.Node;
import model.SearchParameters;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AutocompleteSearchBarController extends Controller implements Initializable {
    @FXML JFXTextField acTextInput;
    @FXML private JFXTextField nodeID;
    @FXML private HBox dropdowns;
    @FXML private ComboBox floors;
    @FXML private ComboBox types;

    private JFXAutoCompletePopup<Node> acSuggestions = new JFXAutoCompletePopup<>();
    private LinkedList<Node> nodes;
    private String nodeFloor;
    private boolean dropDownsOpen;
    private SearchParameters searchParameters;

    public String getNodeID() {
        return nodeID.getText();
    }
    public String getNodeFloor() {return this.nodeFloor;}

    public void setLocation(String nodeID) {
        this.nodeID.setText(nodeID);
        if (nodeID != null)
            this.acTextInput.setText("Current Location");
        else
            this.acTextInput.setText("");
    }

    public void setLocation(Node node){
        this.nodeID.setText(node.getID());
        if(node != null)
            this.acTextInput.setText(node.getShortName());
        else
            this.acTextInput.setText("");
    }

    private void setSearchMethod(){
        switch (Main.info.getSearchType()) {
            case LEVENSHTEIN:
                acTextInput.textProperty().addListener(observable -> {
                    acSuggestions.filter(string -> LevenshteinDistance.calculate(string.getShortName().toLowerCase(), acTextInput.getText().toLowerCase()) < Main.info.getSearchType().getTolerance());
                    if (acSuggestions.getFilteredSuggestions().isEmpty() || acTextInput.getText().isEmpty()) {
                        acSuggestions.hide();
                    } else {
                        acSuggestions.show(acTextInput);
                    }
                });
                break;
            case COMPARISON:
            default:
                acTextInput.textProperty().addListener(observable -> {
                    acSuggestions.filter(string -> string.getLongName().toLowerCase().contains(acTextInput.getText().toLowerCase()));
                    if (acSuggestions.getFilteredSuggestions().isEmpty() || acTextInput.getText().isEmpty()) {
                        acSuggestions.hide();
                    } else {
                        acSuggestions.show(acTextInput);
                    }
                });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
             We can get all of the nodes from the database here and populate
             a JFoenix autocomplete popup with them here.
             We can then override the default behavior of JFoenix autocomplete (which uses .toString() method)
             to set our own custom value for the ListCell to whatever we want it to be.
             We then listen for changes to the text field and populate with results containing
             the search bar text.
             When the item is selected, we have an event listener for this so we can do whatever
             we want with the selected Node (like get its ID and such).
         */
        searchParameters = new SearchParameters();
        refresh();
        acSuggestions.setSuggestionsCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
            @Override
            public ListCell<Node> call(ListView<Node> param) {
                return new ListCell<Node>(){
                    @Override
                    public void updateItem(Node item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null && !empty){
                            setGraphic(new Label(item.getLongName() + " - " + item.getBuilding()));
                        }
                    }
                };
            }
        });
        // when item is selected
        acSuggestions.setSelectionHandler(event -> {
            acTextInput.setText(event.getObject().getShortName());
            nodeID.setText(event.getObject().getID());
            this.nodeFloor = event.getObject().getFloor();
        });
        dropDownsOpen = false;
        ObservableList<String> differentFloors = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        "L2", "L1", "Ground", "1", "2", "3", "4", "Any Floor");
        ObservableList<String> differentTypes = //set the dropdown in the fxml
                FXCollections.observableArrayList(
                        "Departments",
                        "Restrooms",
                        "Food",
                        "Services",
                        "Information",
                        "Elevators",
                        "Stairs",
                        "Exits",
                        "Labs",
                        "Workspaces",
                        "Classrooms",
                        "Conference Rooms",
                        "All Locations");
        floors.setItems(differentFloors);
        types.setItems(differentTypes);
    }

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void refresh() {
        nodes = Node.getSearchableNodes();
        searchParameters.setType("All Locations");
        searchParameters.setType("Any Floor");
        setNodeSearchList(nodes);
        this.setLocation(Main.info.getKioskLocation().getID());
        this.nodeFloor = Main.info.getKioskLocation().getFloor();
        setDropDownsOpen(true); //switches to the opposite - so dropdowns will be closed
        setSearchMethod();
        floors.setValue("Any Floor");
        types.setValue("All Locations");
    }

    /**
     * pulled from refresh: sets acSuggestions to suggest the filtered list
     * @param nodes
     */
    private void setNodeSearchList(LinkedList<Node> nodes){
        acSuggestions.getSuggestions().remove(0, acSuggestions.getSuggestions().size());
        for (Node n : nodes) {
            if (n.getLongName() != null)
                acSuggestions.getSuggestions().add(n);
        }
    }

    /**
     * sets the dropdown to the opposite of b
     * @param b
     * @author Fay Whittall
     */
    public void setDropDownsOpen(boolean b){
        if(b){
            dropdowns.setVisible(false);
            dropDownsOpen = false;
        }
        else{
            dropdowns.setVisible(true);
            dropDownsOpen = true;
        }
    }

    /**
     * switches the dropdown variable from true to false
     * @author Fay Whittall
     */
    public void setDropDownsOpen(){
        setDropDownsOpen(dropDownsOpen);
    }

    /**
     * opens and closes the dropdowns
     * @param e
     * @author Fay Whittall
     */
    public void filterBtnClick(ActionEvent e){
        setDropDownsOpen();
    }

    public void floorBoxClick(ActionEvent e){
        searchParameters.setFloor((String) floors.getValue());
        LinkedList<Node> filteredNodes = searchParameters.filter(nodes);
        setNodeSearchList(filteredNodes);
    }

    public void typeBoxClick(ActionEvent e){
        searchParameters.setType((String) types.getValue());
        LinkedList<Node> filteredNodes = searchParameters.filter(nodes);
        setNodeSearchList(filteredNodes);    }
}