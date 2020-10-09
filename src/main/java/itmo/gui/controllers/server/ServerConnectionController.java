package itmo.gui.controllers.server;

import com.jfoenix.controls.JFXTextField;

import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import itmo.gui.AlertMaker;
import itmo.gui.controllers.lang.LangController;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.gui.controllers.login.LoginRegisterController;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import lombok.Data;
import org.apache.log4j.Logger;

@Data
public class ServerConnectionController implements Initializable, LangSwitcher {

    private ClientUtils clientUtils;

    public ServerConnectionController(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
    }

    @FXML
    private ResourceBundle resources;
    public static final Logger LOGGER = Logger.getLogger(ServerConnectionController.class.getName());
    @FXML
    private JFXTextField serverAddress;

    @FXML
    private JFXTextField port;

    @FXML
    private StackPane connectionPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault());
        loadServerElements();
    }

    @FXML
    public void serverConnection() {
        if (validAddress(serverAddress.getText()) & validPort(port.getText())) {
            try {
                clientUtils.clientProviding().setSocketAddress(serverAddress.getText(), Integer.parseInt(port.getText()));
                if (clientUtils.clientProviding().testConnect()) {
                    loadLoginRegister(resources);
                } else {
                    AlertMaker.showErrorMessage("Connection error", "Failed to connect to server");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                AlertMaker.showErrorMessage("Connection error", "Incorrect address");
            } catch (IOException e) {
                AlertMaker.showErrorMessage("Connection error", e.getClass().getName());
            }
        }

    }

    public void loadLoginRegister(ResourceBundle resourceBundle) {
        try {
            LoginRegisterController loginRegisterController= new LoginRegisterController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menus/log_connection_menu.fxml"));
            loader.setController(loginRegisterController);
            loader.setResources(resourceBundle);
            Parent ro = loader.load();
            StackPane.setAlignment(ro, Pos.CENTER);
            connectionPane.getChildren().setAll(ro);

            LangController langController = new LangController(loginRegisterController);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/lang_menu.fxml"));
            fxmlLoader.setController(langController);
            fxmlLoader.setResources(resourceBundle);
            Parent root = fxmlLoader.load();
            StackPane.setAlignment(root, Pos.BOTTOM_RIGHT);
            connectionPane.getChildren().addAll(root);
        } catch (IOException e) {
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    private boolean validAddress(String string) {
        if (clientUtils.userManager().checkStringRegex(string, "^\\d{1,3}[.]{1}\\d{1,3}[.]{1}\\d{1,3}[.]{1}\\d{1,3}$")) {
            serverAddress.setUnFocusColor(Paint.valueOf("black"));
            serverAddress.setPromptText((resources.getString("server.textField.serverAddress")));
            return true;
        } else {
            serverAddress.setUnFocusColor(Paint.valueOf("red"));
            serverAddress.setPromptText(resources.getString("server.textField.error.serverAddress"));
            return false;
        }

    }

    private boolean validPort(String string) {
        if (clientUtils.userManager().checkIntInputWithParameters(string, 1, 65535)) {
            port.setUnFocusColor(Paint.valueOf("black"));
            port.setPromptText(resources.getString("server.textField.port"));
            return true;
        } else {
            port.setUnFocusColor(Paint.valueOf("red"));
            port.setPromptText(resources.getString("server.textField.error.port"));
            return false;
        }

    }

    private void loadLangElements() throws IOException {
        LangController langController = new LangController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/lang_menu.fxml"));
        fxmlLoader.setController(langController);
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        StackPane.setAlignment(root, Pos.BOTTOM_RIGHT);
        connectionPane.getChildren().addAll(root);
    }

    public void loadServerElements() {
        try {
            ServerMenuController serverMenuController = new ServerMenuController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menus/server_connection_menu.fxml"));
            loader.setController(serverMenuController);
            loader.setResources(resources);
            Parent ro = loader.load();
            serverAddress = serverMenuController.getServerAddress();
            port = serverMenuController.getPort();
            StackPane.setAlignment(ro, Pos.CENTER);
            connectionPane.getChildren().setAll(ro);

            loadLangElements();

        } catch (IOException e) {
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    @Override
    public void switchLanguage() {
        resources = ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault());
        loadServerElements();
    }


}
