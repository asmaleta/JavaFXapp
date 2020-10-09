package itmo.gui.controllers.app.main;

import com.jfoenix.controls.JFXButton;
import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.app.AppPanelController;
import itmo.gui.controllers.lang.LangController;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

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


    public MainController(AppPane clientUtils) {
        //this.clientUtils = clientUtils;
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
}
