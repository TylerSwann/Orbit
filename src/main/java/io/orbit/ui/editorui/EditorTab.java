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
