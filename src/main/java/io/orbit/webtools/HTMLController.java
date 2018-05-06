package io.orbit.webtools;

import io.orbit.api.EditorController;

import io.orbit.api.text.CodeEditor;
import io.orbit.api.formatting.CodeFormatter;
import javafx.scene.input.KeyEvent;
import java.io.File;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:36
 */
public class HTMLController implements EditorController
{
    private CodeEditor editor;
    private CodeFormatter formatter;
    //private final HTMLIndentationMap map = new HTMLIndentationMap();

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.editor = editor;
        //  this.formatter = new CodeFormatter(this.editor, this.map);
        // formatter.play();
        //this.map.debugIndentation(((OrbitEditor)this.editor));
        //this.editor.ignoreDefaultBehaviorOf(KeyCode.TAB);
        //this.editor.addEventHandler(DocumentEvent.REFORMAT_DOCUMENT, event -> this.formatter.reformatDocument());
        //registerEvents();
    }

    private void registerEvents()
    {
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode())
            {
                case TAB:
                    this.formatter.pause();
                    int caretPos = this.editor.getCaretPosition();
                    String left = this.editor.getTextLeftOfCaret();
                    String[] segments = left == null ? new String[0] : left.split("\\s+");
                    boolean shouldInsertTab = true;
                    if (segments.length > 0)
                    {
                        String textLeftOfCaret = segments[segments.length - 1];
                        if (textLeftOfCaret.matches("[a-zA-Z]+"))
                        {
                            shouldInsertTab = false;
                            String tag = String.format("<%s></%s>", textLeftOfCaret, textLeftOfCaret);
                            this.editor.replaceText(caretPos - textLeftOfCaret.length(), caretPos, tag);
                            caretPos = this.editor.getCaretPosition();
                            this.editor.moveTo((caretPos - 1) - (tag.length() / 2));
                        }
                    }
                    if (shouldInsertTab)
                        this.editor.replaceText(caretPos, caretPos, "    ");
                    this.formatter.play();
                    break;
                default: break;
            }
        });
    }
}
