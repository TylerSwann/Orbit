package io.orbit.webtools.html;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.File;


/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:36
 */
public class HTMLEditorController implements EditorController
{
    private CodeEditor editor;
    private HTMLCodeFormatter formatter;

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.editor = editor;
        this.editor.ignoreDefaultBehaviorOf(KeyCode.TAB);
        this.formatter = new HTMLCodeFormatter(this.editor);
        registerEvents();
    }

    private void registerEvents()
    {
        this.formatter.play();
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode())
            {
                case TAB:
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
                    break;
                default: break;
            }
        });
    }

}
