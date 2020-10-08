package itmo.gui.controllers.main;

import itmo.ClientApp;
import itmo.gui.controllers.lang.LangController;
import itmo.utils.ClientUtils;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private ClientUtils clientUtils;
    private ResourceBundle resources;
    private AppPanelController appPanelController;
    private LangController langController;
    public MainController(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
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
}
