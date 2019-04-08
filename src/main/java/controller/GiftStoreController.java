package controller;

import base.EnumScreenType;
import base.Main;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GiftStoreController extends Controller implements Initializable {
    @FXML private JFXTextField recipient;
    @FXML private JFXTextField sender;
    @FXML private AutocompleteSearchBar autocompletetextcontroller;


    public void submitButtonClick(ActionEvent actionEvent) {

        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

    public JFXTextField getRecipient() {
        return recipient;
    }

    public void setRecipient(JFXTextField recipient) {
        this.recipient = recipient;
    }

    public JFXTextField getSender() {
        return sender;
    }

    public void setSender(JFXTextField sender) {
        this.sender = sender;
    }

    public AutocompleteSearchBar getAutocompletetextcontroller() {
        return autocompletetextcontroller;
    }

    public void setAutocompletetextcontroller(AutocompleteSearchBar autocompletetextcontroller) {
        this.autocompletetextcontroller = autocompletetextcontroller;
    }
}
