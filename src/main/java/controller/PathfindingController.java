package controller;

import base.Main;
import com.jfoenix.controls.JFXTextArea;
import com.twilio.type.PhoneNumber;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import model.*;


import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class PathfindingController extends Controller implements Initializable {
    @FXML private ToggleButton dancebtn;
    @FXML private ImageView findpathmap;
    @FXML private AnchorPane findpathview;
    @FXML private Pane mapImgPane;
    @FXML private AutocompleteSearchBarController searchController_origController;
    @FXML private AutocompleteSearchBarController searchController_destController;
    @FXML private JFXTextArea phoneNumberInput;
    @FXML private Button phoneNumberBtn;

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Node> node_onPath;
    private HashMap<String, Circle> nodeCircles;

    private Color black;
    private Color somecolor;

    private String findLocationNodeID;

    @Override
    public void init(URL location, ResourceBundle resources) {
        initialize(location, resources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: setimage
        Platform.runLater(() -> {

            searchController_destController.refresh();
            searchController_origController.refresh();

            findLocationNodeID = (String)Main.screenController.getData("nodeID");

            nodeCircles = new HashMap<>();
            nodes = Node.getNodesByFloor((String) Main.screenController.getData("floor"));
            edges = Edge.getEdgesByFloor((String) Main.screenController.getData("floor"));
            black = new Color(0,0,0,1);
            drawNodes(nodes, edges, black);

            if (findLocationNodeID != null) {
                Circle foundNode = nodeCircles.get(findLocationNodeID);

                foundNode.setRadius(6.0);
                foundNode.setFill(Color.ORANGERED);
                foundNode.toFront();

                ScaleTransition st = new ScaleTransition(Duration.millis(2000), foundNode);
                st.setByX(1.2);
                st.setByY(1.2);
                st.setCycleCount(Animation.INDEFINITE);
                st.setAutoReverse(true);
                st.play();
            }

        });
    }

    private void generateNodes(LinkedList<Node> nodes) {
        String prev = null;
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        for (Node n : nodes) {
            Circle circle = new Circle();
            circle.setCenterX(mapX + n.getX()/mapScale);
            circle.setCenterY(mapY + n.getY()/mapScale);
            circle.setRadius(3.0);
            circle.getProperties().put("node", n);
            if (prev != null) {
//                drawDancingline(prev, n.getID());
                Line line = new Line();
                line.startXProperty().bind(nodeCircles.get(prev).centerXProperty());
                line.startYProperty().bind(nodeCircles.get(prev).centerYProperty());
                line.endXProperty().bind(circle.centerXProperty());
                line.endYProperty().bind(circle.centerYProperty());
                line.setStroke(black);
                line.setStrokeWidth(3.0);
                mapImgPane.getChildren().add(line);
            }
            mapImgPane.getChildren().add(circle);
            nodeCircles.put(n.getID(), circle);
            prev = n.getID();
        }
    }

    @SuppressWarnings("Duplicates")
    private void dancePartyNode(LinkedList<Node> nodes) {
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        if (dancebtn.isSelected()) {
            String prev = null;
            double mapX = findpathmap.getLayoutX();
            double mapY = findpathmap.getLayoutY();
            double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
            for (Node n : nodes) {
                Circle circle = new Circle();
                circle.setCenterX(mapX + n.getX() / mapScale);
                circle.setCenterY(mapY + n.getY() / mapScale);
                circle.setRadius(3.0 + (new Random().nextDouble() * 3.0));
                circle.getProperties().put("node", n);
                Color c1 = randomColorGenerator();
                Color c2 = randomColorGenerator();
                FillTransition ft = new FillTransition(Duration.millis(517), circle, c1, c2);
                ft.setAutoReverse(true);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.play();
                if (prev != null) {
                    drawDancingline(prev, n.getID());
                }
                mapImgPane.getChildren().add(circle);
                nodeCircles.put(n.getID(), circle);
                prev = n.getID();
            }
            for (Circle c : nodeCircles.values()) {
                c.toFront();
            }
        } else {
            generateNodes(nodes);
        }
    }

    private void drawDancingline(String start, String end) {
        Color c1 = randomColorGenerator();
        Color c2 = randomColorGenerator();
        Line line = new Line();
        line.startXProperty().bind(nodeCircles.get(start).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(start).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(end).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(end).centerYProperty());
        line.setStroke(c1);
        line.setStrokeWidth(3.0);
        StrokeTransition ft = new StrokeTransition(Duration.millis(400), line, c1, c2);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        mapImgPane.getChildren().add(line);
    }

    private void drawNodes(LinkedList<Node> nodes_p, LinkedList<Edge> edges_p, Color c) {
        Color w = new Color(1,1,1,1);
        findpathview.setBackground(new Background(new BackgroundFill(w, null, null)));
        ColorAdjust reset = new ColorAdjust();
        reset.setBrightness(0);
        findpathmap.setEffect(reset);
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        double mapX = findpathmap.getLayoutX();
        double mapY = findpathmap.getLayoutY();

        final double[] orgSceneX = new double[1];
        final double[] orgSceneY = new double[1];

        for (Node n : nodes_p){
            generateNode(n, orgSceneX, orgSceneY, mapX, mapY, c);
        }
        for (Edge e : edges_p){
            generateEdge(e, c);
        }
    }

    private Line generateEdge(Edge e, Color r) {
        if (!(nodeCircles.containsKey(e.getStartNode()) && nodeCircles.containsKey(e.getEndNode()))) {
            return null; // this edge is not on this floor so we do not draw it
        }
        Line line = new Line();
        line.startXProperty().bind(nodeCircles.get(e.getStartNode()).centerXProperty());
        line.startYProperty().bind(nodeCircles.get(e.getStartNode()).centerYProperty());
        line.endXProperty().bind(nodeCircles.get(e.getEndNode()).centerXProperty());
        line.endYProperty().bind(nodeCircles.get(e.getEndNode()).centerYProperty());
        line.setStroke(r);
        mapImgPane.getChildren().add(line);
        return line;
    }


    private void generateNode(Node n, double[] orgSceneX, double[] orgSceneY, double mapX, double mapY, Color c) {
        orgSceneX[0] = -1;
        orgSceneY[0] = -1;
        Circle circle = new Circle();
        double mapScale = findpathmap.getImage().getWidth() / findpathmap.getFitWidth();
        circle.setCenterX(mapX + n.getX()/mapScale);
        circle.setCenterY(mapY + n.getY()/mapScale);
        circle.setRadius(3.0);
        circle.setFill(c);
        mapImgPane.getChildren().add(circle);
        nodeCircles.put(n.getID(), circle);
    }
    public void gobtnclick(ActionEvent actionEvent) {
        String orgi_nodeID = searchController_origController.getNodeID();
        String dest_nodeID = searchController_destController.getNodeID();
        node_onPath = Main.info.getAlgorithm().findPath(orgi_nodeID, dest_nodeID);
        somecolor = new Color(0,1,1,1);
//        drawNodes(node_onPath, somecolor);
        mapImgPane.getChildren().remove(1, mapImgPane.getChildren().size());
        generateNodes(node_onPath);
        phoneNumberBtn.setDisable(false);
        dancebtn.setVisible(true);
    }

    public void sendTextClick(ActionEvent actionEvent){
        String phoneNumber = phoneNumberInput.getText();
        PathToText pathToText = new PathToText(node_onPath);
        String path = pathToText.getDetailedPath();
        pathToText.SmsSender(path, new PhoneNumber("+1" + phoneNumber));

    }

    private Color randomColorGenerator(){
        Random rand = new Random();
        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();
        Color color = new Color(r, g, b, 1);
        return color;
    }

    public void Dancebtnclick(ActionEvent actionEvent) {
        dancePartyNode(node_onPath);
    }

    private void setImage(int imageId) {
        Image selectedImage = new Image(String.valueOf(getClass().getResource("/img/" + (String) Main.screenController.getData("floor") + "_NoIcons.png")));
        findpathmap = setImageView(selectedImage);
        findpathmap.fitWidthProperty().bind(mapImgPane.widthProperty());


        //zoom
        double width = findpathmap.getImage().getWidth();
        double height = findpathmap.getImage().getHeight();

        findpathmap.setPreserveRatio(true);
        reset(findpathmap, width, height);

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        findpathmap.setOnMousePressed(e -> {

            Point2D mousePress = imageViewToImage(findpathmap, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        findpathmap.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(findpathmap, new Point2D(e.getX(), e.getY()));
            shift(findpathmap, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(findpathmap, new Point2D(e.getX(), e.getY())));
        });

        findpathmap.setOnScroll(e -> {
            double delta = -e.getDeltaY();
            Rectangle2D viewport = findpathmap.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(findpathmap, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            findpathmap.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        findpathmap.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(findpathmap, width, height);
            }
        });

        findpathmap.setPreserveRatio(true);
        mapImgPane.setCenter(findpathmap);

        findpathmap.fitWidthProperty().bind(mapImgPane.widthProperty());
        findpathmap.fitHeightProperty().bind(mapImgPane.heightProperty());
    }

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
// the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    private ImageView setImageView(Image image) {

        ImageView imageView = new ImageView();
        imageView.setImage(new javafx.scene.image.Image("file:" + image.getAbsPathI()));

        double w;
        double h;

        double ratioX = imageView.getFitWidth() / imageView.getImage().getWidth();
        double ratioY = imageView.getFitHeight() / imageView.getImage().getHeight();

        double reducCoeff;
        if(ratioX >= ratioY) {
            reducCoeff = ratioY;
        } else {
            reducCoeff = ratioX;
        }

        w = imageView.getImage().getWidth() * reducCoeff;
        h = imageView.getImage().getHeight() * reducCoeff;

        imageView.setX((imageView.getFitWidth() - w) / 2);
        imageView.setY((imageView.getFitHeight() - h) / 2);

        return imageView;
    }

}


