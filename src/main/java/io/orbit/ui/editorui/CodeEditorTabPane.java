/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.orbit.ui.editorui;

import io.orbit.api.text.CodeEditor;
import io.orbit.ui.tabs.MUITab;
import io.orbit.ui.tabs.MUITabPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.*;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:27
 */
public class CodeEditorTabPane extends MUITabPane
{
    public static final String DEFAULT_STYLE_CLASS = "editor-tab-pane";

    private final ObservableList<CodeEditor> editors = FXCollections.observableArrayList();
    public final ObservableList<CodeEditor> getEditors() { return editors; }

    private final ObservableList<File> files = FXCollections.observableArrayList();
    public final ObservableList<File> getFiles() { return files; }

    private final ObservableList<EditorTab> openTabs = FXCollections.observableArrayList();
    public ObservableList<EditorTab> getOpenTabs() {  return openTabs;  }

    public CodeEditorTabPane()
    {
        super();
        registerListeners();
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void registerListeners()
    {
        this.getFiles().addListener((ListChangeListener<File>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(file -> {
                    this.addNewFile(file);
                    if (change.getAddedSubList().size() == 1)
                        Platform.runLater(() -> this.select(file));
                });
                change.getRemoved().forEach(file -> this.getOpenTabs().forEach(tab -> {
                    if (tab.getFile().equals(file))
                        this.editors.remove(tab.getEditor());
                }));
            }
        });
        this.getTabs().addListener((ListChangeListener<MUITab>) change -> {
            while (change.next())
            {
                change.getAddedSubList().forEach(tab -> {
                    if (!(tab instanceof EditorTab))
                        throw new RuntimeException("CodeEditorTabPane found Tab that is not instance of EditorTab!");
                    EditorTab editorTab = (EditorTab) tab;
                    if (!this.openTabs.contains(editorTab))
                    {
                        this.openTabs.add(editorTab);
                        addTabMenuEvents(editorTab);
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

    public void select(File file)
    {
        for (EditorTab tab : this.getOpenTabs())
            if (tab.getFile().equals(file))
                this.select(tab);
    }

    private void addTabMenuEvents(EditorTab tab)
    {
        tab.getTabMenu().setOnClose(() -> this.getTabs().remove(tab));
        tab.getTabMenu().setOnCloseOthers(() -> {
            List<MUITab> tabs = new ArrayList<>(this.getTabs());
            tabs.remove(tab);
            this.getTabs().removeAll(tabs);
        });
        tab.getTabMenu().setOnCloseAll(() -> {
            List<MUITab> tabs = new ArrayList<>(this.getTabs());
            this.getTabs().removeAll(tabs);
        });
    }

    private void addNewFile(File file)
    {
        if (!fileIsOpen(file))
        {
            EditorTab tab = new EditorTab(file);
            editors.add(tab.getEditor());
            this.getTabs().add(tab);
            AnchorPane.setTopAnchor(tab.getEditor(), 0.0);
            AnchorPane.setBottomAnchor(tab.getEditor(), 0.0);
            AnchorPane.setLeftAnchor(tab.getEditor(), 0.0);
            AnchorPane.setRightAnchor(tab.getEditor(), 0.0);
        }
    }

    private boolean fileIsOpen(File file)
    {
        for (EditorTab tab : this.getOpenTabs())
            if (tab.getFile().equals(file))
                return true;
        return false;
    }

    public EditorTab getActiveTab() { return (EditorTab) this.getSelectedTab(); }
}
