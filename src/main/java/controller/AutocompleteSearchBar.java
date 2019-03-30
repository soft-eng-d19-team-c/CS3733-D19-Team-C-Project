package controller;

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

public class AutocompleteSearchBar implements Initializable {
    @FXML
    private JFXTextField acTextInput;
//    DataTable dt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        dt = new DataTable();
        LinkedList<Node> nodes = Node.getNodes();
        JFXAutoCompletePopup<Node> acSuggestions = new JFXAutoCompletePopup<>();
        for (Node n : nodes) {
            acSuggestions.getSuggestions().add(n);
        }

        acSuggestions.setSuggestionsCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
            @Override
            public ListCell<Node> call(ListView<Node> param) {
                return new ListCell<Node>(){
                    @Override
                    public void updateItem(Node item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null && !empty){
                            setGraphic(new Label(item.getShortName()));
                        }
                    }
                };
            }
        });


        // when item is selected
        acSuggestions.setSelectionHandler(event -> {
            acTextInput.setText(event.getObject().getShortName());
        });

        // listen for changes to text field and filter results
        acTextInput.textProperty().addListener(observable -> {
            acSuggestions.filter(string -> string.getShortName().toLowerCase().contains(acTextInput.getText().toLowerCase()));
            if (acSuggestions.getFilteredSuggestions().isEmpty() || acTextInput.getText().isEmpty()) {
                acSuggestions.hide();
            } else {
                acSuggestions.show(acTextInput);
            }
        });
        
    }
}
