package itmo.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginRegisterController {
    public LoginRegisterController() {
    }

    @FXML
    private AnchorPane loginPane;

    @FXML
    private VBox loginMenuBarPane;

    @FXML
    private Text title;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton register;

    @FXML
    private StackPane langMenuBarPane;

    @FXML
    private Menu menuLang;

    @FXML
    private RadioMenuItem ru;

    @FXML
    private RadioMenuItem en;

    @FXML
    private RadioMenuItem nl;

    @FXML
    private RadioMenuItem se;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
    }
}
