package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DataTable;
import model.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NodeDataRead implements Initializable {
    // get table
    @FXML
    private TableView<Node> dataTable;
    // get columns
    @FXML
    private TableColumn nodeID;
    @FXML
    private TableColumn xCoord;
    @FXML
    private TableColumn yCoord;
    @FXML
    private TableColumn floor;
    @FXML
    private TableColumn building;
    @FXML
    private TableColumn nodeType;
    @FXML
    private TableColumn longName;
    @FXML
    private TableColumn shortName;
    // data to put in the table, consists of model.Node class members
    private ObservableList<Node> data;
    private int rowCount = -2;

    private Node dataCursor;

    /**
     * Method to run when an edit button is clicked on the table.
     * Load new fxml page with data for that Node.
     *
     * @param e
     */
    EventHandler<ActionEvent> editButtonClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button b = (Button) e.getSource();
            int index = (int)b.getProperties().get("index");
            dataCursor = dataTable.getItems().get(index);
            Stage stage = (Stage) dataTable.getScene().getWindow();
            try {
                // try to load the FXML file and send the data to the controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit.fxml"));

                // try to change scene
                Parent newRoot = loader.load();
                EditNodeData controller = loader.getController();
                controller.setNodeData(dataCursor);
                Scene newScene = new Scene(newRoot);
                stage.setScene(newScene);
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    };

    /**
     * Starting routing for this jfx view.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nodeID.setCellValueFactory(new PropertyValueFactory("ID"));
        xCoord.setCellValueFactory(new PropertyValueFactory("X"));
        yCoord.setCellValueFactory(new PropertyValueFactory("Y"));
        floor.setCellValueFactory(new PropertyValueFactory("Floor"));
        building.setCellValueFactory(new PropertyValueFactory("Building"));
        nodeType.setCellValueFactory(new PropertyValueFactory("NodeType"));
        longName.setCellValueFactory(new PropertyValueFactory("LongName"));
        shortName.setCellValueFactory(new PropertyValueFactory("ShortName"));

        DataTable dt = new DataTable();
        this.data = dt.getAllData();
        // create edit buttons for each row in the table and append
        TableColumn<Node, Void> colBtn = new TableColumn("Edit");
        Callback<TableColumn<Node, Void>, TableCell<Node, Void>> cellFactory = new Callback<TableColumn<Node, Void>, TableCell<Node, Void>>() {
            @Override
            public TableCell<Node, Void> call(final TableColumn<Node, Void> param) {
                final TableCell<Node, Void> cell = new TableCell<Node, Void>() {
                    private final Button btn = new Button("Edit");
                    {
                        btn.setOnAction(editButtonClicked);
                        btn.getProperties().put("index", rowCount++);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        dataTable.getColumns().add(colBtn);
        dataTable.setItems(this.data);
        dataTable.refresh();
    }
}
