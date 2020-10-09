package itmo.gui.controllers.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import itmo.gui.AlertMaker;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.server.ServerConnectionController;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lab6common.data.Driver;
import lab6common.data.Package;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginRegisterController implements Initializable, LangSwitcher {
    private ClientUtils clientUtils;
    private ServerConnectionController serverConnectionController;
    private ResourceBundle resources;
    private StackPane connectionPane;

    public LoginRegisterController(ServerConnectionController serverConnectionController) {
        this.clientUtils = serverConnectionController.getClientUtils();
        this.serverConnectionController = serverConnectionController;
        this.connectionPane = serverConnectionController.getConnectionPane();
    }

    @FXML
    private VBox loginMenuBarPane;

    @FXML
    private Text title;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton register;

    private boolean validTextField(JFXTextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "^\\S{1,}")) {
            field.setUnFocusColor(Paint.valueOf("black"));
            field.setPromptText((resources.getString("login.textField.username")));
            return true;
        } else {
            field.setUnFocusColor(Paint.valueOf("red"));
            field.setPromptText(resources.getString("login.textField.error.username"));
            return false;
        }

    }

    private boolean validPasswordField(JFXPasswordField button) {
        if (clientUtils.userManager().checkStringRegex(button.getText(), "^\\S{1,}")) {
            button.setUnFocusColor(Paint.valueOf("black"));
            button.setPromptText((resources.getString("login.textField.password")));
            return true;
        } else {
            button.setUnFocusColor(Paint.valueOf("red"));
            button.setPromptText(resources.getString("login.textField.error.password"));
            return false;
        }

    }

    @FXML
    private void validLoginTextField() {
        if (validTextField(username) & validPasswordField(password)) {
            login.setDisable(true);
            register.setDisable(true);
                clientUtils.userManager().setDriver(new Driver(username.getText(), password.getText()));
                Package ans = clientUtils.clientProviding().dataExchangeWithServer("check_driver", null,null );
                Driver driver = (Driver) ans.getAns();
                if (driver.getId() == -1) {
                    login.setDisable(false);
                    register.setDisable(false);
                    AlertMaker.showErrorMessage("Driver no exist", null);
                } else {
                    clientUtils.userManager().setDriver(driver);
                    loadApp();
                }

        }
    }

    @FXML
    private void validRegisterTextField() {
        if (validTextField(username) & validPasswordField(password)) {
            login.setDisable(true);
            register.setDisable(true);

            clientUtils.userManager().setDriver(new Driver(username.getText(), password.getText()));
            Package ans = clientUtils.clientProviding().dataExchangeWithServer("registration", null,null );
            Driver driver = (Driver) ans.getAns();
            if (driver.getId() == -1) {
                login.setDisable(false);
                register.setDisable(false);
                AlertMaker.showErrorMessage("Driver exist", null);
            } else {
                clientUtils.userManager().setDriver(driver);
                loadApp();
            }
        }
    }

    void loadApp() {
        ((Stage) connectionPane.getScene().getWindow()).close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/panes/application_pane.fxml"));
            loader.setController(new AppPane(clientUtils));
            loader.setResources(resources);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Navigator");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertMaker.showErrorMessage("Load fxml error", null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault());
    }

    @Override
    public void switchLanguage() {
        resources = ResourceBundle.getBundle(ClientUtils.resourceBundlePath, Locale.getDefault());
        serverConnectionController.loadLoginRegister(resources);
    }
}
