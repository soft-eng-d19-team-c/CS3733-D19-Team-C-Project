package controller;

import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Feedback;

import java.net.URL;
import java.util.ResourceBundle;

public class FeedbackController extends Controller implements Initializable {
    @FXML private NavController navController;
    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navController.setActiveTab(NavTypes.NONE);
    }


    public void goodBtnClick(ActionEvent actionEvent) {
        submitFeedback(EnumFeedbackType.GOOD);
    }

    public void okBtnClick(ActionEvent actionEvent) {
        submitFeedback(EnumFeedbackType.OK);
    }

    public void badBtnClick(ActionEvent actionEvent) {
        submitFeedback(EnumFeedbackType.BAD);
    }


    private void submitFeedback(EnumFeedbackType type) {
        new Feedback(type.getValue());
        Main.screenController.goBack();
    }

}
