package io.orbit.api.text;

import io.orbit.api.event.CodeEditorEvent;
import io.orbit.ui.MUIGutterButton;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Created by Tyler Swann on Thursday April 12, 2018 at 12:27
 */
public class CodeEditor extends StyleClassedTextArea
{
    /**
     * Used for clipboard actions. Remains null until copy/cut/paste/undo/redo methods are called
     */
    private static Clipboard clipboard;

    private CodeEditor() { }

    public CodeEditor(String source)
    {
        super(true);
        Platform.runLater(() -> this.init(source));

    }

    public ObservableList<Node> getChildren()
    {
        return super.getChildren();
    }

    public CodeEditor(File sourceFile)
    {
        try
        {
            byte[] data = Files.readAllBytes(Paths.get(sourceFile.getPath()));
            String source = new String(data, Charset.defaultCharset());
            Platform.runLater(() -> this.init(source));
        }
        catch (IOException ex)
        {
            System.out.println(String.format("ERROR loading file at path %s", sourceFile.getPath()));
            ex.printStackTrace();
        }
    }

    private final void init(String source)
    {
        this.replaceText(0, 0, source);
        this.setParagraphGraphicFactory(line -> new MUIGutterButton(line + 1));
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
        this.caretColumnProperty().addListener(event -> {
            int line = this.getFocusPosition().line;
            this.currentLine.setValue(this.getIndexedDocument().lines.get(line));
        });
    }


    /**
     *
     * @return - The specific character that is touching the left side
     *           of the caret on that specific line, including spaces, or null is there is none.
     */
    public final Character getCharacterLeftOfCaret()
    {
        String textLeftOfCaret = this.getTextLeftOfCaret();
        if (textLeftOfCaret != null && textLeftOfCaret.length() > 0)
            return textLeftOfCaret.toCharArray()[textLeftOfCaret.length() - 1];
        return null;
    }

    /**
     *
     * @return - The specific character that is touching the right side
     *           of the caret on that specific line, including spaces, or null is there is none.
     */
    public final Character getCharacterRightOfCaret()
    {
        String textRightOfCaret = this.getTextRightOfCaret();
        if (textRightOfCaret != null && textRightOfCaret.length() > 0)
            return textRightOfCaret.toCharArray()[0];
        return null;
    }

    /**
     *
     * @return - All text left of the caret on that specific line or null is there is none
     */
    public final String getTextLeftOfCaret()
    {
        TextFocusPosition focusPosition = this.getFocusPosition();
        IndexedLine currentLine = this.getIndexedDocument().lines.get(focusPosition.line);
        if (currentLine.length > 0)
            return currentLine.text.substring(0, focusPosition.caretPositionInLine);
        return null;
    }

    /**
     *
     * @return - All text right of the caret on that specific line or null is there is none
     */
    public final String getTextRightOfCaret()
    {
        TextFocusPosition focusPosition = this.getFocusPosition();
        IndexedLine currentLine = this.getIndexedDocument().lines.get(focusPosition.line);
        if (focusPosition.caretPositionInLine < currentLine.length)
            return currentLine.text.substring(focusPosition.caretPositionInLine, currentLine.length);
        return null;
    }

    /**
     * This method is useful for knowing the current line the caret is on,
     * the index of the caret in relation to the whole document, as well as
     * the index of the caret in relation to that specific line
     * @return - An instance of TextFocusPosition.
     */
    public final TextFocusPosition getFocusPosition()
    {
        Position position = this.offsetToPosition(this.getCaretPosition(), Bias.Forward);
        int caretPos = this.getCaretPosition();
        return new TextFocusPosition(position.getMajor(), position.getMinor(), caretPos);
    }


    public String getText()
    {
        return this.textProperty().getValue();
    }

    /**
     * Key presses of KeyCode type will be ignored
     *
     * @param keys - KeyCode ex. KeyCode.ENTER
     */
    public final void ignoreDefaultBehaviorOf(KeyCode... keys)
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
    public final void ignoreDefaultBehaviorOf(KeyCombination... combinations)
    {
        for (KeyCombination combination : combinations)
        {
            InputMap<Event> disabled = InputMap.consume(EventPattern.anyOf(EventPattern.keyPressed(combination)));
            Nodes.addInputMap(this, disabled);
        }
    }

    public final IndexedDocument getIndexedDocument()
    {
        return new IndexedDocument(this.getDocument());
    }

    /***
     * A segment is the current word the user typing.
     * The text left of the current line that the caret is on
     * is split by one or more spaces with the last segment
     * being the current segment the user is on. This is useful for autocompletion.
     */
    protected final ObjectProperty<String> segmentChangesProperty = new SimpleObjectProperty<>("");
    protected final EventStream<String> segmentChangeStream = EventStreams.valuesOf(segmentChangesProperty);
    public final EventStream<String> segmentChanges()
    {
        return segmentChangeStream;
    }
    public final String getCurrentSegment()
    {
        return this.segmentChangesProperty.get();
    }

    protected final ObjectProperty<IndexedLine> currentLine = new SimpleObjectProperty<>();
    public final IndexedLine getCurrentLine() { return currentLine.get(); }
    public final ReadOnlyObjectProperty<IndexedLine> indexedLineProperty() { return currentLine; }


    /**
     *
     * @param factory - This is a functional interface that returns a Node that will be
     *                  beside each line in the editor and display the line number
     */
    @Override
    public void setParagraphGraphicFactory(IntFunction<? extends Node> factory)
    {
        super.setParagraphGraphicFactory(factory);
    }

    @Override
    public void undo() { }

    @Override
    public void redo() { }

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
        if (clipboard == null || !clipboard.hasContent(DataFormat.PLAIN_TEXT))
            return;
        String content = clipboard.getString();
        int caretPos = this.getCaretPosition();
        this.replaceText(caretPos, caretPos, content);
        this.fireEvent(new CodeEditorEvent(this, this, CodeEditorEvent.PASTE));
    }
}
