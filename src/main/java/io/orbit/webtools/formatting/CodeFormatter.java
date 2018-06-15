package io.orbit.webtools.formatting;

import io.orbit.api.text.CodeEditor;
import io.orbit.api.text.IndexedDocument;
import io.orbit.api.text.IndexedLine;
import io.orbit.api.text.TextFocusPosition;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.collection.LiveList;

import java.util.Collection;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday April 21, 2018 at 12:48
 */
public class CodeFormatter
{
    private final IndentationMap map;
    private final CodeEditor editor;
    private boolean isPaused = true;
    private boolean hasAppliedInitialIndentation = false;

    public CodeFormatter(CodeEditor editor, IndentationMap map)
    {
        this.editor = editor;
        this.map = map;
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
        this.map.compute(this.editor.getDocument());
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
            //this.applyIndentation();
        }
        else
        {
            this.editor.replaceText(caretPos, caretPos, String.format("\n\n%s", indent));
            String textLeftOfCaret = this.editor.getTextLeftOfCaret();
            caretPos = this.editor.getCaretPosition();
            if (textLeftOfCaret != null && textLeftOfCaret.length() > 0)
                this.editor.moveTo((caretPos - textLeftOfCaret.length() - 1));
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
        if (currentLine.isBlank && currentLine.length == indent.length() && currentLine.start - 1 >= 0)
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
        }
    }


    public void reformatDocument()
    {
        this.map.compute(this.editor.getDocument());
        LiveList<Paragraph<Collection<String>, String, Collection<String>>> paragraphs = this.editor.getParagraphs();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paragraphs.size(); i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> paragraph = paragraphs.get(i);
            String line = paragraph.getText();

            if (line.matches("^\\s+$"))
            {
                builder.append(String.format("\n%s", line));
                continue;
            }
            int indentLevel = this.map.indentLevelForLine(i);
            String indent = this.getIndentation(indentLevel);
            String unformattedLine = line.replaceAll("^[\\s]+", "");
            builder.append(String.format("\n%s%s", indent, unformattedLine));
        }

        this.editor.replaceText(0, this.editor.getDocument().length(), builder.toString());
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
