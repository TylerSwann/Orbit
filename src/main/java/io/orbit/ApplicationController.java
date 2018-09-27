package io.orbit;

import io.orbit.api.text.CodeEditor;
import io.orbit.controllers.*;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.settings.LocalUser;
import io.orbit.settings.ProjectFile;
import io.orbit.util.FontLoader;
import io.orbit.util.Size;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 09:15
 */
public class ApplicationController
{

    @FXML private AnchorPane container;
    @FXML private AnchorPane navigatorContainer;
    @FXML private SplitPane rootSplitPane;
    @FXML private AnchorPane editorContainer;
    @FXML private AnchorPane terminalContainer;
    @FXML private SplitPane contentSplitPane;

    private OProjectViewController projectViewController;
    private OMenuBarController menuBarController;
    private OTerminalController terminalController;
    private ONavigatorController projectNavigatorController;
    private OEditorTabPaneController editorTabPaneController;

    public void initialize() { }

    public void open()
    {
        FontLoader.loadFonts();
        this.terminalController = new OTerminalController(this.terminalContainer);
        this.menuBarController = new OMenuBarController(this.container);
        this.projectNavigatorController = new ONavigatorController(this.navigatorContainer);
        this.projectViewController = this.projectNavigatorController.getProjectViewController();
        this.editorTabPaneController = new OEditorTabPaneController(this.editorContainer);
        this.rootSplitPane.widthProperty().addListener(event -> this.rootSplitPane.getDividers().forEach(div -> div.setPosition(0.1911)));
        registerListeners();
    }

    private void registerListeners()
    {
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_LOAD, __ -> LanguageService.open(OEditorTabPaneController.ACTIVE_EDITOR_CONTROLLER, 1));
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, __ -> {
            Stage stage = App.PRIMARY_STAGE.get();
            Size windowSize = new Size(stage.getWidth(), stage.getHeight());
            LocalUser.userSettings.setWindowSize(windowSize);
        });
    }

    public SplitPane getRootSplitPane() { return rootSplitPane; }
    public SplitPane getContentSplitPane() { return contentSplitPane; }

    public OMenuBarController getMenuBarController() { return menuBarController; }
    public OEditorTabPaneController getEditorTabPaneController() { return this.editorTabPaneController; }
    public OTerminalController getTerminalController() { return terminalController; }
    public ONavigatorController getProjectNavigatorController() { return projectNavigatorController; }
    public OProjectViewController getProjectViewController() { return projectViewController; }
    public ObservableValue<OCodeEditorController> activeControllerProperty() { return OEditorTabPaneController.ACTIVE_EDITOR_CONTROLLER; }
    public OCodeEditorController getActiveController() {  return activeControllerProperty().getValue();  }
    public CodeEditor getActiveEditor() { return activeControllerProperty().getValue().getEditor(); }
    public List<ProjectFile> getActiveProjectFiles()
    {
        List<ProjectFile> activeFiles = new ArrayList<>();
        OEditorTabPaneController.ACTIVE_CONTROLLERS.forEach(controller -> activeFiles.add(controller.getEditor().getFile()));
        return activeFiles;
    }
    public Optional<ProjectFile> getActiveProjectFile()
    {
        if (getActiveEditor() != null)
            return Optional.of(getActiveEditor().getFile());
        return Optional.empty();
    }
}
