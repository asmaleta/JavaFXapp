package itmo.gui.controllers.app.lisroutes;

import com.jfoenix.controls.JFXTextField;
import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientCollectionManager;
import itmo.utils.ClientUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab6common.generatedclasses.Route;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.INACTIVE;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TableRoutesController implements Initializable, LangSwitcher {
    public static final Logger LOGGER = Logger.getLogger(TableRoutesController.class.getName());

    private final ObservableList<Route> routeList = FXCollections.observableArrayList();
    @FXML
    private TableView<Route> routesTable;

    @FXML
    private TableColumn<Route, Integer> idCol;

    @FXML
    private TableColumn<Route, String> nameCol;

    @FXML
    private TableColumn<Route, Long > corXCol;

    @FXML
    private TableColumn<Route, Integer> corYCol;

    @FXML
    private TableColumn<Route, String> locToName;

    @FXML
    private TableColumn<Route, Long> locToX;

    @FXML
    private TableColumn<Route, Long> locToY;

    @FXML
    private TableColumn<Route, String> locFromName;

    @FXML
    private TableColumn<Route, Long> locFromX;

    @FXML
    private TableColumn<Route, Long> locFromY;

    @FXML
    private TableColumn<Route, Float> distanceCol;
    @FXML
    private TableColumn<Route, ZonedDateTime> dateCol;
    @FXML
    private JFXTextField serach;
    private AppPane appPane;
    private ClientUtils clientUtils;

    public TableRoutesController(AppPane appPanelController) {
        this.appPane = appPanelController;
        this.clientUtils = appPanelController.getClientUtils();



    }
    private ResourceBundle resources;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        initializeCol();
         updateData();
    }
    private void loadFilteringOption() {
        // Filter commands by name functionality
        FilteredList<Route> filteredData = new FilteredList<>(routeList, s -> true);
        serach.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(route -> {
                if (newVal == null || newVal.length() == 0)
                    return true;

                String writtenText = newVal.toLowerCase();
                if (String.valueOf(route.getName()).contains(writtenText))
                    return true;
                else if (String.valueOf(route.getId()).contains(writtenText))
                    return true;
                else if (String.valueOf(route.getDistance()).contains(writtenText))
                    return true;
                else
                    return false;
            });
        });
        SortedList<Route> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(routesTable.comparatorProperty());
        routesTable.setItems(sortedData);
    }

    private boolean updateLocalCollection() {
        Object response = clientUtils.clientProviding().dataExchangeWithServer("get_collection",null,null ).getAns();
        if (response instanceof List){
            List<Route> routes = (List<Route>) response;
            if (! clientUtils.clientCollectionManager().equals(new ClientCollectionManager((routes)))){
                clientUtils.clientCollectionManager().getRouteList().clear();
                clientUtils.clientCollectionManager().getRouteList().addAll((List<Route>) response);
                LOGGER.log(Level.INFO, "Collection updated");

                return true;
            }
        }
        return false;
    }
    @FXML
    private void getInfoRoute(){
            appPane.getRouteInfoController().displayRoute(routesTable.getSelectionModel().getSelectedItem());
    }
    private void initializeCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        corXCol.setCellValueFactory(new PropertyValueFactory<>("corX"));
        corYCol.setCellValueFactory(new PropertyValueFactory<>("corY"));
        locToName.setCellValueFactory(new PropertyValueFactory<>("toName"));
        locToX.setCellValueFactory(new PropertyValueFactory<>("toX"));
        locToY.setCellValueFactory(new PropertyValueFactory<>("toY"));
        locFromName.setCellValueFactory(new PropertyValueFactory<>("fromName"));
        locFromX.setCellValueFactory(new PropertyValueFactory<>("fromX"));
        locFromY.setCellValueFactory(new PropertyValueFactory<>("fromY"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        dateCol.setCellFactory(column -> getFormatedDate());
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
    }
    private TableCell<Route, ZonedDateTime> getFormatedDate() {
        return new TableCell<Route, ZonedDateTime>() {
            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                    this.setText(DateTimeFormatter.ofPattern("dd/MMM HH:mm z").withLocale(Locale.getDefault()).format(item));
            }
        };
    }

    public synchronized void updateTable(){
        if (updateLocalCollection()){
            Platform.runLater(() -> {
                updateData();
            });
            }
        }
    public void updateData(){
        routeList.clear();
        routeList.addAll(appPane.getClientUtils().clientCollectionManager().getRouteList());
        routesTable.setItems(routeList);
        loadFilteringOption();

    }

    @Override
    public void switchLanguage() {
        appPane.setResources( ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault()));
        appPane.loadListObj();
    }
}
