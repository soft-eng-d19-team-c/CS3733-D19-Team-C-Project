package base;

import controller.Controller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 * This is the Facade class that is used for passing data between controllers
 * as well as switching between screens.
 * @author Ryan LaMarche
 */
public final class Facade {
    private HashMap<EnumScreenType, ScreenCacheMachine> cacheMachine;
    private Scene primaryScene;
    private HashMap<String, Object> data;
    private Stack<EnumScreenType> history;
    private EnumScreenType prevType;
    private Image backgroundImage; // for caching

    /**
     * This class is used for storing information about screens as they are loaded so we can cache them.
     * @author Ryan LaMarche
     */
    private final class ScreenCacheMachine {
        private Parent parent;
        private Controller controller;
        private URL location;
        private ResourceBundle resources;

        ScreenCacheMachine(Parent parent, Controller controller, URL location, ResourceBundle resources) {
            this.parent = parent;
            this.controller = controller;
            this.location = location;
            this.resources = resources;
        }

        Parent getParent() {
            return parent;
        }

        Controller getController() {
            return controller;
        }

        URL getLocation() {
            return location;
        }

        ResourceBundle getResources() {
            return resources;
        }
    }



    /**
     * Creates a Facade.
     * @author Ryan LaMarche
     * @param s
     */
    public Facade(Scene s) {
        this.cacheMachine = new HashMap<>();
        this.data = new HashMap<>();
        this.primaryScene = s;
        this.history = new Stack<>();
        this.prevType = EnumScreenType.PATHFINDING;
        this.backgroundImage = new Image(String.valueOf(getClass().getResource("/img/background.png")));
        Platform.runLater(() -> {
            System.out.println("Attempting to load first screen into cacheMachine on thread: " + Thread.currentThread().getId() + "...");
            loadNewScreen(EnumScreenType.PATHFINDING);
            System.out.println("Successfully loaded first screen into cacheMachine...");
        });
    }

    /**
     * gets the primary scene
     * @author Fay Whittall
     * @return primary scene
     */
    Scene getPrimaryScene() {
        return primaryScene;
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
        if (this.cacheMachine.containsKey(type)) {
            this.primaryScene.setRoot(loadCachedScreen(type));
        } else {
            this.primaryScene.setRoot(loadNewScreen(type));
        }
    }

    /**
     * Loads the Parent of a new FXML page from disk.
     * @param type
     * @author Ryan LaMarche
     */
    private Parent loadNewScreen(EnumScreenType type) {
        FXMLLoader loader;
        Parent newRoot = null;
        Controller newController = null;
        ResourceBundle newResources = null;
        URL newLocation = getClass().getResource(type.getPath());
        try {
            loader = new FXMLLoader(newLocation);
            newRoot = loader.load();
            newController = loader.getController();
            newResources = loader.getResources();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ScreenCacheMachine newCacheMachine = new ScreenCacheMachine(newRoot, newController, newLocation, newResources);
        this.cacheMachine.put(type, newCacheMachine);
        return newRoot;
    }

    /**
     * Loads the Parent of a cached FXML page and initializes its controller.
     * @param type
     * @author Ryan LaMarche
     */
    private Parent loadCachedScreen(EnumScreenType type) {
        return loadCachedScreen(type, true);
    }


        /**
         * Loads the Parent of a cached FXML page and initializes its controller.
         * @param type
         * @author Ryan LaMarche
         */
    private Parent loadCachedScreen(EnumScreenType type, boolean doInit) {
        ScreenCacheMachine temp = this.cacheMachine.get(type);
        if(doInit){
            temp.getController().init(temp.getLocation(), temp.getResources());
        }
        else{
            temp.getController();
        }
        return temp.getParent();
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
        if (this.history.size() > 0)
            loadCachedScreen(this.history.pop(), false);
        else
            loadCachedScreen(EnumScreenType.PATHFINDING, false);
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
