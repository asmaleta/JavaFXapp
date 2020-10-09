package itmo.gui.controllers.app;

import com.jfoenix.controls.JFXButton;
import itmo.gui.controllers.app.lisroutes.TableRoutesController;
import itmo.gui.controllers.app.main.MainController;
import itmo.gui.controllers.app.visualization.VisualizationController;
import itmo.gui.controllers.lang.LangSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;
@Data
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

    private MainController mainController;
    private TableRoutesController tableRoutesController;
    private VisualizationController visualizationController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    private void loadAppPanel() {

    }
    @FXML
    private void loadMain() {
        appPane.loadMain();
    }
    @FXML
    private void loadListObj() {
        appPane.loadListObj();
    }
    @FXML
    private void loadVisualization() {
        appPane.loadVisualization();
    }
    @Override
    public void switchLanguage() {

    }
}
