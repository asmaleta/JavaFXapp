package itmo.gui.controllers.app.main;

import itmo.gui.controllers.app.AppPane;
import itmo.gui.controllers.app.AppPanelController;
import itmo.gui.controllers.lang.LangController;
import itmo.gui.controllers.lang.LangSwitcher;
import itmo.utils.ClientUtils;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable, LangSwitcher {

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
