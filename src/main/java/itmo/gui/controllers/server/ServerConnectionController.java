package itmo.gui.controllers.server;


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
import javafx.scene.layout.StackPane;
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
    private StackPane connectionPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault());
        loadServerElements();
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
