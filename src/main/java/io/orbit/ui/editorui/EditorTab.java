package io.orbit.ui.editorui;

import io.orbit.api.text.CodeEditor;
import io.orbit.controllers.OCodeEditorController;
import io.orbit.text.TextEditorPane;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.ContextMenuEvent;

import java.io.File;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:47
 */
public class EditorTab extends Tab
{
    public static final String DEFAULT_STYLE_CLASS = "editor-tab";
    private CodeEditor editor;
    private OCodeEditorController controller;
    private File file;
    private Node owner;
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
    }

    private void buildTabMenu()
    {
        this.tabMenu = new EditorTabMenu();
        this.owner.setOnContextMenuRequested(event -> {
            double x = event.getScreenX();
            double y = event.getScreenY();
            this.tabMenu.show(this.owner, x, y);
        });
    }

    public OCodeEditorController getController() { return controller; }
    public CodeEditor getEditor() { return editor; }
    public File getFile() { return file; }
    public EditorTabMenu getTabMenu() { return tabMenu; }

    public Node getOwner() { return owner; }
    public void setOwner(Node owner)
    {
        this.owner = owner;
        buildTabMenu();
    }
}
