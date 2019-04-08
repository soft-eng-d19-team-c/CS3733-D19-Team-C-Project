package controller;

import base.Main;
import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.Node;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AutocompleteSearchBarController extends Controller implements Initializable {
    @FXML
    JFXTextField acTextInput;
    @FXML
    private JFXTextField nodeID;

    private JFXAutoCompletePopup<Node> acSuggestions = new JFXAutoCompletePopup<>();

    private LinkedList<Node> nodes;

    public String getNodeID() {
        return nodeID.getText();
    }

    public void setLocation(String nodeID) {
        this.nodeID.setText(nodeID);
        this.acTextInput.setText("Current Location");
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
             When the item is selcted, we have an event listener for this so we can do whatever
             we want with the selcted Node (like get its ID and such).
         */
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
        });
        // listen for changes to text field and filter results
        acTextInput.textProperty().addListener(observable -> {
            acSuggestions.filter(string -> string.getLongName().toLowerCase().contains(acTextInput.getText().toLowerCase()));
            if (acSuggestions.getFilteredSuggestions().isEmpty() || acTextInput.getText().isEmpty()) {
                acSuggestions.hide();
            } else {
                acSuggestions.show(acTextInput);
            }
        });
    }

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    public void refresh() {
        String floor = (String) Main.screenController.getData("autocompleteFloor");
        if (floor != null) {
            nodes = Node.getNodesByFloor(floor);
        } else {
            nodes = Node.getNodes();
        }
        for (Node n : nodes) {
            if (n.getLongName() != null)
                acSuggestions.getSuggestions().add(n);
        }
        this.setLocation(Main.info.getKioskLocation().getID());
    }
}
