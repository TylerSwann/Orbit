package io.orbit.webtools;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.formatting.HTMLCodeFormatter;
import javafx.scene.input.KeyEvent;
import java.io.File;


/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:36
 */
public class HTMLEditorController implements EditorController
{
    private CodeEditor editor;
//    private static final Pattern LEFT_PATTERN = Pattern.compile("(\\<)(?!(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr))([a-zA-Z0-9]+).*(\\>)");
//    private static final Pattern RIGHT_PATTERN = Pattern.compile("\\<\\/[a-zA-Z0-9\\-?]+\\>");
//    private HTMLIndentationMap htmlMap = new HTMLIndentationMap();

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.editor = editor;
        registerEvents();
        String text = HTMLCodeFormatter.format(this.editor.getText());
        this.editor.replaceText(0, this.editor.getText().length() - 1, text);
//        this.editor.caretPositionProperty().addListener(event -> {
//            int caretPos = this.editor.getCaretPosition();
//            if (HTMLCodeFormatter.indentMap.containsKey(caretPos))
//            {
//                String type = HTMLCodeFormatter.indentMap.get(caretPos);
//                System.out.println(String.format("%d %s", caretPos, type));
//            }
//        });
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

}
