package base;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

/**
 * This is the Facade class that is used for passing data between controllers
 * as well as switching between screens.
 * @author Ryan LaMarche
 */
public final class Facade {
    private HashMap<EnumScreenType, Parent> screens;
    private HashMap<EnumScreenType, Controller> controllers;
    private Scene primaryScene;
    private HashMap<String, Object> data;
    private Stack<EnumScreenType> history;
    private EnumScreenType prevType;
    private Image backgroundImage; // for caching

    /**
     * Creates a Facade.
     * @author Ryan LaMarche
     * @param s
     */
    public Facade(Scene s) {
        this.screens = new HashMap<>();
        this.controllers= new HashMap<>();
        this.data = new HashMap<>();
        this.primaryScene = s;
        this.history = new Stack<>();
        this.prevType = EnumScreenType.PATHFINDING;
        this.backgroundImage = new Image(String.valueOf(getClass().getResource("/img/background.png")));
    }

    /**
     * Sets screen.
     * @author Ryan LaMarche
     * @param type
     */
    public void setScreen(EnumScreenType type) {
        setScreen(type, null, true);
    }

    /**
     * Sets screen with data.
     * @author Ryan LaMarche
     * @param type
     * @param data
     */
    public void setScreen(EnumScreenType type, HashMap<String, Object> data) {
        setScreen(type, data, true);
    }

    /**
     * Sets screen with data and the option to not add the screen to the history stack.
     * @author Ryan LaMarche
     * @param type
     * @param data
     * @param addToHistory
     */
    public void setScreen(EnumScreenType type, HashMap<String, Object> data, boolean addToHistory) {
        this.primaryScene.setCursor(Cursor.DEFAULT);
        this.data = data;
        if (addToHistory) {
            this.history.push(prevType);
        }
        this.prevType = type;
        if (this.screens.containsKey(type)) {
            loadCachedScreen(type);
        } else {
            loadNewScreen(type);
        }
    }

    /**
     * Loads the Parent of a new FXML page from disk.
     * @param type
     * @author Ryan LaMarche
     */
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

    /**
     * Loads the Parent of a cached FXML page and initializes its controller.
     * @param type
     * @author Ryan LaMarche
     */
    private void loadCachedScreen(EnumScreenType type) {
        this.primaryScene.setRoot(this.screens.get(type));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getPath()));
        this.controllers.get(type).init(getClass().getResource(type.getPath()), loader.getResources());
    }

    /**
     * Returns to previous screen.
     * @author Ryan LaMarche
     */
    public void goBack() {
        goBack(null);
    }

    /**
     * Returns to previous screen with data.
     * @author Ryan LaMarche
     * @param data
     */
    public void goBack(HashMap<String, Object> data) {
        setScreen(this.history.pop(), data, false);
    }

    /**
     * Helper function to clear the history Stack of the go back button.
     * @author Ryan LaMarche
     */
    protected void clearHistory() {
        this.history.clear();
        this.history.push(EnumScreenType.PATHFINDING);
    }

    /**
     * Getter for data passed between controllers using a hash map.
     * @author Ryan LaMarche
     * @param key the key in the hash mapped that was passed to switchScreen().
     * @return Object that must be cast to the correct type after you get it from the Facade.
     */
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

    /**
     * @author Ryan LaMarche
     * @return the cached background image
     */
    public Image getBackgroundImage() {
        return this.backgroundImage;
    }

    public EnumScreenType getPrevType() {
        return prevType;
    }
}
