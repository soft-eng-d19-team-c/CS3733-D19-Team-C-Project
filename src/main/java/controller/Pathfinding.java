package controller;

import base.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import model.AStar;
import model.DataTable;
import model.Edge;
import model.Node;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Pathfinding extends Controller implements Initializable {
    @FXML private ImageView findpathmap;
    @FXML private AnchorPane findpathview;
    @FXML private TextField origin;
    @FXML private TextField destination;
    @FXML private Button findpathbtn;
    @FXML private Window dialog1;
    @FXML private AutocompleteSearchBar searchController_origController;
    @FXML private Window dialog2;
    @FXML private AutocompleteSearchBar searchController_destController;

    AStar star;
    LinkedList<Node> astarResult;

    private DataTable dt;

    private LinkedList<Node> node_onPath;
    private HashMap<String, Edge> edge_onPath;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findpathmap.setImage(new Image(String.valueOf(getClass().getResource("/img/"+ Main.screenController.getData("floor")+"_NoIcons.png"))));
    }

    public void gobtnclick(ActionEvent actionEvent) {
        /*

        I think here instead of switching screens, we just want to create a new AStar object and findPath
        then display the path on the map. This will all happen on one page.

        HashMap<String, Object> data = new HashMap<>();
        data.put("orgi_nodeID", searchController_origController.getNodeID());
        data.put("dest_nodeID", searchController_destController.getNodeID());
        Main.screenController.setScreen(EnumScreenType.DASHBOARD, data);

         */
    }
}
