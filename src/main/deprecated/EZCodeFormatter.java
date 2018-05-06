package io.orbit.api;

import io.orbit.text.*;
import io.orbit.text.IndentationMap;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Tyler Swann on Thursday March 01, 2018 at 18:59
 */
public class EZCodeFormatter extends CodeFormatter
{
    private IndentationMap map;
    private ArrayList<CharacterPair> autocompletionPairs;
    private KeyEvent previousEvent;
    private KeyEvent currentKey;
    private boolean mouseIsDown = false;

    public EZCodeFormatter() { }
    public EZCodeFormatter(CodeEditor editor, LanguageDelegate language)
    {
        this.editor = editor;
        this.language = language;
    }

    @Override
    public void format() throws UnsupportedOperationException
    {
        super.format();
        InputMap<Event> disabledButtons = InputMap.consume(EventPattern.anyOf(
                EventPattern.keyPressed(KeyCode.TAB),
                EventPattern.keyPressed(KeyCode.ENTER)
//                EventPattern.keyPressed(KeyCode.BACK_SPACE)
        ));
        Nodes.addInputMap(this.editor, disabledButtons);
        this.map = new IndentationMap(this.language.getHierarchicalPair());
        this.map.compute(this.editor.getDocument());
        this.autocompletionPairs = new ArrayList<>(Arrays.asList(this.language.getAutoCompletingPairs()));
        this.autocompletionPairs.addAll(Collections.singletonList(this.language.getHierarchicalPair()));
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, this::keyWasPressed);
        this.editor.caretPositionProperty().addListener((observable, oldValue, newValue) -> caretPositionDidChange(newValue));
        this.editor.plainTextChanges().addObserver(event -> this.map.compute(this.editor.getDocument()));
        this.editor.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> this.mouseIsDown = event.isPrimaryButtonDown());
        this.editor.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> this.mouseIsDown = !event.isPrimaryButtonDown());
    }

    private void caretPositionDidChange(Integer caretCharPosition)
    {
        if (!this.editor.getSelectedText().equals(""))
            return;
        int caretLinePosition = this.editor.getFocusPosition().line;
        Line line = this.getCurrentLine();
        String expectedIndentation = this.indentationForLine(line.number).indent;
        if (line.isEmpty)
        {
            int length = expectedIndentation.length();
            String currentIndentation = (caretCharPosition - length) < 0 ? null : this.editor.getText(caretCharPosition - length, caretCharPosition);
            if (currentIndentation != null && !(this.editor.getText(caretCharPosition - length, caretCharPosition).equals(expectedIndentation)))
                this.editor.replaceText(caretCharPosition, caretCharPosition, expectedIndentation);
        }
        else if (line.isBlank &&
                line.text.length() == expectedIndentation.length() &&
                this.currentKey != null &&
                this.currentKey.getCode() == KeyCode.ENTER)
        {
            this.editor.moveTo(line.end - 1);
        }
    }

    private void keyWasPressed(KeyEvent event)
    {
        this.currentKey = event;
        Line line = this.getCurrentLine();
        int caretCharPosition = this.editor.getCaretPosition();
        int caretLinePosition = this.editor.getFocusPosition().line;
        String key = CharacterPair.keyEventText(event);
        switch (event.getCode())
        {
            case RIGHT:
                case LEFT:
                    System.out.println(caretCharPosition);
                    break;
//            case BACK_SPACE:
//                if (line.isBlank)
//                    this.editor.deleteText((caretCharPosition - line.numberOfCharactersInLine - 1), caretCharPosition);
//                else if (!this.editor.getSelectedText().equals(""))
//                    this.editor.deleteText(this.editor.getCaretSelectionBind().startPositionProperty().getValue(), this.editor.getCaretSelectionBind().endPositionProperty().getValue());
//                else
//                    this.editor.deleteText(caretCharPosition - 1, caretCharPosition);
//                return;
            case TAB:
                this.editor.replaceText(caretCharPosition, caretCharPosition, "    ");
                return;
            case ENTER:
                String leftChar = this.editor.getText(caretCharPosition - 1, caretCharPosition);
                String rightChar = this.editor.getLength() < caretCharPosition + 1 ? "" : this.editor.getText(caretCharPosition, caretCharPosition + 1);
                String indent = this.indentationForLine(caretLinePosition).indent;
                CharacterPair pair = getMatchingPair(leftChar);
                if (pair != CharacterPair.EMPTY && isHierarchicalPair(leftChar) && pair.right.equals(rightChar))
                {
                    this.editor.replaceText(caretCharPosition, caretCharPosition + 1, String.format("\n%s\n%s", indent, rightChar));
                    this.editor.moveTo(caretCharPosition + 1);
                }
                else if (isHierarchicalPair(rightChar))
                    this.editor.insertText(caretCharPosition, "\n");
                else
                    this.editor.replaceText(caretCharPosition, caretCharPosition, String.format("\n%s", indent));
                return;
        }
        CharacterPair pair = this.getMatchingPair(key);
        if (pair != CharacterPair.EMPTY)
        {
            if (key.equals(pair.left))
            {
                this.editor.replaceText(caretCharPosition, caretCharPosition, pair.right);
                this.editor.moveTo(caretCharPosition);
            }
            else if (key.equals(pair.right) && CharacterPair.keyEventText(this.previousEvent).equals(pair.left))
                this.editor.deleteText(caretCharPosition - 1, caretCharPosition);
        }
        this.previousEvent = event;
    }

    private CharacterPair getMatchingPair(String key)
    {
        for (CharacterPair pair : this.autocompletionPairs)
            if (pair.left.equals(key) || pair.right.equals(key))
                return pair;
        return  CharacterPair.EMPTY;
    }

    private boolean isHierarchicalPair(String key)
    {
        return this.language.getHierarchicalPair().left.equals(key) || this.language.getHierarchicalPair().right.equals(key);
    }


    private Document document() { return new Document(this.editor.getDocument()); }
    private Line getCurrentLine() { return this.document().lines[this.editor.getFocusPosition().line]; }

    @Override
    public Indentation indentationForLine(int line)
    {
        int indentationLevel = this.map.indentLevelForLine(line);
        if (indentationLevel <= 0)
            return new Indentation(0);
        return new Indentation(indentationLevel);
    }

}
