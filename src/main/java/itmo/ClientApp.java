package itmo;

import itmo.client.ClientProviding;
import itmo.gui.controllers.server.ServerConnectionController;
import itmo.utils.ClientCollectionManager;
import itmo.utils.ClientUtils;
import itmo.utils.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import lab6common.generatedclasses.Coordinates;
import lab6common.generatedclasses.Location;
import lab6common.generatedclasses.Route;
import org.apache.log4j.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClientApp  extends Application {
    public static final Logger LOGGER = Logger.getLogger(ClientApp.class.getName());

    private static ClientUtils clientUtils;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*Pane wrapperMapPane = new Pane();
        double x = 0;
        double y = 0;
        double size = 1;
        *//*gc.fillPolygon(new double[]{45 * size + x, 85 * size + x, 65 * size + x},
                new double[]{70 * size + y, 70 * size + y, 120 * size + y}, 3);
        gc.fillOval(40 * size + x, 40 * size + y, 50 * size, 50 * size);
        gc.setFill(Color.WHITE);
        gc.fillOval(50 * size + x, 50 * size + y, 30 * size, 30 * size);*//*
        Circle circle = new Circle(65 * size + x, 60 * size + y, 50 * size, Color.BLACK);
        //Polygon polygon = new Polygon(45 * size + x,
          //      70 * size + y,  85 * size + x,70 * size + y,65 * size + x, 120 * size + y);
        Polygon polygon = new Polygon(15 * size + x,60 * size + y,
                115 * size + x,60 * size + y,65 * size + x,150 * size + y);
        Circle circlew = new Circle(65 * size + x, 60 * size + y, 30 * size, Color.valueOf("white"));
        wrapperMapPane.getChildren().addAll(circle,polygon,circlew);
        Canvas canvas = new Canvas(900,900);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Group root = new Group();
        Scene scene = new Scene(root,900,900);
        root.getChildren().addAll(circle,polygon,circlew);
        primaryStage.setTitle("Road to deduction");
        primaryStage.setScene(scene);
        primaryStage.show();*/

        UserManager userManager = new UserManager(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out)), true);
        ClientCollectionManager clientCollectionManager = new ClientCollectionManager();
        ClientProviding clientProviding = new ClientProviding(userManager);
        this.clientUtils = new ClientUtils() {

            @Override
            public UserManager userManager() {
                return userManager ;
            }

            @Override
            public ClientProviding clientProviding() {
                return clientProviding;
            }

            @Override
            public ClientCollectionManager clientCollectionManager() {
                return clientCollectionManager;
            }
        };
        Locale.setDefault(Locale.forLanguageTag("ru"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/panes/start_pane.fxml"));
        fxmlLoader.setController(new ServerConnectionController(clientUtils));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Road to deduction");
        primaryStage.show();

    }
    private void drawShape(GraphicsContext gc, double size, double x, double y) {
        gc.fillPolygon(new double[]{45*size + x, 85*size + x, 65*size + x},
                new double[]{70*size + y, 70*size + y, 120*size + y}, 3);
        gc.fillOval(40 * size + x, 40 * size + y, 50 * size, 50 * size);
        gc.setFill(Color.WHITE);
        gc.fillOval(50 * size + x, 50 * size + y, 30 * size, 30 * size);

    }
}


