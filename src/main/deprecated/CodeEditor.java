package io.orbit.text;

import io.orbit.api.CodeFormatter;
import io.orbit.api.Indentation;
import io.orbit.api.LanguageDelegate;
import io.orbit.controllers.events.StatefulEventTarget;
import io.orbit.ui.MUIGutterButton;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 14:31
 */
public class CodeEditor extends TextDocumentEditor implements StatefulEventTarget
{
    private SyntaxHighlighter highlighter;
    private LanguageDelegate language;
    private CodeFormatter formatter;

    public CodeEditor(LanguageDelegate language)
    {
        this(language, "");
    }
    public CodeEditor(LanguageDelegate language, String source)
    {
        this.init(source);
        this.language = language;
        this.clear();
        this.replaceText(0, 0, source);
        this.highlighter = new SyntaxHighlighter(this, this.language);
        this.highlighter.highlight();
    }

    public CodeEditor(File file)
    {
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(file.getPath())));
            this.init(source);
            LanguageDelegate language = LanguageManager.languageFromFile(file);
            this.language = language;
            //new CSS3Language(this);
            this.formatter = this.language.getFormatter();
            this.highlighter = new SyntaxHighlighter(this, language);
            this.highlighter.highlight();
        }
        catch (Exception ex) { ex.printStackTrace(); }
        if (this.formatter == null)
            return;
        this.formatter.start(this);

        this.ignoreKeys(new KeyCode[]{ KeyCode.ENTER });

        Platform.runLater(this::applyIndentation);

        //this.plainTextChanges().successionEnds(Duration.ofMillis(500)).addObserver(event -> this.applyIndentation());

        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            int lineNumber = this.getFocusPosition().line;
            int caretPos = this.caretPositionProperty().getValue();
            Document document = new Document(this.getDocument());
            List<Line> lines = Arrays.asList(document.lines);
            Line currentLine = lines.get(lineNumber);
            switch (event.getCode())
            {
                case ENTER:
                    Indentation newLineIndent = this.formatter.indentationForNewLine(lineNumber, caretPos);
                    this.replaceText(newLineIndent.insertAtChar, newLineIndent.insertAtChar, String.format("\n%s", this.getIndentation(newLineIndent.amount)));
                    break;
                case BACK_SPACE:
                    Indentation expectedIndent = this.formatter.indentationForLine(lineNumber);
                    if (expectedIndent == Indentation.EMPTY)
                        return;
                    String expectedIndentText = this.getIndentation(expectedIndent.amount);
                    if (currentLine.isBlank && currentLine.length == expectedIndentText.length() - 1)
                        this.deleteText(currentLine.start - 1, currentLine.end);
                    break;
                default: break;
            }
        });
    }

    public CodeEditor(String source)
    {
        super(source);
        this.setParagraphGraphicFactory(MUIGutterButton::new);
    }

    protected void applyIndentation()
    {
        Document document = new Document(this.getDocument());
        for (Line line : document.lines)
        {
            Indentation indent = this.formatter.indentationForLine(line.number);
            if (indent == Indentation.EMPTY)
                continue;
            this.replaceText(indent.insertAtChar, indent.insertAtChar, this.getIndentation(indent.amount));
        }
    }

    private String getIndentation(int amount)
    {
        String singleIndent = new String(new char[]{  ' ', ' ', ' ', ' '  }, 0, 4);
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < amount; i++)
            builder.append(singleIndent);
        return builder.toString();
    }
}
