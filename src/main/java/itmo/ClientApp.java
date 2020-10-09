package itmo;

import itmo.client.ClientProviding;
import itmo.gui.controllers.server.ServerConnectionController;
import itmo.utils.ClientCollectionManager;
import itmo.utils.ClientUtils;
import itmo.utils.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
}


