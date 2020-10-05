package itmo.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import itmo.ClientApp;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginRegisterController implements Initializable, LangSwitcher{
    private static ClientUtils clientUtils;
    private ServerConnectionController serverConnectionController;
    private ResourceBundle resources;
    private AnchorPane connectionPane;

    public LoginRegisterController(ServerConnectionController serverConnectionController) {
        this.clientUtils = ClientApp.getClientUtils();
        this.serverConnectionController = serverConnectionController;
        this.connectionPane = serverConnectionController.getConnectionPane();
    }

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
        this.resources = resources;
    }

    @Override
    public void switchLanguage(String id) {
        Locale locale = Locale.forLanguageTag(id);
        resources = ResourceBundle.getBundle("languages.Langs", locale);
        loadElements();
    }

    private void loadElements() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menus/log_connection_menu.fxml"));
            loader.setController(this);
            loader.setResources(resources);
            Parent ro = loader.load();
            connectionPane.getChildren().setAll(ro);

            LangController langController = new LangController(this);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/lang_menu.fxml"));
            fxmlLoader.setController(langController);
            fxmlLoader.setResources(ResourceBundle.getBundle("languages.Langs", resources.getLocale()));
            Parent root = fxmlLoader.load();
            AnchorPane.setBottomAnchor(root,0d);
            AnchorPane.setRightAnchor(root,0d);
            connectionPane.getChildren().addAll(root);

        }catch (IOException e){
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }
}
