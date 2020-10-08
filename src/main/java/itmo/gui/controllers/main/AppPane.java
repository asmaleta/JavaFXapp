package itmo.gui.controllers.main;

import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Data
public class AppPane implements Initializable {

    @FXML
    private StackPane appPane;
    private ClientUtils clientUtils;
    private ResourceBundle resources;

    public AppPane(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        loadAppPanel();
    }

    private void loadAppPanel() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/panels/app_panel.fxml"));
            loader.setController(new AppPanelController(this));
            loader.setResources(resources);
            Parent root = loader.load();
            StackPane.setAlignment(root, Pos.TOP_LEFT);
            appPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }
}
