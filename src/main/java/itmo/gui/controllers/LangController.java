package itmo.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LangController implements Initializable {
    private LangSwitcher controller;
    public LangController(LangSwitcher controller) {
        this.controller = controller;
    }
    public LangController (){

    }
    @FXML private ToggleGroup languageButtons;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane connectionMenuBarPane;

    @FXML
    private Menu menuLang;

    @FXML
    private RadioMenuItem ru;

    @FXML
    private RadioMenuItem en;

    @FXML
    private RadioMenuItem nl;

    @FXML
    private RadioMenuItem se;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        languageButtons.selectedToggleProperty().addListener(switchLanguageListener());
    }
    private ChangeListener<Toggle> switchLanguageListener() {
        return (ov, old_toggle, new_toggle) -> {
            if (languageButtons.getSelectedToggle() != null) {
                String selectedID = ((RadioMenuItem) languageButtons.getSelectedToggle()).getId();
                controller.switchLanguage(selectedID);
            }
        };
    }
}
