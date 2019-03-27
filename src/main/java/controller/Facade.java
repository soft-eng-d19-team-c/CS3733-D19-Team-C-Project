package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class Facade {
    private HashMap<EnumScreenType, Parent> screens;
    private HashMap<EnumScreenType, Controller> controllers;
    private Scene primaryScene;
    private HashMap<String, Object> data;

    public Facade(Scene s) {
        this.screens = new HashMap<>();
        this.controllers= new HashMap<>();
        this.data = new HashMap<>();
        this.primaryScene = s;
    }

    public void setScreen(EnumScreenType type, HashMap<String, Object> data) {
        this.data = data;
        if (this.screens.containsKey(type)) {
            this.primaryScene.setRoot(this.screens.get(type));
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getPath()));
            this.controllers.get(type).init(getClass().getResource(type.getPath()), loader.getResources());
        } else {
            try {
                // try to load the FXML file and send the data to the controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getPath()));
                Parent newRoot = loader.load();
                this.screens.put(type, newRoot);
                this.controllers.put(type, loader.getController());
                this.primaryScene.setRoot(newRoot);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setScreen(EnumScreenType type) {
        setScreen(type, null);
    }

    public Object getData(String key) {
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        } else {
            System.err.println("Looking for Key: " + key + ", but no member with that name was found in keyset: " + this.data.keySet());
            return null;
        }
    }

}
