package itmo.gui.controllers.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import itmo.gui.AlertMaker;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lab6common.generatedclasses.Coordinates;
import lab6common.generatedclasses.Location;
import lab6common.generatedclasses.Route;

import java.net.URL;
import java.util.ResourceBundle;

public class AddFormController implements Initializable {
    @FXML
    private Label addFormLabel;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField coordidinateX;

    @FXML
    private JFXTextField coordidinateY;

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
    private JFXTextField distance;
    @FXML
    private JFXButton send;

    private ResourceBundle resources;
    private ClientUtils clientUtils;
   private RouteInfoController routeInfoController;
   private Stage stage;
    public AddFormController(RouteInfoController routeInfoController, Stage stage) {
        this.clientUtils = routeInfoController.getClientUtils();
        this.routeInfoController = routeInfoController;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @FXML
    private void sendButton() {
        if (validStringInput(name) & validLongInput(coordidinateX) & validIntInput(coordidinateY)
                & validStringInput(locationToName) & validLongInput(locationToX) & validLongInput(locationToY)
                & validStringInput(locationFromName) & validLongInput(locationFromX) & validLongInput(locationFromY)
                & validFloatInput(distance)) {
            Route route = new Route(name.getText(),
                    new Coordinates(Long.parseLong(coordidinateX.getText()),
                            Integer.parseInt(coordidinateY.getText())),
                    new Location(Long.parseLong(locationFromX.getText()),
                            Long.parseLong(locationFromY.getText()),locationFromName.getText()),
                    new Location(Long.parseLong(locationToX.getText()),
                            Long.parseLong(locationToY.getText()),locationToName.getText()),
                    Float.parseFloat(distance.getText()));
            Object response = clientUtils.clientProviding().dataExchangeWithServer("add", null,
                    route).getAns();
            if (!(response instanceof Integer)){
                AlertMaker.showErrorMessage("Load fxml error",(String) response);
            }
           stage.close();
        }
    }

    private boolean validStringInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "\\S{1,}")) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("addForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("addForm.error.textField.message"));
            return false;
        }

    }

    private boolean validLongInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}")
                && clientUtils.userManager().checkLongInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("addForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("addForm.error.textField.message"));
            return false;
        }

    }

    private boolean validFloatInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}[.]{1}\\d{1,}")
                && clientUtils.userManager().checkFloatInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("addForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("addForm.error.textField.message"));
            return false;
        }

    }
    private boolean validIntInput(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}")
                && clientUtils.userManager().checkIntInput(field.getText())) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("addForm.textField." + field.getId())));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("addForm.error.textField.message"));
            return false;
        }

    }
}
