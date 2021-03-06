package io.orbit.text;

import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.text.CodeEditor;
import io.orbit.controllers.OCodeEditorController;
import io.orbit.controllers.FindAndReplace;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;

/**
 * Created by Tyler Swann on Sunday April 29, 2018 at 16:21
 */
public class TextEditorPane extends AnchorPane
{
    private VirtualizedScrollPane<CodeEditor> scrollPane;
    private CodeEditor editor;
    private FindAndReplace findAndReplace;

    public TextEditorPane(OCodeEditorController controller)
    {
        this.editor = controller.getEditor();
        this.scrollPane = new VirtualizedScrollPane<>(this.editor);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        this.getChildren().add(scrollPane);
        registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(CodeEditorEvent.FIND_AND_REPLACE, event -> this.showFindAndReplaceDialog(true));
        this.editor.addEventHandler(CodeEditorEvent.FIND, event -> this.showFindAndReplaceDialog(false));
    }

    public void showFindAndReplaceDialog(boolean withReplaceShowing)
    {
        if (findAndReplace != null )
        {
            if (findAndReplace.mode == FindAndReplace.Mode.FIND && withReplaceShowing)
            {
                findAndReplace.toggleReplacePane();
                AnchorPane.setTopAnchor(scrollPane, 100.0);
            }
            else if (findAndReplace.mode == FindAndReplace.Mode.FIND_REPLACE && !withReplaceShowing)
            {
                findAndReplace.toggleReplacePane();
                AnchorPane.setTopAnchor(scrollPane, 45.0);
            }
        }
        if (findAndReplace != null)
            return;
        VBox findAndReplacePane = FindAndReplace.load(editor, withReplaceShowing, this.scrollPane);
        this.findAndReplace = FindAndReplace.controller;
        FindAndReplace.controller.setOnCloseRequest( () -> {
            this.findAndReplace = null;
            this.getChildren().remove(0);
            AnchorPane.setTopAnchor(scrollPane, 0.0);
        });
        if (withReplaceShowing)
            AnchorPane.setTopAnchor(scrollPane, 100.0);
        else
            AnchorPane.setTopAnchor(scrollPane, 45.0);
        AnchorPane.setRightAnchor(findAndReplacePane, 0.0);
        AnchorPane.setLeftAnchor(findAndReplacePane, 0.0);
        this.getChildren().add(0, findAndReplacePane);
    }

    public VirtualizedScrollPane<CodeEditor> getScrollPane() { return scrollPane; }
    public CodeEditor getEditor() { return this.editor; }
}
