package itmo.gui.controllers.server;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import itmo.gui.AlertMaker;
import itmo.gui.controllers.server.ServerConnectionController;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;
@Data
public class ServerMenuController implements Initializable {
    private ClientUtils clientUtils;

    private ServerConnectionController serverConnectionController;
    public ServerMenuController(ServerConnectionController serverConnectionController) {
        this.clientUtils = serverConnectionController.getClientUtils();
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
        if (validAddress(serverAddress.getText()) & validPort(port.getText())) {
            try {
                clientUtils.clientProviding().setSocketAddress(serverAddress.getText(), Integer.parseInt(port.getText()));
                Object connect = clientUtils.clientProviding().dataExchangeWithServer("test_connect",null,null).getAns();
                if (connect  instanceof Boolean) {
                   serverConnectionController.loadLoginRegister(resources);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                AlertMaker.showErrorMessage("Connection error", "Incorrect address");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
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

}
