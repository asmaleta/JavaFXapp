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
import java.util.*;

@Data
public class VisualizationController implements Initializable, LangSwitcher {
    public static final Logger LOGGER = Logger.getLogger(VisualizationController.class.getName());
    @FXML
    private Pane wrapperMapPane;
    private AbsResizableCanvas routeMapCanvas;
    private ResourceBundle resource;
    private AppPane appPane;
    private HashSet<Route> newRoutes;
    private HashSet<Route> updatesRoutes;
    private HashSet<Route> deleteRoutes;
    private HashSet <Route> routeHashSet;
    private volatile Route selectedRoute;
    private ClientUtils clientUtils;

    public VisualizationController(AppPane appPanelController) {
        this.appPane = appPanelController;
        routeHashSet = new HashSet<>();
        newRoutes = new HashSet<>();
        updatesRoutes = new HashSet<>();
        deleteRoutes= new HashSet<>();
        clientUtils = appPanelController.getClientUtils();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resource = resources;
        routeMapCanvas = new ResizableMapCanvas(routeHashSet, wrapperMapPane);
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

        if (needAddRoute()) {
            Platform.runLater(() -> {
            checkForAnimate();
        });
        }else {
            refreshMap();
        }
    }

    private void checkForAnimate() {
        for (Route add: newRoutes){
            routeMapCanvas.animateEntry(add);
        }
        newRoutes.clear();
        for (Route rem: deleteRoutes){
            routeMapCanvas.animateRemove(rem);
        }
        deleteRoutes.clear();
        for (Route update: updatesRoutes){
            routeMapCanvas.animateUpdate(selectedRoute,update);
            selectedRoute.setCoordinates(update.getCoordinates());
            selectedRoute.setDistance(update.getDistance());
            appPane.getRouteInfoController().displayRoute(update);
        }
        updatesRoutes.clear();
    }
    void  refreshMap (){
        routeHashSet.clear();
        routeHashSet.addAll(appPane.getClientUtils().clientCollectionManager().getRouteHashSet());
        routeMapCanvas.setObj(appPane.getClientUtils().clientCollectionManager().getRouteHashSet());
        Platform.runLater(() -> {
            routeMapCanvas.draw();
        });
    }

    public boolean needAddRoute() {
        Object response = clientUtils.clientProviding().dataExchangeWithServer("get_collection", null, null).getAns();
        if (response instanceof List) {
            List<Route> routes = (List<Route>) response;
            if (!clientUtils.clientCollectionManager().equals(new ClientCollectionManager((routes)))) {

                clientUtils.clientCollectionManager().getRouteList().clear();
                clientUtils.clientCollectionManager().getRouteList().addAll((List<Route>) response);
                if (routes.size() != routeHashSet.size()) {
                    newRoutes.clear();
                    newRoutes.addAll(routes); /// new - old
                    newRoutes.removeAll(routeHashSet);
                    deleteRoutes.clear();
                    deleteRoutes.addAll(routeHashSet);// old - new
                    deleteRoutes.removeAll(routes);
                    System.out.println("add");
                    System.out.println(newRoutes);
                    System.out.println("delete");
                    System.out.println(deleteRoutes);
                }else {
                    updatesRoutes.clear();
                    updatesRoutes.addAll(routes);// old - new
                    updatesRoutes.removeAll(routeHashSet);
                    System.out.println("update");
                    System.out.println(updatesRoutes);
                }
                routeHashSet.clear();
                routeHashSet.addAll(routes);
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
