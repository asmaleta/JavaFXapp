package itmo.gui.controllers.lang;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LangController implements Initializable {
    private LangSwitcher controller;
    public LangController(LangSwitcher controller) {
        this.controller = controller;
    }

    @FXML private ToggleGroup languageButtons;

    @FXML
    private ResourceBundle resources;

    private ChangeListener<Toggle> switchLanguageListener() {
        return (ov, old_toggle, new_toggle) -> {
            if (languageButtons.getSelectedToggle() != null) {
                String selectedID = ((RadioMenuItem) languageButtons.getSelectedToggle()).getId();
                Locale.setDefault(Locale.forLanguageTag(selectedID));
                controller.switchLanguage();
            }
        };
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        languageButtons.selectedToggleProperty().addListener(switchLanguageListener());
    }
}
