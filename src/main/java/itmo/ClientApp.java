package itmo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.*;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientApp  extends Application {
    public static final Logger LOGGER = Logger.getLogger(ClientApp.class.getName());

    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("/views/menus/server_connection_menu.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle("languages.Langs", new Locale("ru"));
        fxmlLoader.setResources(bundle);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


