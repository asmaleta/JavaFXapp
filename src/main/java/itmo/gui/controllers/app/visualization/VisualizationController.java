package itmo.gui.controllers.app.visualization;

import itmo.gui.canvas.AbsResizableCanvas;
import itmo.gui.canvas.ResizableMapCanvas;
import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientCollectionManager;
import itmo.utils.ClientUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import lab6common.generatedclasses.Route;
import lombok.Data;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
@Data
public class VisualizationController implements Initializable, LangSwitcher {
    public static final Logger LOGGER = Logger.getLogger(VisualizationController.class.getName());
    @FXML
    private Pane wrapperMapPane;
    private AbsResizableCanvas routeMapCanvas;
    private ResourceBundle resource;
    private AppPane appPane;
    private List<Route> routeList;
    private Route selectedRoute;
    private ClientUtils clientUtils;

    public VisualizationController(AppPane appPanelController) {
        this.appPane = appPanelController;
        routeList = new ArrayList<>();
        clientUtils = appPanelController.getClientUtils();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resource = resources;
        routeMapCanvas = new ResizableMapCanvas((ArrayList<Route>) routeList, wrapperMapPane);
        wrapperMapPane.getChildren().add(routeMapCanvas);
        routeMapCanvas.widthProperty().bind(wrapperMapPane.widthProperty());
        routeMapCanvas.heightProperty().bind(wrapperMapPane.heightProperty());
        routeMapCanvas.setOnMouseClicked(event -> {
            selectedRoute = (Route) routeMapCanvas.findObj(event.getSceneX(), event.getSceneY());
            appPane.getRouteInfoController().displayRoute(selectedRoute);
        });
        updateData();
    }

    public void updateData() {

        if (needAddRoute()&& routeList.size() > 0) {
            Platform.runLater(() -> {
            checkForNewRoutes();
        });
        }else {
            routeList.clear();
            routeList.addAll(appPane.getClientUtils().clientCollectionManager().getRouteList());
            routeMapCanvas.setObj(appPane.getClientUtils().clientCollectionManager().getRouteList());
            routeMapCanvas.draw();
        }

    }

    private void checkForNewRoutes() {
        List<Route> diff = new ArrayList<>();
        outer:
        for (Route fetched : appPane.getClientUtils().clientCollectionManager().getRouteList()) {
            for (Route elemMap : routeList)
                if (fetched.equals(elemMap))
                    continue outer;
            diff.add(fetched);
        }

        for (Route newElem : diff) {
            System.out.println(newElem.toString());
            routeList.add(newElem);
            ((List<Route>) routeMapCanvas.getObj()).add(newElem);
            routeMapCanvas.animateEntry(newElem);
        }
    }

    public boolean needAddRoute() {
        Object response = clientUtils.clientProviding().dataExchangeWithServer("get_collection", null, null).getAns();
        if (response instanceof List) {
            List<Route> routes = (List<Route>) response;
            if (!clientUtils.clientCollectionManager().equals(new ClientCollectionManager((routes)))) {
                clientUtils.clientCollectionManager().getRouteList().clear();
                clientUtils.clientCollectionManager().getRouteList().addAll((List<Route>) response);
                LOGGER.log(Level.INFO, "Collection updated");

                return true;
            }
        }
        return false;
    }

    @Override
    public void switchLanguage() {
        appPane.setResources( ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault()));
        appPane.loadVisualization();
    }
}
