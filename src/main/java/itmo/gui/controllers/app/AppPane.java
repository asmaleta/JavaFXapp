package itmo.gui.controllers.app;

import itmo.gui.AlertMaker;
import itmo.gui.controllers.app.lisroutes.TableRoutesController;
import itmo.gui.controllers.app.main.MainController;
import itmo.gui.controllers.app.visualization.VisualizationController;
import itmo.gui.controllers.lang.LangController;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import lombok.Data;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Data
public class AppPane implements Initializable {
    public static final Logger LOGGER = Logger.getLogger(AppPane.class.getName());
    @FXML
    private StackPane appPane;
    private ClientUtils clientUtils;
    private ResourceBundle resources;

    private MainController mainController;
    private volatile TableRoutesController tableRoutesController;
    private VisualizationController visualizationController;
    private AppPanelController appPanelController;
    private LangController langController;
    private RouteInfoController routeInfoController;
    public final CollectionRefresher refresherThread;
    public AppPane(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
        this.refresherThread = new CollectionRefresher();
        refresherThread.setName("CollectionRefresherThread");
        refresherThread.setPriority(Thread.MIN_PRIORITY);
        refresherThread.setDaemon(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        refresherThread.start();
        loadMain();

    }

    public void loadMain() {
        try {
            tableRoutesController = null;
            loadAppPanel();
            this.mainController = new MainController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menus/commands_menu.fxml"));
            loader.setController(mainController);
            loader.setResources(resources);
            Parent root = loader.load();
            StackPane.setAlignment(root, Pos.BOTTOM_CENTER);
            appPane.getChildren().addAll(root);
            loadLangElements(mainController);
            appPanelController.getMain().setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    public void loadListObj() {
        try {
            loadAppPanel();
            this.tableRoutesController = new TableRoutesController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tables/table_routes.fxml"));
            loader.setController(tableRoutesController);
            loader.setResources(resources);
            Parent root = loader.load();
            StackPane.setAlignment(root, Pos.CENTER_LEFT);
            StackPane.setMargin(root, new Insets(10,0,0,10));
            appPane.getChildren().addAll(root);
            loadLangElements(mainController);
            appPanelController.getListObj().setDisable(true);
            loadRouteInfoPanel();
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    public void loadVisualization() {
        tableRoutesController = null;
        loadAppPanel();
        this.visualizationController = new VisualizationController(this);
        appPanelController.getVisualization().setDisable(true);
    }

    private void loadLangElements(LangSwitcher controller) throws IOException {
        langController = new LangController(controller);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/menus/lang_menu.fxml"));
        fxmlLoader.setController(langController);
        fxmlLoader.setResources(resources);
        Parent root = fxmlLoader.load();
        StackPane.setAlignment(root, Pos.BOTTOM_RIGHT);
        appPane.getChildren().addAll(root);
    }
    private void loadRouteInfoPanel() {
        try {
            routeInfoController = new RouteInfoController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/panels/route_info_panel.fxml"));
            loader.setController(routeInfoController);
            loader.setResources(resources);
            Parent root = loader.load();
            StackPane.setAlignment(root, Pos.CENTER_RIGHT);
            appPane.getChildren().addAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    private void loadAppPanel() {
        try {
            AppPanelController appPanelController = new AppPanelController(this);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/panels/app_panel.fxml"));
            loader.setController(appPanelController);
            loader.setResources(resources);
            Parent root = loader.load();
            StackPane.setAlignment(root, Pos.TOP_LEFT);
            appPane.getChildren().setAll(root);
            this.appPanelController = appPanelController;
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }
    public class CollectionRefresher extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                    if (tableRoutesController != null) {
                        tableRoutesController.updateTable();
                    }
                } catch (InterruptedException e) {
                    LOGGER.log(Level.ERROR, "Потоковая ошибка");
                }
            }
        }
    }
}
