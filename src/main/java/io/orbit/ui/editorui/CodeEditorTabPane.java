package io.orbit.ui.editorui;

import com.jfoenix.controls.JFXTabPane;
import io.orbit.api.text.CodeEditor;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.File;
import java.util.*;

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

    // TODO - fix tab events or write new tab pane

    public CodeEditorTabPane()
    {
        this.registerListeners();
        this.setDisableAnimation(true);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void registerListeners()
    {
        this.getSelectionModel().selectedItemProperty().addListener(observable -> ((SimpleObjectProperty<EditorTab>)this.activeTab).set(this.getSelectedItem()));
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
                    {
                        this.openTabs.add(editorTab);
                        PauseTransition pause = new PauseTransition(Duration.millis(500.0));
                        pause.setOnFinished(__ -> updateTabNodes());
                        pause.play();
                    }
                });
                change.getRemoved().forEach(tab -> {
                    EditorTab editorTab = (EditorTab) tab;
                    this.openTabs.remove(editorTab);
                    List<File> files = new ArrayList<>(this.getFiles());
                    files.forEach(file -> {
                        if (editorTab.getFile().equals(file))
                            this.files.remove(file);
                    });
                });
            }
        });
    }

    private Optional<EditorTab> getTabWithFile(File file)
    {
        for (EditorTab tab : this.getOpenTabs())
            if (tab.getFile().equals(file))
                return Optional.of(tab);
        return Optional.empty();
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

    private void updateTabNodes()
    {

        List<Node> tabNodes = new ArrayList<>(this.lookupAll(".editor-tab"));
        if (tabNodes == null)
            return;
        for (int i = 0; i < tabNodes.size(); i++)
        {
            Node tabNode = tabNodes.get(i);
            File associatedFile = this.files.get(i);
            EditorTab editorTab;
            if (getTabWithFile(associatedFile).isPresent())
            {
                editorTab = getTabWithFile(associatedFile).get();
                editorTab.setOwner(tabNode);
                addTabMenuEvents(editorTab);
                tabNode.setUserData(associatedFile.getName());
            }
        }
    }

    private void addTabMenuEvents(EditorTab tab)
    {
        tab.getTabMenu().setOnClose(() -> this.getTabs().remove(tab));
        tab.getTabMenu().setOnCloseOthers(() -> {
            List<Tab> tabs = new ArrayList<>(this.getTabs());
            tabs.remove(tab);
            this.getTabs().removeAll(tabs);
        });
        tab.getTabMenu().setOnCloseAll(() -> {
            List<Tab> tabs = new ArrayList<>(this.getTabs());
            this.getTabs().removeAll(tabs);
        });
    }

    private void addNewFile(File file)
    {
        if (fileIsOpen(file))
            this.getTabWithFile(file).ifPresent(this::select);
        else
        {
            EditorTab tab = new EditorTab(file);
            editors.add(tab.getEditor());
            this.getTabs().add(tab);
            AnchorPane.setTopAnchor(tab.getEditor(), 0.0);
            AnchorPane.setBottomAnchor(tab.getEditor(), 0.0);
            AnchorPane.setLeftAnchor(tab.getEditor(), 0.0);
            AnchorPane.setRightAnchor(tab.getEditor(), 0.0);
            this.getSelectionModel().select(tab);
        }
    }

    private boolean fileIsOpen(File file)
    {
        for (EditorTab tab : this.getOpenTabs())
            if (tab.getFile().equals(file))
                return true;
        return false;
    }

    private EditorTab getSelectedItem()
    {
        return (EditorTab) this.getSelectionModel().getSelectedItem();
    }
}
