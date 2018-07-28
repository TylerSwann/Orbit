package io.orbit.ui.contextmenu;

import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.text.CodeEditor;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.util.Arrays;

/**
 * Created by Tyler Swann on Friday July 06, 2018 at 18:55
 */
public class EditorContextMenu extends MUIContextMenu
{
    private static final String DEFAULT_STYLE_CLASS = "editor-context-menu";

    private CodeEditor editor;

    public EditorContextMenu(CodeEditor editor)
    {
        super(editor);
        this.editor = editor;
        this.root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        build();
    }

    private void build()
    {
        MUIMenuItem undo = new MUIMenuItem(FontAwesomeSolid.UNDO," Undo");
        MUIMenuItem redo = new MUIMenuItem(FontAwesomeSolid.REDO," Redo");
        MUIMenuItem cut = new MUIMenuItem(FontAwesomeSolid.CUT," Cut");
        MUIMenuItem copy = new MUIMenuItem(FontAwesomeSolid.COPY," Copy");
        MUIMenuItem paste = new MUIMenuItem(FontAwesomeSolid.PASTE," Paste");
        MUIMenuItem selectAll = new MUIMenuItem(FontAwesomeSolid.FILE_ALT," Select All");

        undo.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.UNDO, this.editor.getFile())));
        redo.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.REDO, this.editor.getFile())));
        cut.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.CUT, this.editor.getFile())));
        copy.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.COPY, this.editor.getFile())));
        paste.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.PASTE, this.editor.getFile())));
        selectAll.setOnAction(__ -> this.fireEvent(new CodeEditorEvent(CodeEditorEvent.SELECT_ALL, this.editor.getFile())));

        this.getItems().addAll(
            Arrays.asList(
                    undo,
                    redo,
                    cut,
                    copy,
                    paste,
                    selectAll
                )
        );
    }
}
