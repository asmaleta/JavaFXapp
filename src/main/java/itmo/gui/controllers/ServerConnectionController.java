package itmo.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import itmo.ClientApp;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import itmo.utils.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

public class ServerConnectionController implements Initializable {

    private static ClientUtils clientUtils;

    public ServerConnectionController() {
        this.clientUtils = ClientApp.getClientUtils();
    }

    public static final Logger LOGGER = Logger.getLogger(ServerConnectionController.class.getName());
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane connectionPane;

    @FXML
    private Text title;

    @FXML
    private JFXTextField serverAddress;

    @FXML
    private JFXTextField port;

    @FXML
    private JFXButton connect;


    @FXML
    private void serverConnection() {
        if (validAddress(serverAddress.getText()) & validPort(port.getText())) {
            clientUtils.clientProviding().setSocketAddress(serverAddress.getText(),Integer.parseInt(port.getText()));
            try {
                if (clientUtils.clientProviding().testConnect()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/log_connection_menu.fxml"));
                    //fxmlLoader.setController(new LoginRegisterController(clientUtils));
                    fxmlLoader.setResources(resources);
                    Parent root = fxmlLoader.load();
                    connectionPane.getChildren().setAll(root);
                }else{
                    AlertMaker.showErrorMessage("Connection error", "Failed to connect to server");
                }
            } catch (RuntimeException e) {
                AlertMaker.showErrorMessage("Connection error", "Incorrect address");
            } catch (IOException e) {
               AlertMaker.showErrorMessage("Connection error", e.getClass().getName());
            }
        }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        loadElements();
    }

    private void loadElements() {

    }
}
