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
import io.orbit.controllers.OCodeEditorController;
import io.orbit.text.TextEditorPane;
import io.orbit.ui.tabs.MUITab;
import javafx.scene.input.ContextMenuEvent;
import java.io.File;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:47
 */
public class EditorTab extends MUITab
{
    public static final String DEFAULT_STYLE_CLASS = "editor-tab";
    private CodeEditor editor;
    private OCodeEditorController controller;
    private File file;
    private EditorTabMenu tabMenu;

    public EditorTab(File file)
    {
        super(file.getName());
        this.editor = new CodeEditor(file);
        this.controller = new OCodeEditorController(this.editor);
        this.file = file;
        TextEditorPane pane = new TextEditorPane(this.controller);
        this.setContent(pane);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setId(file.getName());
        this.tabMenu = new EditorTabMenu();
        this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            double x = event.getScreenX();
            double y = event.getScreenY();
            this.tabMenu.show(this, x, y);
        });
    }
    public OCodeEditorController getController() { return controller; }
    public CodeEditor getEditor() { return editor; }
    public File getFile() { return file; }
    public EditorTabMenu getTabMenu() { return tabMenu; }
}
