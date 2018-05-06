package io.orbit.text;

import io.orbit.api.CharacterPair;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.text.IndexedDocument;
import io.orbit.api.text.IndexedLine;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.input.*;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Created by Tyler Swann on Thursday January 25, 2018 at 17:49
 */
@Deprecated
public class TextDocumentEditor extends StyleClassedTextArea
{
    private ObjectProperty<String> segmentChangesProperty = new SimpleObjectProperty<>("");
    private EventStream<String> segmentChangeStream = EventStreams.valuesOf(segmentChangesProperty);
    private Clipboard clipboard;

    public TextDocumentEditor()
    {
        this.init("");

    }
    public TextDocumentEditor(String text) { this.init(text); }
    public TextDocumentEditor(File textFile)
    {
        try
        {
            String text = new String(Files.readAllBytes(Paths.get(textFile.toURI())));
            this.init(text);
        }
        catch (Exception ex){ex.printStackTrace();}

    }


    public EventStream<String> segmentChanges()
    {
        return segmentChangeStream;
    }

    public String getCurrentSegment()
    {
        return this.segmentChangesProperty.get();
    }
    public IndexedDocument getIndexedDocument()
    {
        return new IndexedDocument(this.getDocument());
    }

    public String getTextLeftOfCaret()
    {
        TextFocusPosition focusPosition = this.getFocusPosition();
        IndexedLine currentLine = this.getIndexedDocument().lines.get(focusPosition.line);
        if (currentLine.length > 0)
            return currentLine.text.substring(0, focusPosition.caretPositionInLine);
        return null;
    }

    public String getTextRightOfCaret()
    {
        TextFocusPosition focusPosition = this.getFocusPosition();
        IndexedLine currentLine = this.getIndexedDocument().lines.get(focusPosition.line);
        if (focusPosition.caretPositionInLine < currentLine.length)
            return currentLine.text.substring(focusPosition.caretPositionInLine, currentLine.length);
        return null;
    }

    public Character getCharacterLeftOfCaret()
    {
        String textLeftOfCaret = this.getTextLeftOfCaret();
        if (textLeftOfCaret != null && textLeftOfCaret.length() > 0)
            return textLeftOfCaret.toCharArray()[textLeftOfCaret.length() - 1];
        return null;
    }

    public Character getCharacterRightOfCaret()
    {
        String textRightOfCaret = this.getTextRightOfCaret();
        if (textRightOfCaret != null && textRightOfCaret.length() > 0)
            return textRightOfCaret.toCharArray()[0];
        return null;
    }

    public CharacterPair getCharactersAroundCaret()
    {
        Character left = this.getCharacterLeftOfCaret();
        Character right = this.getCharacterRightOfCaret();
        if (left != null && right != null)
            return new CharacterPair(left, right);
        return null;
    }


    /**
     * Key presses of KeyCode type will be ignored
     *
     * @param keys - KeyCode ex. KeyCode.ENTER
     */
    public void ignoreKeys(KeyCode key)
    {
        ignoreKeys(new KeyCode[]{ key });
    }

    /**
     * Key presses of KeyCode type will be ignored
     *
     * @param keys - KeyCode ex. KeyCode.ENTER
     */
    public void ignoreKeys(KeyCode... keys)
    {
        for (KeyCode key : keys)
        {
            InputMap<Event> disabled = InputMap.consume(EventPattern.anyOf(EventPattern.keyPressed(key)));
            Nodes.addInputMap(this, disabled);
        }
    }

    /**
     * KeyPresses that match this KeyCombination pattern will be ignored
     *
     * @param combinations - Instance of KeyCombination class
     */
    public void ignoreKeys(KeyCombination... combinations)
    {
        for (KeyCombination combination : combinations)
        {
            InputMap<Event> disabled = InputMap.consume(EventPattern.anyOf(EventPattern.keyPressed(combination)));
            Nodes.addInputMap(this, disabled);
        }
    }

    /**
     *
     * Keys presses of KeyCode type will be ignore assuming the specified modifiers are also pressed
     *
     * @param key
     * @param modifiers
     */
    public void ignoreKeys(KeyCode key, KeyCombination.Modifier... modifiers)
    {
        InputMap<Event> disabled = InputMap.consume(EventPattern.anyOf(EventPattern.keyPressed(key, modifiers)));
        Nodes.addInputMap(this, disabled);
    }

    protected final void init(String text)
    {
        this.replaceText(0, 0, text);
        this.setCursor(Cursor.TEXT);

        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode())
            {
                case ENTER:
                case SPACE:
                    this.fireEvent(new CodeEditorEvent(CodeEditorEvent.SEGMENT_CHANGE, this.segmentChangesProperty.get(), ""));
                    this.segmentChangesProperty.setValue("");
                    break;
                default:
                    int caretPosInLine = this.getFocusPosition().line;
                    String currentLine = this.getIndexedDocument().lines.get(caretPosInLine).text;
                    String[] segments = currentLine.split("\\s+");
                    if (segments.length > 0)
                    {
                        String newValue = segments[segments.length - 1];
                        String oldValue = this.segmentChangesProperty.getValue();
                        this.segmentChangesProperty.setValue(segments[segments.length - 1]);
                        this.fireEvent(new CodeEditorEvent(CodeEditorEvent.SEGMENT_LETTER_CHANGE, oldValue, newValue));
                    }
                    break;
            }
        });
    }

    public TextFocusPosition getFocusPosition()
    {
        Position position = this.offsetToPosition(this.getCaretPosition(), Bias.Forward);
        int caretPos = this.getCaretPosition();
        return new TextFocusPosition(position.getMajor(), position.getMinor(), caretPos);
    }
    public String getText() { return this.textProperty().getValue(); }
    @Override
    public void undo()
    {
        System.out.println("TODO!");
//        super.undo();
//        this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.UNDO));
    }
    @Override
    public void redo()
    {
        System.out.println("TODO!");
        //super.redo();
        //this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.REDO));
    }
    @Override
    public void copy()
    {
        if (this.selectedTextProperty().getValue().equals(""))
            return;
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, this.selectedTextProperty().getValue());
        clipboard.setContent(content);
        this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.COPY));
    }
    @Override
    public void cut()
    {
        if (this.selectedTextProperty().getValue().equals(""))
            return;
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        Map<DataFormat, Object> content = new HashMap<>();
        String selectedText = this.selectedTextProperty().getValue();
        IndexRange range = this.getSelection();
        content.put(DataFormat.PLAIN_TEXT, selectedText);
        clipboard.setContent(content);
        this.deleteText(range.getStart(), range.getEnd());
        this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.CUT));
    }
    @Override
    public void paste()
    {
        if (this.clipboard == null || !this.clipboard.hasString())
            return;
        String content = this.clipboard.getString();
        int caretPos = this.getCaretPosition();
        this.replaceText(caretPos, caretPos, content);
        this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.PASTE));
    }

    @Override
    public void setParagraphGraphicFactory(IntFunction<? extends Node> factory) { super.setParagraphGraphicFactory(factory); }

}
