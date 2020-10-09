package itmo.gui.controllers.app.main;

import com.jfoenix.controls.JFXButton;
import itmo.gui.AlertMaker;
import itmo.gui.controllers.app.AddFormController;
import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.app.AppPanelController;
import itmo.gui.controllers.lang.LangController;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
@Data
public class MainController implements Initializable, LangSwitcher {
    @FXML
    private JFXButton filter_less_than_distance;

    @FXML
    private JFXButton info;

    @FXML
    private JFXButton sum_of_distance;

    @FXML
    private JFXButton history;

    @FXML
    private JFXButton execute_script;

    @FXML
    private JFXButton remove_greater;

    @FXML
    private JFXButton remove_lower;

    @FXML
    private TextField arg;

    @FXML
    private TextArea output;
    private ClientUtils clientUtils;
    private ResourceBundle resources;
    private AppPanelController appPanelController;
    private LangController langController;


    public MainController(AppPane appPane) {
        this.clientUtils = appPane.getClientUtils();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        loadElements();
    }

    private void loadElements() {

    }

    public void changeLanguage(String languageCode) {
        Locale locale = Locale.forLanguageTag(languageCode);
        resources = ResourceBundle.getBundle("bundles.LangBundle", locale);
        loadElements();
    }

    @Override
    public void switchLanguage() {

    }
    @FXML
    private void infoButton(){
        Object response = clientUtils.clientProviding().dataExchangeWithServer("info",null,null).getAns();
        output.setText((String) response);
    }
    @FXML
    private void historyButton(){
        Object response = clientUtils.clientProviding().dataExchangeWithServer("history",null,null).getAns();
        output.setText((String) response);
    }
    @FXML
    private void filterLessThanDistanceButton(){
        if (validFloatInput(arg)) {
            Object response = clientUtils.clientProviding().dataExchangeWithServer("filter_less_than_distance", arg.getText(), null).getAns();
            output.setText((String) response);
        }
    }

    private boolean validFloatInput(TextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "-?\\d{1,}[.]{1}\\d{1,}")
                && clientUtils.userManager().checkFloatInput(field.getText())) {
            field.getStyleClass().remove("error");
            field.setPromptText((resources.getString("commands.textField.arg")));
            return true;
        } else {
            field.getStyleClass().setAll("error");
            field.setPromptText(resources.getString("commands.textField.error.arg"));
            return false;
        }

    }
    @FXML
    private void sumOfDistanceButton(){
        Object response = clientUtils.clientProviding().dataExchangeWithServer("sum_of_distance", null, null).getAns();
        output.setText((String) response);
    }
    @FXML
    private void removeGreaterButton(){
        try{
            Stage stage = new Stage();
            AddFormController addFormController = new AddFormController(this,"remove_greater",stage);
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
    private void removeLowerButton(){
        try{
            Stage stage = new Stage();
            AddFormController addFormController = new AddFormController(this,"remove_lower",stage);
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
    public void displayAddRequest(Object response){
        output.setText((String) response);
    }@FXML
    private void scriptButton(){
        if(validStringInput(arg)){
            if(clientUtils.userManager().scriptModeOn(arg.getText())){
                clientUtils.userManager().readRequests(output,clientUtils.clientProviding());
            }
        }

    }
    private boolean validStringInput(TextField field) {
        if (clientUtils.userManager().checkStringRegex(field.getText(), "\\S{1,}")) {
            field.getStyleClass().remove("error");
            field.setPromptText((resources.getString("commands.textField.arg")));
            return true;
        } else {
            field.getStyleClass().setAll("error");
            field.setPromptText(resources.getString("commands.textField.error.arg"));
            return false;
        }

    }



}
