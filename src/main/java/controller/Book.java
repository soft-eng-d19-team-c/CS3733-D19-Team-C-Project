package controller;

import base.EnumScreenType;
import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Book extends Controller implements Initializable {

    @FXML
    private ComboBox<String> locationBox;
    @FXML
    private ComboBox<String> startTimeBox;
    @FXML
    private ComboBox<String> endTimeBox;
    @FXML
    private Button bookRoomButton;

    //create booking object to be updated


    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] times = {"12:00am", "12:30am", "1:00am", "1:30am", "2:00am", "2:30am", "3:00am", "3:30am", "4:00am", "4;30am", "5:00am", "5:30am", "6:00am", "6:30am", "7:00am", "7:30am", "8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00pm", "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm", "4;30pm", "5:00pm", "5:30pm", "6:00pm", "6:30pm", "7:00pm", "7:30pm", "8:00pm", "8:30pm", "9:00pm", "9:30pm", "10:00pm", "10:30pm", "11:00pm", "11:30pm"};
        ObservableList<String> bookableLocations = FXCollections.observableArrayList();
        String str = "SELECT * FROM BOOKINGLOCATIONS";
        Statement statement = null;
        try{
            statement = Main.database.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(str);
            while (rs.next()) {
                bookableLocations.add(rs.getString("TITLE"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        startTimeBox.getItems().addAll(times);
        endTimeBox.getItems().addAll(times);
        locationBox.getItems().addAll(bookableLocations);
    }

    public void bookRoomButtonClick(ActionEvent actionEvent) {
        //needs to update some sort of schedule saying which rooms are booked for certain times
        Main.screenController.setScreen(EnumScreenType.DASHBOARD);
    }

}
