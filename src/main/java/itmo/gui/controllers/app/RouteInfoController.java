package itmo.gui.controllers.app;

import com.jfoenix.controls.JFXTextField;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lab6common.generatedclasses.Coordinates;
import lab6common.generatedclasses.Location;
import lab6common.generatedclasses.Route;
import lombok.Data;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
@Data
public class RouteInfoController implements Initializable {

    public static final Logger LOGGER = Logger.getLogger(RouteInfoController.class.getName());
    @FXML
    private Label addFormLabel;

    @FXML
    private JFXTextField id;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField coordinateX;

    @FXML
    private JFXTextField coordinateY;

    @FXML
    private JFXTextField locationToName;

    @FXML
    private JFXTextField locationToX;

    @FXML
    private JFXTextField locationToY;

    @FXML
    private JFXTextField locationFromName;

    @FXML
    private JFXTextField locationFromX;

    @FXML
    private JFXTextField locationFromY;
    @FXML
    private JFXTextField date;
    @FXML
    private JFXTextField distance;
    private AppPane appPane;
    private ClientUtils clientUtils;
    private ResourceBundle resources;
    private Route selectedRoute;
    public RouteInfoController(AppPane appPane) {
        this.appPane = appPane;
        this.clientUtils = appPane.getClientUtils();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    public void displayRoute(Route route) {
        selectedRoute = route;
        System.out.println(route);
        id.setText(route.getId().toString());
        name.setText(route.getName());
        coordinateX.setText(route.getCorX().toString());
        coordinateY.setText(route.getCorY().toString());
        locationFromName.setText(route.getFromName());
        locationFromX.setText(route.getFromX().toString());
        locationFromY.setText(route.getFromY().toString());
        locationToName.setText(route.getToName());
        locationToX.setText(route.getToX().toString());
        locationToY.setText(route.getToY().toString());
        distance.setText(route.getDistance().toString());
        date.setText(route.getCreationDate().format(
                DateTimeFormatter.ofPattern("dd/MMM HH:mm z")
                        .withLocale(Locale.getDefault())));
    }

    @FXML
    private void buttonAddCommand() {
        try{
            Stage stage = new Stage();
            AddFormController addFormController = new AddFormController(this,stage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/forms/route_form.fxml"));
            loader.setController(addFormController);
            loader.setResources(resources);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }
    @FXML
    private void buttonRemoveCommand() {
            Object response = clientUtils.clientProviding().dataExchangeWithServer("remove_by_id", selectedRoute.getId().toString(),null).getAns();
            if (response instanceof Integer){
                LOGGER.log(Level.INFO, "Success delete "+ response);
            }else{
                AlertMaker.showErrorMessage("Request ex", (String) response);
            }
    }
    @FXML
    private void buttonClearCommand() {
        Object response = clientUtils.clientProviding().dataExchangeWithServer("clear", null,null).getAns();
        AlertMaker.showSimpleAlert("Message", (String) response);
    }
    @FXML
    private void buttonChangeCommand() {
        if (validStringInput(name) & validLongInput(coordinateX) & validIntInput(coordinateY)
                & validStringInput(locationToName) & validLongInput(locationToX) & validLongInput(locationToY)
                & validStringInput(locationFromName) & validLongInput(locationFromX) & validLongInput(locationFromY)
                & validFloatInput(distance)) {
            Route route = new Route(name.getText(),
                    new Coordinates(Long.parseLong(coordinateX.getText()),
                            Integer.parseInt(coordinateY.getText())),
                    new Location(Long.parseLong(locationFromX.getText()),
                            Long.parseLong(locationFromY.getText()),locationFromName.getText()),
                    new Location(Long.parseLong(locationToX.getText()),
                            Long.parseLong(locationToY.getText()),locationToName.getText()),
                    Float.parseFloat(distance.getText()));
            Object response = clientUtils.clientProviding().dataExchangeWithServer("update_id", selectedRoute.getId().toString(),
                    route).getAns();
            if (response instanceof Integer){
                LOGGER.log(Level.INFO, "Success update "+ response);
            }else{
                AlertMaker.showErrorMessage("Request ex", (String) response);
            }
        }
    }


    private boolean validStringInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "\\S{1,}")) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("infoForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("infoForm.error.textField.message"));
            return false;
        }

    }

    private boolean validLongInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}")
                && clientUtils.userManager().checkLongInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("infoForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("infoForm.error.textField.message"));
            return false;
        }

    }

    private boolean validFloatInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}[.]{1}\\d{1,}")
                && clientUtils.userManager().checkFloatInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("infoForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("infoForm.error.textField.message"));
            return false;
        }

    }
    private boolean validIntInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}")
                && clientUtils.userManager().checkIntInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("infoForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("infoForm.error.textField.message"));
            return false;
        }

    }
}
