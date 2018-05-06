package io.orbit.text.webtools;

import io.orbit.api.AutoCompletionBase;
import io.orbit.api.CharacterPair;
import io.orbit.controllers.events.AutoCompletionEvent;
import io.orbit.text.OrbitEditor;
import io.orbit.api.text.IndexedDocument;
import io.orbit.text.IndentationMap;
import io.orbit.api.text.IndexedLine;
import io.orbit.ui.AutoCompletionDialog;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.fxmisc.richtext.model.PlainTextChange;
import org.reactfx.AwaitingEventStream;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Tyler Swann on Wednesday March 21, 2018 at 09:17
 */
@Deprecated
public class CSS3Language implements ParseTreeListener
{
    private IndentationMap map;
    private OrbitEditor editor;
    private int tabSize = 4;
    private int textHash = 0;
    private List<? extends AutoCompletionBase> options;
    private String currentWord = "";
    private CSS3 language = new CSS3();
    private AutoCompletionDialog autoCompletionDialog;
    private ParseTreeWalker walker = new ParseTreeWalker();
    private final ObjectProperty<State> state = new SimpleObjectProperty<>(State.FORMATTING);
    private final ObjectProperty<AutoCompletionState> autoCompletionState = new SimpleObjectProperty<>(AutoCompletionState.OPTIONS);

    public CSS3Language(OrbitEditor editor)
    {
        this.autoCompletionDialog = new AutoCompletionDialog(editor);
        this.options = new ArrayList<>();
        this.editor = editor;
        this.map = new IndentationMap(new CharacterPair('{', '}'));
        this.map.compute(this.editor.getDocument());
        this.editor.ignoreKeys(KeyCode.ENTER, KeyCode.UP, KeyCode.DOWN);
        applyIndentation();
        registerAutoCompletionListeners();
        registerFormattingListeners();
        registerBasicListeners();
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                state.setValue(State.AUTO_COMPLETING);
                editor.removeEventHandler(KeyEvent.KEY_PRESSED, this);
            }
        });
    }

    private void registerBasicListeners()
    {
        this.autoCompletionDialog.getVisibilityProperty().addListener(event -> {

            if (this.autoCompletionDialog.isVisible())
                this.state.setValue(State.AUTO_COMPLETING);
            else
                this.state.setValue(State.FORMATTING);
        });
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SEMICOLON && !event.isShiftDown())
                this.autoCompletionDialog.setVisible(false);
        });
    }

    private void registerAutoCompletionListeners()
    {
        this.autoCompletionDialog.addEventHandler(AutoCompletionEvent.OPTION_WAS_SELECTED, event -> {
             if (event.selectedOption.isPresent())
             {
                 AutoCompletionBase option = event.selectedOption.get();
                 int caretPos = this.editor.getCaretPosition();
                 this.editor.replaceText(caretPos - currentWord.length(), caretPos, option.getInsertedText());
                 if (!option.getSubOptions().isEmpty())
                     this.autoCompletionState.setValue(AutoCompletionState.SUBOPTIONS);
             }
        });
        AwaitingEventStream<PlainTextChange> plainTextStream = this.editor.plainTextChanges().successionEnds(Duration.ofMillis(500));
        this.editor.addStatefulEventStreamListener(plainTextStream, this.state, State.AUTO_COMPLETING, event -> {
            //this.walker.walk(this, this.language.getPrimaryExpression(this.editor.getText()));
        });
        this.editor.addStatefulEventHandler(KeyEvent.KEY_PRESSED, this.state, State.AUTO_COMPLETING, event -> this.updateDialogPosition(this.editor.getCaretBounds()));
        this.editor.addStatefulEventHandler(KeyEvent.KEY_PRESSED, this.autoCompletionState, AutoCompletionState.SUBOPTIONS, event -> {
            if (event.getCode() == KeyCode.ENTER)
                this.autoCompletionState.setValue(AutoCompletionState.OPTIONS);
        });
    }

    private void registerFormattingListeners()
    {
        this.editor.addStatefulEventHandler(KeyEvent.KEY_PRESSED, this.state, State.FORMATTING, event -> {
            IndexedLine currentLine = this.getCurrentLine();
            String indent = this.getIndentation(this.map.indentLevelForLine(currentLine.number));
            switch (event.getCode())
            {
                case LEFT:
                case RIGHT:
                    System.out.println(this.editor.getCaretPosition());
                    break;
                case ENTER:
                    if (currentLine.end > this.editor.getDocument().length())
                        this.editor.replaceText(currentLine.end - 1, currentLine.end - 1, String.format("\n%s", indent));
                    else
                        this.editor.replaceText(currentLine.end, currentLine.end, String.format("\n%s", indent));
                    if (currentLine.text.equals(""))
                        this.editor.moveTo(this.editor.getCaretPosition() - 1);
                    break;
                case BACK_SPACE:
                    if (currentLine.isBlank && currentLine.length == indent.length() - 1)
                        this.editor.deleteText(currentLine.start - 1, currentLine.end);
                    break;
            }
        });
        this.editor.addStatefulPropertyListener(this.editor.caretPositionProperty(), this.state, State.FORMATTING, event -> this.applyIndentation());
    }

    private void applyIndentation()
    {
        if (this.textHash == this.editor.getText().hashCode())
            return;
        this.map.compute(this.editor.getDocument());
        List<IndexedLine> lines = Arrays.asList(this.document().indexedLines);
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
            lines = Arrays.asList(this.document().indexedLines);
            this.map.compute(this.editor.getDocument());
        }
        this.textHash = this.editor.getText().hashCode();
    }

    private void updateDialogPosition(Optional<Bounds> optionalBounds)
    {
        if (!optionalBounds.isPresent())
            return;
        Bounds caretBounds = optionalBounds.get();
        this.autoCompletionDialog.show(caretBounds.getMinX(), caretBounds.getMinY());
    }

    private String getIndentation(int level)
    {
        StringBuilder builder = new StringBuilder("");
        String indent = "    ";
        for (int i = 0; i < level; i++)
            builder.append(indent);
        return builder.toString();
    }

    private IndexedDocument document() { return new IndexedDocument(this.editor.getDocument()); }
    private IndexedLine getCurrentLine() { return this.document().indexedLines[this.editor.getFocusPosition().line]; }

    @Override
    public void visitTerminal(TerminalNode node) { }

    @Override
    public void visitErrorNode(ErrorNode node)
    {

        if (node == null || node.getText().equals("") || node.getText().matches("<.*>") || this.autoCompletionState.get().equals(AutoCompletionState.SUBOPTIONS))
            return;
        this.currentWord = node.getText();
        this.options = this.language
                .getAutoCompletionProvider()
                .optionsFor(
                        node.getSymbol().getStartIndex(),
                        node.getSymbol().getStopIndex(),
                        node.getSymbol().getLine(),
                        node.getText()
                );
        if (options == null || options.isEmpty())
            this.autoCompletionDialog.setVisible(false);
        else
            this.autoCompletionDialog.updateOptions(this.options);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) { }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) { }


    enum State
    {
        AUTO_COMPLETING,
        FORMATTING
    }
    enum AutoCompletionState
    {
        SUBOPTIONS,
        OPTIONS
    }
}
