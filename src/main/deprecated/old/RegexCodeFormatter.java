package io.orbit.text.formatting;

import io.orbit.api.text.CodeEditor;
import io.orbit.api.text.IndexedDocument;
import io.orbit.api.text.IndexedLine;
import io.orbit.text.TextFocusPosition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 15:09
 */
@Deprecated
public class RegexCodeFormatter
{
    private PatternIndentationMap map;
    private CodeEditor editor;
    private boolean isPaused = true;
    private boolean hasAppliedInitialIndentation = false;

    public RegexCodeFormatter(CodeEditor editor, PatternPair pattern)
    {
        this.editor = editor;
        this.map = new PatternIndentationMap(pattern);
        editor.ignoreDefaultBehaviorOf(KeyCode.ENTER, KeyCode.UP, KeyCode.DOWN);
        registerListeners();
    }

    public void pause()
    {
        this.isPaused = true;
    }

    public void play()
    {
        this.isPaused = false;
        if (!hasAppliedInitialIndentation)
        {
            applyIndentation();
            this.hasAppliedInitialIndentation = true;
        }
    }

    @Deprecated
    private void reApplyAllIndentations()
    {
        //(^[\s]+)
        StyledDocument<Collection<String>, String, Collection<String>> document = this.editor.getDocument();
        StringBuilder builder = new StringBuilder();
        for (Paragraph<Collection<String>, String, Collection<String>> paragraph : document.getParagraphs())
        {
            String line = paragraph.getText();
            if (line.matches("^\\s+$"))
            {
                builder.append(String.format("\n%s", line));
                continue;
            }
            String spaceLessLine = line.replaceAll("^[\\s]+", "");
            builder.append(String.format("\n%s", spaceLessLine));
        }
        StyledDocument<Collection<String>, String, Collection<String>> spacelessDocument;
        spacelessDocument = ReadOnlyStyledDocument.fromString(builder.toString(), Collections.emptyList(), Collections.emptyList(), SegmentOps.styledTextOps());
        PatternIndentationMap map = new PatternIndentationMap(this.map.pattern);
        map.compute(spacelessDocument);
        System.out.println("Paragraph length: " + spacelessDocument.length());
        builder = new StringBuilder();
        for (int i = 0; i < spacelessDocument.getParagraphs().size(); i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> paragraph = spacelessDocument.getParagraphs().get(i);
            int indentLevel = map.indentLevelForLine(i);
            String indent = this.getIndentation(indentLevel);
            String formattedLine = String.format("%s%s", indent, paragraph.getText());
            builder.append(String.format("\n%s", formattedLine));
        }
        this.editor.replaceText(0, this.editor.getDocument().length(), builder.toString());
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (this.isPaused)
                return;
            switch (event.getCode())
            {
                case ENTER:
                    insertNewLine();
                    break;
                case BACK_SPACE:
                    deleteLineIfNecessary();
                    break;
                case UP:
                case DOWN:
                    TextFocusPosition pos = this.editor.getFocusPosition();
                    IndexedDocument document = this.editor.getIndexedDocument();
                    if (event.getCode() == KeyCode.UP && pos.line - 1 >= 0)
                    {
                        IndexedLine nextLine = document.lines.get(pos.line - 1);
                        if (nextLine.isEmpty)
                            this.editor.moveTo(nextLine.start);
                        else
                            this.editor.moveTo(nextLine.end);
                    }
                    else if (event.getCode() == KeyCode.DOWN && pos.line + 1 < document.lines.size())
                    {
                        IndexedLine nextLine = document.lines.get(pos.line + 1);
                        if (nextLine.isEmpty)
                            this.editor.moveTo(nextLine.start);
                        else
                            this.editor.moveTo(nextLine.end);
                    }
                    break;
                default: break;
            }
        });
    }

    private void insertNewLine()
    {
        IndexedLine currentLine = this.getCurrentLine();
        int caretPos = this.editor.getCaretPosition();
        int indentLevel = this.map.indentLevelForLine(currentLine.number);
        String indent = this.getIndentation(indentLevel);
        String textRightOfCaret = this.editor.getTextRightOfCaret();
        if (textRightOfCaret == null)
        {
            if (currentLine.end > this.editor.getDocument().length())
                this.editor.replaceText(currentLine.end - 1, currentLine.end - 1, String.format("\n%s", indent));
            else
                this.editor.replaceText(currentLine.end, currentLine.end, String.format("\n%s", indent));
            if (currentLine.text.equals(""))
                this.editor.moveTo(this.editor.getCaretPosition() - 1);
        }
        else
        {
            if (currentLine.number - 1 >= 0 && this.map.indentLevelForLine(currentLine.number - 1) == 0)
                this.editor.replaceText(caretPos, caretPos, String.format("\n%s\n", indent));
            else
                this.editor.replaceText(caretPos, caretPos, String.format("\n\n%s", indent));
            String textLeftOfCaret = this.editor.getTextLeftOfCaret();
            caretPos = this.editor.getCaretPosition();
            if (textLeftOfCaret != null && textLeftOfCaret.length() > 0)
            {
                this.editor.moveTo((caretPos - textLeftOfCaret.length() - 1));
                this.applyIndentation();
            }
            else
                this.applyIndentation();
        }
        this.map.compute(this.editor.getDocument());
    }

    private void deleteLineIfNecessary()
    {
        if (!this.editor.getSelectedText().equals(""))
            return;
        IndexedLine currentLine = this.getCurrentLine();
        int indentLevel = this.map.indentLevelForLine(currentLine.number);
        String indent = this.getIndentation(indentLevel);
        if (currentLine.isBlank && currentLine.length == indent.length())
            this.editor.deleteText(currentLine.start, currentLine.end);
    }

    private IndexedLine getCurrentLine()
    {
        int caretPosInLine = this.editor.getFocusPosition().line;
        return this.editor.getIndexedDocument().lines.get(caretPosInLine);
    }
    private void applyIndentation()
    {
        this.map.compute(this.editor.getDocument());
        List<IndexedLine> lines = this.editor.getIndexedDocument().lines;
        for (int i = 0; i < lines.size(); i++)
        {
            IndexedLine line = lines.get(i);
            int caretCharPosition = line.start;
            String expectedIndentation = this.getIndentation(this.map.indentLevelForLine(line.number));
            if (line.isEmpty)
            {
                int length = expectedIndentation.length();
                String currentIndentation = (caretCharPosition - length) < 0 ? null : this.editor.getText(caretCharPosition - length, caretCharPosition);
                if (currentIndentation != null && !(this.editor.getText(caretCharPosition - length, caretCharPosition).equals(expectedIndentation)))
                    this.editor.replaceText(caretCharPosition, caretCharPosition, expectedIndentation);
            }
            lines = this.editor.getIndexedDocument().lines;
            this.map.compute(this.editor.getDocument());
        }
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
