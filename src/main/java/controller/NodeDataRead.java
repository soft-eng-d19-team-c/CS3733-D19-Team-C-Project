package controller;

import javafx.collections.FXCollections;
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
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NodeDataRead implements Initializable {
    // get table
    @FXML
    private TableView<Node> table;
    // get columns
    @FXML
    private TableColumn nodeID;
    @FXML
    private TableColumn xcoord;
    @FXML
    private TableColumn ycoord;
    // data to put in the table, consists of model.Node class members
    private ObservableList<Node> data;

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
            dataCursor = new Node("TEST", 12, 52);
            Stage s = new Stage();
            FXMLLoader newRoot = null;
            try {
                newRoot = FXMLLoader.load(getClass().getResource("/views/edit.fxml"));
                s.setScene(new Scene(newRoot.load()));
                newRoot.<EditNodeData>getController().initController(dataCursor);
                s.show();
//                table.getScene().setRoot(newRoot.load());
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
        xcoord.setCellValueFactory(new PropertyValueFactory("x"));
        ycoord.setCellValueFactory(new PropertyValueFactory("y"));
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
        table.getColumns().add(colBtn);
        table.setItems(this.data);
        table.refresh();
    }
}
