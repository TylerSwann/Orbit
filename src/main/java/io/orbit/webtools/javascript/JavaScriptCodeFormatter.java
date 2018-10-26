package io.orbit.webtools.javascript;

import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.CodeFormatter;
import io.orbit.webtools.javascript.antlr.JavaScriptParser;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.antlr.v4.runtime.ParserRuleContext;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStreams;

import java.util.regex.Pattern;

public class JavaScriptCodeFormatter extends CodeFormatter
{
    private static final String STATEMENT_PATTERN = ".*;";
    private static final String OPEN_BRACE_PATTERN = "^\\s*\\{";
    private static final String CLOSE_BRACE_PATTERN = "^\\s*}\\s*?$";

    private boolean hasSelectedText = false;

    public JavaScriptCodeFormatter(CodeEditor editor)
    {
        super(editor);
        InputMap<KeyEvent> disabledEnterKey = InputMap.consume(EventPattern.keyPressed(KeyCode.ENTER).onlyIf(event -> !(this.isPaused())));
        Nodes.addInputMap(this.editor, disabledEnterKey);
        this.editor.ignoreDefaultBehaviorOf(KeyCode.TAB);
        registerListeners();
    }

    private void registerListeners()
    {
        this.editor.selectedTextProperty().addListener(change -> Platform.runLater(() -> this.hasSelectedText = !this.editor.getSelectedText().equals("")));
        EventStreams.eventsOf(this.editor, KeyEvent.KEY_PRESSED).pauseWhen(paused).addObserver(this::keyWasPress);
    }

    private void keyWasPress(KeyEvent event)
    {
        switch (event.getCode())
        {
            case ENTER:
                this.insertNewLine();
                break;
            case BACK_SPACE:
                this.backspace();
                break;
            case TAB:
                this.editor.insertText(this.editor.getCaretPosition(), this.getIndent(1));
                break;
            default: break;
        }
    }

    private void backspace()
    {
        int lineNumber = this.editor.getFocusPosition().line;
        int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
        String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
        if (lineText.matches("\\s+") && !this.hasSelectedText)
            this.editor.replaceText(((characterNumber - 1) - lineText.length()), characterNumber, "");
    }

    private void insertNewLine()
    {
        String textLeftOfCaret = this.editor.getTextLeftOfCaret();
        String textRightOfCaret = this.editor.getTextRightOfCaret();
        int lineNumber = this.editor.getFocusPosition().line;
        int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
        if (textLeftOfCaret == null)
        {
            this.editor.insertText(characterNumber, "\n");
            return;
        }
        Character charLeftOfCaret = this.editor.getCharacterLeftOfCaret() == null ? Character.valueOf(Character.MIN_VALUE) : this.editor.getCharacterLeftOfCaret();
        Character charRightOfCaret = this.editor.getCharacterRightOfCaret() == null ? Character.valueOf(Character.MIN_VALUE) : this.editor.getCharacterRightOfCaret();
        String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
        if (charLeftOfCaret == '{' && charRightOfCaret == '}')
        {
            String currentIndent = trailingWhiteSpace(lineText);
            String indent = getIndent((currentIndent.length() / 4) + 1);
            this.editor.insertText(characterNumber, String.format("%s\n%s", indent, currentIndent));
            this.editor.moveTo(characterNumber + indent.length() + 1);
        }
        if (textLeftOfCaret.matches(STATEMENT_PATTERN) || textLeftOfCaret.matches(CLOSE_BRACE_PATTERN) || charLeftOfCaret == ',')
        {
            String currentIndent = trailingWhiteSpace(lineText);
            String indent = getIndent((currentIndent.length() / 4));
            this.editor.insertText(characterNumber, String.format("\n%s", indent));
        }
        else if (textLeftOfCaret.matches("\\s+"))
        {
            String indent = getIndent((lineText.length() / 4));
            this.editor.insertText(characterNumber, String.format("\n%s", indent));
        }
        else if (charLeftOfCaret == '{' || charLeftOfCaret == ':')
        {
            String currentIndent = trailingWhiteSpace(lineText);
            String indent = getIndent((currentIndent.length() / 4) + 1);
            this.editor.insertText(characterNumber, String.format("\n%s", indent));
        }
        else
            this.editor.insertText(characterNumber, "\n");
    }

    private String trailingWhiteSpace(String text)
    {
        char[] characters = text.toCharArray();
        int end = 0;
        for (int i = 0; i < characters.length; i++)
        {
            if (characters[i] != ' ')
            {
                end = i;
                break;
            }
        }
        return text.substring(0, end);
    }

    @Override
    public String reformat(String source)
    {
        return "";
    }
}
