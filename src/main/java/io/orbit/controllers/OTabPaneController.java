package io.orbit.controllers;

import com.jfoenix.controls.JFXTabPane;
import io.orbit.settings.ProjectFile;
import io.orbit.settings.UnownedProjectFile;
import io.orbit.api.event.DocumentEvent;
import io.orbit.text.TextEditorPane;
import io.orbit.text.OrbitEditor;
import io.orbit.ui.MUIDialog;
import io.orbit.ui.MUITabPane;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import java.io.File;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 18:21
 */
public class OTabPaneController
{
    private JFXTabPane tabPane;

    public OTabPaneController(JFXTabPane tabPane, AnchorPane container)
    {
        AnchorPane.setRightAnchor(tabPane, 0.0);
        AnchorPane.setLeftAnchor(tabPane, 0.0);
        AnchorPane.setTopAnchor(tabPane, 0.0);
        AnchorPane.setBottomAnchor(tabPane, 0.0);
        container.getChildren().add(tabPane);
        tabPane.setDisableAnimation(true);
        tabPane.getStyleClass().add("project-tab-pane");
        this.tabPane = tabPane;
    }

    public void openTab(ProjectFile projectFile)
    {
        if (projectFile == null || !projectFile.exists() || projectFile.isDirectory()) { return; }
        Tab preExistingTab = getTabWithFile(projectFile);
        if (preExistingTab != null)
        {
            this.tabPane.getSelectionModel().select(preExistingTab);
            return;
        }
        Tab tab = new Tab(projectFile.getName());
        tab.setUserData(projectFile);
        addEventsToTab(tab);
        this.tabPane.getTabs().add(tab);
        this.tabPane.getSelectionModel().select(tab);
    }

    public void openNonProjectFile(File file, UnownedProjectFile.UnownedProjectFileMode mode)
    {
        UnownedProjectFile nonProjectFile = new UnownedProjectFile(file, mode);
        Tab tab = getTabWithFile(nonProjectFile);
        if (tab != null)
        {
            this.tabPane.getSelectionModel().select(tab);
            return;
        }
        tab = new Tab(file.getName());
        tab.setUserData(nonProjectFile);
        addEventsToTab(tab);
        this.tabPane.getTabs().add(tab);
        this.tabPane.getSelectionModel().select(tab);
    }

    private Tab getTabWithFile(File file)
    {
        for (Tab tab : this.tabPane.getTabs())
            if (tab.getUserData() != null && tab.getUserData() instanceof ProjectFile)
                if (tab.getUserData().equals(file))
                    return tab;
        return null;
    }

    private void addEventsToTab(Tab tab)
    {
        MenuItem close = new MenuItem("Close");
        MenuItem closeOthers = new MenuItem("Close Others");
        MenuItem closeAll = new MenuItem("Close All");
        MenuItem closeAllButPinned = new MenuItem("Close All but Pinned");
        ContextMenu contextMenu = new ContextMenu(
                close,
                closeOthers,
                closeAll,
                closeAllButPinned
        );
        close.setOnAction(event -> {
            TextEditorPane pane = (TextEditorPane) tab.getContent();
            if (pane.getEditor() instanceof OrbitEditor)
            {
                OrbitEditor editor = (OrbitEditor) pane.getEditor();
                ProjectFile projectFile = (ProjectFile) tab.getUserData();
                if (editor.hasUnsavedChanges())
                {
                    String message = String.format("%s has unsaved changes. Would you like to save them?", projectFile.getName());
                    MUIDialog dialog = new MUIDialog("Unsaved Changes", "Don't Save", "Save", message);
                    dialog.setOnPrimaryClick(unused -> {
                        projectFile.save();
                        editor.fireEvent(new DocumentEvent(DocumentEvent.SAVE_FILE, projectFile));
                        tabPane.getTabs().remove(tab);
                    });
                    dialog.setOnSecondaryClick(() -> {
                        editor.fireEvent(new DocumentEvent(DocumentEvent.CLOSE_UNSAVED_FILE, projectFile));
                        tabPane.getTabs().remove(tab);
                    });
                    dialog.show(editor.getScene().getWindow());
                }
                else
                    tabPane.getTabs().remove(tab);
            }
        });
        closeAll.setOnAction(event -> tabPane.getTabs().removeAll(tabPane.getTabs()));
        closeAllButPinned.setOnAction(event -> System.out.println("TODO"));
        closeOthers.setOnAction(event -> {
            tabPane.getTabs().removeAll(tabPane.getTabs());
            tabPane.getTabs().add(tab);
        });
        tab.setContextMenu(contextMenu);
    }

    public JFXTabPane getTabPane() { return tabPane; }
}
