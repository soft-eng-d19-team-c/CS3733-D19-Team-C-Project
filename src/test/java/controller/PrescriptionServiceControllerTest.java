package controller;

import base.EnumScreenType;
import com.twilio.rest.studio.v1.Flow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
//import org.testfx.service.*;

import javax.swing.text.FlowView;

import java.beans.EventHandler;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class PrescriptionServiceControllerTest extends ApplicationTest {
    PrescriptionServiceController psc;
    Scene s;

    @Override
    public void start(Stage stage) throws Exception {
//        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(EnumScreenType.PRESCRIPTIONSERVICE.getPath())));
//        stage.setScene(scene);
//        stage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EnumScreenType.PRESCRIPTIONSERVICE.getPath()));
        s = new Scene(loader.load());
        psc = loader.getController();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void submitButtonClick_test() {
//        verifyThat(".button", hasText("Submit"));
        //TODO
        //pass things into the text fields
        //put the mouse on the submit btn
//        clickOn(onNode((Node)psc.submitButton), MouseButton.PRIMARY);
//        clickOn(psc.submitButton);
        //Query the db
        //assert with a string that we manually made according to our expectation
    }
}