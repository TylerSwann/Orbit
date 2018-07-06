package io.orbit.controllers;

import com.jfoenix.controls.JFXTabPane;
import io.orbit.*;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.settings.LocalUser;
import io.orbit.settings.OrbitFile;
import io.orbit.settings.ProjectData;
import io.orbit.settings.ProjectFile;
import io.orbit.text.TextEditorPane;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.api.event.DocumentEvent;
import io.orbit.controllers.events.StatelessEventTargetObject;
import io.orbit.controllers.events.menubar.MenuBarCodeEvent;
import io.orbit.text.OrbitEditor;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.*;

/**
 * Created by Tyler Swann on Friday February 09, 2018 at 14:04
 */
public class OEditorController extends StatelessEventTargetObject
{
    private ProjectFile previouslyOpenedDocument;
    private JFXTabPane tabPane;
    public static final ObservableList<OrbitEditor> OPEN_EDITORS = FXCollections.observableArrayList();
    private static final SimpleObjectProperty<OrbitEditor> ACTIVE_EDITOR = new SimpleObjectProperty<>();
    public static final OrbitEditor getActiveEditor() { return ACTIVE_EDITOR.get(); }
    public static ObservableValue<OrbitEditor> activeEditorProperty() { return ACTIVE_EDITOR; }

    public OEditorController(JFXTabPane tabPane)
    {
        this.tabPane = tabPane;
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> this.saveUserSettings());
        registerSelectionListeners();
        loadUserSettings();
//        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_LOAD, appEvent -> {
//            OMenuBarController menuBarController = App.applicationController().getMenuBarController();
//            menuBarController.addEventHandler(MenuBarFileEvent.SAVE_ALL, event -> {
//                this.getOpenProjectFiles().forEach(projectFile -> {
//                    if (projectFile.wasModified())
//                        projectFile.save();
//                });
//                App.applicationController().getStatusBarController().showSnackBarMessage("Saved All!", 2000);
//            });
//        });
    }

    private void loadUserSettings()
    {
        Platform.runLater(() -> {
            ProjectData lastModified = LocalUser.userSettings.getLastModifiedProject();
            File lastOpenedFile = lastModified.getOpenFile();
            if (lastModified.getOpenEditors() != null)
            {
                for (File file : lastModified.getOpenEditors())
                    App.applicationController().getTabPaneController().openTab(new ProjectFile(file));
            }
            if (lastOpenedFile != null)
                tabPane.getSelectionModel().select(lastModified.getOpenEditors().indexOf(lastOpenedFile));
        });
    }

    private void saveUserSettings()
    {
        List<File> openFiles = new ArrayList<>();
        tabPane.getTabs().forEach(tab -> {
            if (tab.getUserData() != null && tab.getUserData() instanceof ProjectFile)
                openFiles.add(((ProjectFile)tab.getUserData()));
        });
        LocalUser.userSettings.getLastModifiedProject().setOpenEditors(openFiles);
        Tab openTab = tabPane.getSelectionModel().selectedItemProperty().get();
        if (openTab != null && openTab.getUserData() != null && openTab.getUserData() instanceof ProjectFile)
            LocalUser.userSettings.getLastModifiedProject().setOpenFile(((ProjectFile)openTab.getUserData()));
    }

    public List<ProjectFile> getOpenProjectFiles()
    {
        List<ProjectFile> projectFiles = new ArrayList<>();
        this.tabPane.getTabs().forEach(tab -> {
            if (tab.getUserData() != null && tab.getUserData() instanceof ProjectFile)
            {
                ProjectFile projectFile = (ProjectFile) tab.getUserData();
                projectFiles.add(projectFile);
            }
        });
        return projectFiles;
    }

    private void registerSelectionListeners()
    {
        App.applicationController().getMenuBarController().addEventHandler(MenuBarCodeEvent.REFORMAT_ACTIVE_DOCUMENT, event -> {
            event.getTargetEditor().ifPresent(editor -> editor.fireEvent(new DocumentEvent(this, this, DocumentEvent.REFORMAT_DOCUMENT)));
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener(event -> {
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            if (tab != null && tab.getUserData() != null && tab.getContent() == null && tab.getUserData() instanceof OrbitFile)
            {
                OrbitFile file = (OrbitFile) tab.getUserData();
                OrbitEditor editor = new OrbitEditor(file);
                editor.addEventHandler(DocumentEvent.SAVE_FILE, this::fireEvent);
                editor.addEventHandler(DocumentEvent.CLOSE_UNSAVED_FILE, this::fireEvent);
                editor.addEventHandler(CodeEditorEvent.FILE_WAS_MODIFIED, this::fireEvent);
                editor.addEventHandler(DocumentEvent.SAVE_NON_PROJECT_FILE, this::fireEvent);
                file.setTextProperty(editor.textProperty());
                TextEditorPane pane = new TextEditorPane(editor);
                App.syntaxTheme.sync(editor.getStylesheets());
                editor.styleProperty().bind(App.editorFontStyle);
                AnchorPane.setTopAnchor(editor, 0.0);
                AnchorPane.setBottomAnchor(editor, 0.0);
                AnchorPane.setLeftAnchor(editor, 0.0);
                AnchorPane.setRightAnchor(editor, 0.0);
                tab.setContent(pane);
            }
            if (tab != null && tab.getContent() != null && tab.getContent() instanceof TextEditorPane)
            {
                TextEditorPane pane = (TextEditorPane) tab.getContent();
                ACTIVE_EDITOR.set(((OrbitEditor)pane.getEditor()));
            }
            if (this.previouslyOpenedDocument != null)
                this.fireEvent(new DocumentEvent(DocumentEvent.DOCUMENT_WAS_CLOSED, this.previouslyOpenedDocument));
            if (tab != null && tab.getUserData() != null && tab.getUserData() instanceof ProjectFile)
            {
                ProjectFile file = ((ProjectFile)tab.getUserData());
                this.fireEvent(new DocumentEvent(DocumentEvent.DOCUMENT_WAS_OPENED, file));
                this.previouslyOpenedDocument = file;
            }
        });
        tabPane.getTabs().addListener((ListChangeListener<Tab>) c -> {
            List<OrbitEditor> updated = new ArrayList<>();

            tabPane.getTabs().forEach(tab -> {
                if (tab.getContent() instanceof TextEditorPane)
                {
                    TextEditorPane pane = (TextEditorPane) tab.getContent();
                    if (pane.getEditor() instanceof OrbitEditor)
                    {
                        OrbitEditor editor = (OrbitEditor) pane.getEditor();
                        updated.add(editor);
                    }
                }
            });
            OPEN_EDITORS.clear();
            OPEN_EDITORS.addAll(updated);
        });
    }
}
