package io.orbit.webtools;

import io.orbit.api.EditorController;

import io.orbit.api.formatting.PatternIndentationMap;
import io.orbit.api.text.CodeEditor;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.*;
import org.reactfx.collection.LiveList;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:36
 */
public class HTMLController implements EditorController
{
    private CodeEditor editor;
    private PatternIndentationMap map;
    private static final Pattern LEFT_PATTERN = Pattern.compile("(\\<)(?!(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr))([a-zA-Z0-9]+).*(\\>)");
    private static final Pattern RIGHT_PATTERN = Pattern.compile("\\<\\/[a-zA-Z0-9\\-?]+\\>");

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.editor = editor;
        map = new PatternIndentationMap(LEFT_PATTERN, RIGHT_PATTERN);
        registerEvents();
        this.editor.caretPositionProperty().addListener(event -> {
//            this.map.compute(this.editor.getDocument());
            int linNum = this.editor.getFocusPosition().line;
            System.out.println(String.format("Line: %d IndentLevel: %d", linNum, this.map.indentLevelForLine(linNum)));
        });

    }

    private void registerEvents()
    {
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
                            this.editor.replaceText((caretPos - 1) - textLeftOfCaret.length(), caretPos, tag);
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

    private String getIndentation(int level)
    {
        StringBuilder builder = new StringBuilder("");
        String indent = "    ";
        for (int i = 0; i < level; i++)
            builder.append(indent);
        return builder.toString();
    }
}
