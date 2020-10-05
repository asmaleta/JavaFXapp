package itmo.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import itmo.ClientApp;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
@Data
public class ServerMenuController implements Initializable {
    private static ClientUtils clientUtils;

    private ServerConnectionController serverConnectionController;
    public ServerMenuController(ServerConnectionController serverConnectionController) {
        this.clientUtils = ClientApp.getClientUtils();
        this.serverConnectionController = serverConnectionController;
    }

    @FXML
    private VBox connectionMenuBarPane;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Text title;

    @FXML
    private JFXTextField serverAddress;

    @FXML
    private JFXTextField port;

    @FXML
    private JFXButton connect;
    @FXML
    private void serverConnection(){
        serverConnectionController.serverConnection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }
}
