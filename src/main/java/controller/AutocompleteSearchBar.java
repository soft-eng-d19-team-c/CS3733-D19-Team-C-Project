package controller;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.DataTable;
import model.Node;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AutocompleteSearchBar implements Initializable {
    @FXML
    private JFXTextField acTextInput;
    DataTable dt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dt = new DataTable();
        LinkedList<Node> nodes = dt.getProjectCNodesByFloor("L2");
        JFXAutoCompletePopup<String> acSuggestions = new JFXAutoCompletePopup<>();
        for (Node n : nodes) {
            acSuggestions.getSuggestions().add(n.getShortName());
        }

//        acSuggestions.setSuggestionsCellFactory(//(Callback<ListView<String>, ListCell<String>>) param -> {
////            return new JFXListCell<>();
////            return new SimpleStringProperty();
//            new PropertyValueFactory<Node, String>("toString");
////        });
//        );


        // when item is selected
        acSuggestions.setSelectionHandler(event -> {
            acTextInput.setText(event.getObject());
        });

        // listen for changes to text field and filter results
        acTextInput.textProperty().addListener(observable -> {
            acSuggestions.filter(string -> string.toLowerCase().contains(acTextInput.getText().toLowerCase()));
            if (acSuggestions.getFilteredSuggestions().isEmpty() || acTextInput.getText().isEmpty()) {
                acSuggestions.hide();
            } else {
                acSuggestions.show(acTextInput);
            }
        });
        
    }
}
