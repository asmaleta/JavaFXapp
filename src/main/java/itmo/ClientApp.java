package itmo;

import itmo.client.ClientProviding;
import itmo.gui.AlertMaker;
import itmo.gui.controllers.ServerConnectionController;
import itmo.utils.ClientUtils;
import itmo.utils.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientApp  extends Application {
    public static final Logger LOGGER = Logger.getLogger(ClientApp.class.getName());

    public static ClientUtils getClientUtils() {
        return clientUtils;
    }

    private static ClientUtils clientUtils;
    public static void main(String[] args) {
        UserManager userManager = new UserManager(
                new BufferedReader(new InputStreamReader(System.in)),
                new BufferedWriter(new OutputStreamWriter(System.out)), true);
        ClientProviding clientProviding = new ClientProviding(userManager);
        clientUtils = new ClientUtils() {
            @Override
            public UserManager userManager() {
                return userManager ;
            }

            @Override
            public ClientProviding clientProviding() {
                return clientProviding;
            }
        };
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/server_connection_menu.fxml"));
        //fxmlLoader.setController(new ServerConnectionController(clientUtils));
        ResourceBundle bundle = ResourceBundle.getBundle("languages.Langs", new Locale("ru"));
        fxmlLoader.setResources(bundle);
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}


