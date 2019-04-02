package base;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public final class Facade {
    private HashMap<EnumScreenType, Parent> screens;
    private HashMap<EnumScreenType, Controller> controllers;
    private Scene primaryScene;
    private HashMap<String, Object> data;
    private Stack<EnumScreenType> history;
    private EnumScreenType prevType;

    public Facade(Scene s) {
        this.screens = new HashMap<>();
        this.controllers= new HashMap<>();
        this.data = new HashMap<>();
        this.primaryScene = s;
        this.history = new Stack<>();
        this.prevType = EnumScreenType.DASHBOARD;
    }

    public void setScreen(EnumScreenType type) {
        setScreen(type, null);
    }

    public void setScreen(EnumScreenType type, HashMap<String, Object> data) {
        this.data = data;
        this.history.push(prevType);
        this.prevType = type;
        if (this.screens.containsKey(type)) {
            loadCachedScreen(type);
        } else {
            loadNewScreen(type);
        }
        if (type == EnumScreenType.DASHBOARD) {
            clearHistory();
        }
    }

    private void loadNewScreen(EnumScreenType type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getPath()));
            Parent newRoot = loader.load();
            this.screens.put(type, newRoot);
            this.controllers.put(type, loader.getController());
            this.primaryScene.setRoot(newRoot);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void loadCachedScreen(EnumScreenType type) {
        this.primaryScene.setRoot(this.screens.get(type));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getPath()));
        this.controllers.get(type).init(getClass().getResource(type.getPath()), loader.getResources());
    }

    public void goBack() {
        goBack(null);
    }

    public void goBack(HashMap<String, Object> data) {
        setScreen(this.history.pop(), data);
    }

    private void clearHistory() {
        this.history.clear();
    }

    public Object getData(String key) {
        if (this.data == null) {
            System.err.println("There is no data set in the Facade.");
            return null;
        }
        if (this.data.containsKey(key)) {
            return this.data.get(key);
        } else {
            System.err.println("Looking for Key: " + key + ", but no member with that name was found in keyset: " + this.data.keySet());
            return null;
        }
    }

}
