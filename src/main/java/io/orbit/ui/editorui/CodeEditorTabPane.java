package io.orbit.ui.editorui;

import com.jfoenix.controls.JFXTabPane;
import io.orbit.api.text.CodeEditor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:27
 */
public class CodeEditorTabPane extends JFXTabPane
{
    public static final String DEFAULT_STYLE_CLASS = "editor-tab-pane";

    private final ObservableList<CodeEditor> editors = FXCollections.observableArrayList();
    public final ObservableList<CodeEditor> getEditors() { return editors; }

    private final ObservableList<File> files = FXCollections.observableArrayList();
    public final ObservableList<File> getFiles() { return files; }

    private final ObservableValue<EditorTab> activeTab = new SimpleObjectProperty<>();
    public ObservableValue<EditorTab> activeTabProperty() { return activeTab; }
    public EditorTab getActiveTab() { return ((SimpleObjectProperty<EditorTab>)activeTab).get(); }

    private final ObservableList<EditorTab> openTabs = FXCollections.observableArrayList();
    public ObservableList<EditorTab> getOpenTabs() {  return openTabs;  }

    public CodeEditorTabPane(List<File> files)
    {
        this();
        this.getFiles().addAll(files);
    }

    public CodeEditorTabPane()
    {
        this.registerListeners();
        this.setDisableAnimation(true);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void registerListeners()
    {
        this.getFiles().addListener((ListChangeListener<File>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(this::addNewFile);
                change.getRemoved().forEach(file -> this.getOpenTabs().forEach(tab -> {
                    if (tab.getFile().equals(file))
                        this.editors.remove(tab.getEditor());
                }));
            }
        });
        this.getTabs().addListener((ListChangeListener<Tab>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(tab -> {
                    if (!(tab instanceof EditorTab))
                        throw new RuntimeException("CodeEditorTabPane found Tab that is not instance of EditorTab!");
                    EditorTab editorTab = (EditorTab) tab;
                    if (!this.openTabs.contains(editorTab))
                        this.openTabs.remove(editorTab);
                });
                change.getRemoved().forEach(tab -> {
                    EditorTab editorTab = (EditorTab) tab;
                    this.openTabs.remove(editorTab);
                    this.getFiles().forEach(file -> {
                        if (editorTab.getFile().equals(file))
                            this.files.remove(file);
                    });
                });
            }
        });
        this.getSelectionModel().selectedItemProperty().addListener(observable -> ((SimpleObjectProperty<EditorTab>)this.activeTab).set((EditorTab)this.getSelectedItem()));
    }


    public void select(EditorTab tab)
    {
        this.getSelectionModel().select(tab);
    }

    public void select(File file)
    {
        for (EditorTab tab : this.getOpenTabs())
            if (tab.getFile().equals(file))
                this.select(tab);
    }

    private void addNewFile(File file)
    {
        EditorTab tab = new EditorTab(file);
        editors.add(tab.getEditor());
        this.getTabs().add(tab);
        AnchorPane.setTopAnchor(tab.getEditor(), 0.0);
        AnchorPane.setBottomAnchor(tab.getEditor(), 0.0);
        AnchorPane.setLeftAnchor(tab.getEditor(), 0.0);
        AnchorPane.setRightAnchor(tab.getEditor(), 0.0);
    }

    private EditorTab getSelectedItem()
    {
        return (EditorTab) this.getSelectionModel().getSelectedItem();
    }
}