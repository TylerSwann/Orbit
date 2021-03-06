package io.orbit.controllers;

import io.orbit.App;
import io.orbit.Themes;
import io.orbit.api.text.CodeEditor;
import io.orbit.settings.LocalUser;
import io.orbit.settings.ProjectData;
import io.orbit.ui.editorui.CodeEditorTabPane;
import io.orbit.ui.editorui.EditorTab;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:59
 */
public class OEditorTabPaneController
{
    private CodeEditorTabPane editorPane;
    public static final ObservableValue<OCodeEditorController> ACTIVE_EDITOR_CONTROLLER = new SimpleObjectProperty<>();
    public static final ObservableList<OCodeEditorController> ACTIVE_CONTROLLERS = FXCollections.observableArrayList();

    public OEditorTabPaneController(AnchorPane container)
    {
        this.editorPane = new CodeEditorTabPane();
        registerListeners();
        AnchorPane.setRightAnchor(editorPane, 0.0);
        AnchorPane.setLeftAnchor(editorPane, 0.0);
        AnchorPane.setTopAnchor(editorPane, 0.0);
        AnchorPane.setBottomAnchor(editorPane, 0.0);
        container.getChildren().add(editorPane);
        loadUserSettings();
    }

    public void openFile(File file)
    {
        this.editorPane.getFiles().add(file);
    }

    private void registerListeners()
    {
        App.addOnCloseHandler(this::saveUserSettings);
        this.editorPane.getEditors().addListener((ListChangeListener<CodeEditor>) change -> {
            while (change.next())
                change.getAddedSubList().forEach(Themes::sync);
        });
        this.editorPane.selectedTabProperty().addListener(__ -> {
            EditorTab activeTab = this.editorPane.getActiveTab();
            if (activeTab != null)
                ((SimpleObjectProperty<OCodeEditorController>) ACTIVE_EDITOR_CONTROLLER).set(activeTab.getController());
        });
        this.editorPane.getOpenTabs().addListener((ListChangeListener<EditorTab>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(tab -> ACTIVE_CONTROLLERS.add(tab.getController()));
                change.getRemoved().forEach(tab -> ACTIVE_CONTROLLERS.remove(tab.getController()));
            }
        });
    }

    private void loadUserSettings()
    {
        Platform.runLater(() -> {
            ProjectData lastModified = LocalUser.settings.getLastModifiedProject();
            File lastOpenedFile = lastModified.getOpenFile();
            if (lastModified.getOpenEditors() != null)
            {
                List<File> files = new ArrayList<>(lastModified.getOpenEditors());
                lastModified.getOpenEditors().clear();
                for (File file : files)
                {
                    if (!lastModified.getOpenEditors().contains(file))
                        lastModified.getOpenEditors().add(file);
                }
                this.editorPane.getFiles().addAll(files);
            }
            if (lastOpenedFile != null)
            {
                Platform.runLater(() -> this.editorPane.select(lastOpenedFile));
            }
        });
    }

    private void saveUserSettings()
    {
        LocalUser.settings.getLastModifiedProject().setOpenEditors(this.editorPane.getFiles());
        if (this.editorPane.getActiveTab() != null)
            LocalUser.settings.getLastModifiedProject().setOpenFile(this.editorPane.getActiveTab().getFile());
    }
}
