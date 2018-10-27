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
 *
 */
package io.orbit.webtools.javascript;

import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.CodeFormatter;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStreams;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 20, 2018
 * Time: 5:21 PM
 * Website: https://orbiteditor.com
 */
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
