package io.orbit;

import com.jfoenix.controls.JFXTabPane;
import io.orbit.controllers.*;
import io.orbit.util.FontLoader;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 09:15
 */
@SuppressWarnings("ALL")
public class ApplicationController
{

    @FXML private AnchorPane container;
    @FXML private AnchorPane navigatorContainer;
    public SplitPane rootSplitPane;
    @FXML private AnchorPane editorContainer;
    @FXML private AnchorPane terminalContainer;
    public SplitPane contentSplitPane;

    private OProjectViewController projectViewController;
    private OTabPaneController tabPaneController;
    private OMenuBarController menuBarController;
    private OEditorController editorController;
    private OTerminalController terminalController;
    private ONavigatorController projectNavigatorController;
    private AppEventsReceiver receiver;


    public void initialize() { }

    public void open()
    {
        FontLoader.loadFonts();
        this.terminalController = new OTerminalController(this.terminalContainer);
        this.menuBarController = new OMenuBarController(this.container);
        this.projectNavigatorController = new ONavigatorController(this.navigatorContainer);
        this.tabPaneController = new OTabPaneController(new JFXTabPane(), this.editorContainer);
        this.editorController = new OEditorController(this.tabPaneController.getTabPane());
        this.rootSplitPane.widthProperty().addListener(event -> this.rootSplitPane.getDividers().forEach(div -> div.setPosition(0.1911)));
        this.receiver = new AppEventsReceiver();
    }

    public OTabPaneController getTabPaneController() { return tabPaneController; }
    public OMenuBarController getMenuBarController() { return menuBarController; }
    public OEditorController getEditorController() { return editorController; }
    public OTerminalController getTerminalController() { return terminalController; }
    public ONavigatorController getProjectNavigatorController() { return projectNavigatorController; }
}
