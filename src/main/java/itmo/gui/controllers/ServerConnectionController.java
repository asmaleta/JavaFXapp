package itmo.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

import itmo.ClientApp;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import itmo.utils.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import lombok.Data;
import org.apache.log4j.Logger;
@Data
public class ServerConnectionController implements Initializable, LangSwitcher {

    private static ClientUtils clientUtils;

    public ServerConnectionController() {
        this.clientUtils = ClientApp.getClientUtils();
    }

    public static final Logger LOGGER = Logger.getLogger(ServerConnectionController.class.getName());
    @FXML
    private ResourceBundle resources;
    @FXML
    private JFXTextField serverAddress;

    @FXML
    private JFXTextField port;

    @FXML
    private AnchorPane connectionPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        loadElements();
    }
    @FXML
    public void serverConnection() {
        if (validAddress(serverAddress.getText()) & validPort(port.getText())) {
            try {
                clientUtils.clientProviding().setSocketAddress(serverAddress.getText(), Integer.parseInt(port.getText()));
                if (clientUtils.clientProviding().testConnect()) {
                    loadLoginRegister();
                } else {
                    AlertMaker.showErrorMessage("Connection error", "Failed to connect to server");
                }
            } catch (RuntimeException e) {
                AlertMaker.showErrorMessage("Connection error", "Incorrect address");
            } catch (IOException e) {
                AlertMaker.showErrorMessage("Connection error", e.getClass().getName());
            }
        }

    }

    private void loadLoginRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/log_connection_menu.fxml"));
        fxmlLoader.setController(new LoginRegisterController(this));
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        connectionPane.getChildren().setAll(root);
    }

    private boolean validAddress(String string) {
        System.out.println("serv");
        if (clientUtils.userManager().checkStringRegex(string, "^\\d{1,3}[.]{1}\\d{1,3}[.]{1}\\d{1,3}[.]{1}\\d{1,3}$")) {
            serverAddress.getStyleClass().remove("error");
            serverAddress.setPromptText((resources.getString("server.textField.serverAddress")));
            return true;
        } else {
            serverAddress.getStyleClass().add("error");
            serverAddress.setPromptText(resources.getString("server.textField.error.serverAddress"));
            return false;
        }

    }

    private boolean validPort(String string) {
        System.out.println("port");
        if (clientUtils.userManager().checkIntInputWithParameters(string, 1, 65535)) {
            port.getStyleClass().remove("error");
            port.setPromptText(resources.getString("server.textField.port"));
            return true;
        } else {
            port.getStyleClass().add("error");
            port.setPromptText(resources.getString("server.textField.error.port"));
            return false;
        }

    }
    private void loadElements() {
        try {
            ServerMenuController serverMenuController = new ServerMenuController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menus/server_connection_menu.fxml"));
            loader.setController(serverMenuController);
            loader.setResources(ResourceBundle.getBundle("languages.Langs", resources.getLocale()));
            Parent ro = loader.load();
            serverAddress = serverMenuController.getServerAddress();
            port = serverMenuController.getPort();
            AnchorPane.setTopAnchor(ro,connectionPane.getPrefHeight()/4);
            AnchorPane.setRightAnchor(ro,connectionPane.getPrefWidth()/3.3);
            connectionPane.getChildren().setAll(ro);



            LangController langController = new LangController(this);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/lang_menu.fxml"));
            fxmlLoader.setController(langController);
            fxmlLoader.setResources(ResourceBundle.getBundle("languages.Langs", resources.getLocale()));
            Parent root = fxmlLoader.load();
            AnchorPane.setBottomAnchor(root,0d);
            AnchorPane.setRightAnchor(root,0d);
            connectionPane.getChildren().addAll(root);


        } catch (IOException e) {
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    @Override
    public void switchLanguage(String id) {
       Locale locale = Locale.forLanguageTag(id);
       resources = ResourceBundle.getBundle("languages.Langs", locale);
       loadElements();
    }
}
