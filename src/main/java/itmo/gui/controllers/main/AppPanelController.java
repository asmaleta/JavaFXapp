package itmo.gui.controllers.main;

import com.jfoenix.controls.JFXButton;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AppPanelController implements Initializable, LangSwitcher {


    private AppPane appPane;

    public AppPanelController(AppPane appPane) {
        this.appPane = appPane;
    }
    private ResourceBundle resources;
    @FXML
    private JFXButton main;

    @FXML
    private JFXButton listObj;

    @FXML
    private JFXButton visualization;
    @FXML
    private HBox appPanel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        main.setDisable(true);
    }

    private void loadAppPanel() {

    }

    @Override
    public void switchLanguage() {

    }
}
