package itmo.gui.controllers;

import itmo.ClientApp;
import itmo.utils.ClientUtils;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static ClientUtils clientUtils;
    private ResourceBundle resourceBundle;

    public MainController() {
        this.clientUtils = ClientApp.getClientUtils();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        loadElements();
    }

    private void loadElements() {
    }

    public void changeLanguage(String languageCode) {
        Locale locale = Locale.forLanguageTag(languageCode);
        resourceBundle = ResourceBundle.getBundle("bundles.LangBundle", locale);
        loadElements();
    }
}
