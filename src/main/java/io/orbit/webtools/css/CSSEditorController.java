package io.orbit.webtools.css;

import io.orbit.api.EditorController;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.text.IndexedDocument;
import io.orbit.api.text.TextFocusPosition;
import io.orbit.api.event.AutoCompletionEvent;
import io.orbit.api.text.IndexedLine;
import io.orbit.api.autocompletion.AutoCompletionDialog;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:39
 */
public class CSSEditorController implements EditorController
{
//    private enum State {  FORMATTING, AUTO_COMPLETING  }
//    private PatternIndentationMap map;
//    private CodeEditor editor;
//    private ObjectProperty<State> state = new SimpleObjectProperty<>(State.FORMATTING);
//    private AutoCompletionDialog<AutoCompletionBase> dialog;
//    private CSS3AutoCompletionProvider autocompleter;
//    private String lastAlphanumericSegment;

    @Override
    public void start(File file, CodeEditor editor)
    {
//        this.editor = editor;
//        this.dialog = new AutoCompletionDialog<>(this.editor);
//        this.autocompleter = new CSS3AutoCompletionProvider();
//        this.map = new PatternIndentationMap("\\{", "\\}");
//        this.map.compute(this.editor.getDocument());
//        editor.ignoreDefaultBehaviorOf(KeyCode.ENTER, KeyCode.UP, KeyCode.DOWN);
//        applyIndentation();
//        registerListeners();
//        this.editor.addEventHandler(CodeEditorEvent.ANY, event -> this.map.compute(this.editor.getDocument()));
//        this.registerAutoCompletionListeners();
    }

//    private void registerListeners()
//    {
//        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            switch (this.state.get())
//            {
//                case FORMATTING:
//                    switch (event.getCode())
//                    {
//                        case ENTER:
//                            insertNewLine();
//                            break;
//                        case BACK_SPACE:
//                            deleteLineIfNecessary();
//                            break;
//                        case UP:
//                        case DOWN:
//                            TextFocusPosition pos = this.editor.getFocusPosition();
//                            IndexedDocument document = this.editor.getIndexedDocument();
//                            if (event.getCode() == KeyCode.UP && pos.line - 1 >= 0)
//                            {
//                                IndexedLine nextLine = document.lines.get(pos.line - 1);
//                                if (nextLine.isEmpty)
//                                    this.editor.moveTo(nextLine.start);
//                                else
//                                    this.editor.moveTo(nextLine.end);
//                            }
//                            else if (event.getCode() == KeyCode.DOWN && pos.line + 1 < document.lines.size())
//                            {
//                                IndexedLine nextLine = document.lines.get(pos.line + 1);
//                                if (nextLine.isEmpty)
//                                    this.editor.moveTo(nextLine.start);
//                                else
//                                    this.editor.moveTo(nextLine.end);
//                            }
//                            break;
//                        default: break;
//                    }
//                case AUTO_COMPLETING:
//                    break;
//                default: break;
//            }
//        });
//    }
//
//    private void registerAutoCompletionListeners()
//    {
//        this.editor.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.dialog.hide());
//        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.SEMICOLON || event.getCode() == KeyCode.BACK_SPACE)
//                this.dialog.hide();
//        });
//        this.editor.segmentChanges().addObserver(change -> {
//            if (change.isEmpty() || change.matches("\\s*"))
//                return;
//            TextFocusPosition position = this.editor.getFocusPosition();
//            List<? extends AutoCompletionBase> options = this.autocompleter.optionsFor(0, 0, position.line, change);
//            if (options != null && options.size() > 0)
//            {
//                this.dialog.getOptions().removeAll(options);
//                this.dialog.getOptions().addAll(options);
//                this.updateDialogPosition();
//            }
//
//        });
//        this.dialog.showingProperty().addListener(event -> {
//            if (this.dialog.isShowing())
//                this.state.setValue(State.AUTO_COMPLETING);
//            else
//                this.state.setValue(State.FORMATTING);
//        });
//        this.dialog.addEventHandler(AutoCompletionEvent.OPTION_WAS_SELECTED, event -> event.selectedOption.ifPresent(option -> {
//            int caretPos = this.editor.getCaretPosition();
//            this.editor.replaceText(caretPos - this.lastAlphanumericSegment.length(), caretPos, option.getInsertedText());
//            this.updateDialogPosition();
//        }));
//        this.dialog.addEventHandler(AutoCompletionEvent.SUB_OPTION_WAS_SELECTED, event -> event.selectedOption.ifPresent(option -> {
//            int caretPos = this.editor.getCaretPosition();
//            this.editor.replaceText(caretPos, caretPos, option.getInsertedText());
//            this.updateDialogPosition();
//            Platform.runLater(() -> this.dialog.hide());
//        }));
//    }
//
//    private void registerAutoClosingListeners()
//    {
//
//    }
//
//    private void insertNewLine()
//    {
//        IndexedLine currentLine = this.getCurrentLine();
//        String indent = this.getIndentation(this.map.indentLevelForLine(currentLine.number));
//        if (currentLine.end > this.editor.getDocument().length())
//            this.editor.replaceText(currentLine.end - 1, currentLine.end - 1, String.format("\n%s", indent));
//        else
//            this.editor.replaceText(currentLine.end, currentLine.end, String.format("\n%s", indent));
//        if (currentLine.text.equals(""))
//            this.editor.moveTo(this.editor.getCaretPosition() - 1);
//        this.map.compute(this.editor.getDocument());
//    }
//
//    private void deleteLineIfNecessary()
//    {
//        IndexedLine currentLine = this.getCurrentLine();
//        String indent = this.getIndentation(this.map.indentLevelForLine(currentLine.number));
//        if (currentLine.isBlank && currentLine.length == indent.length() - 1)
//            this.editor.deleteText(currentLine.start - 1, currentLine.end);
//    }
//
//    private void updateDialogPosition()
//    {
//        this.editor.getCaretBounds().ifPresent(bounds -> this.dialog.show(bounds.getMinX(), bounds.getMinY()));
//    }
//
//    private IndexedLine getCurrentLine()
//    {
//        int caretPosInLine = this.editor.getFocusPosition().line;
//        return this.editor.getIndexedDocument().lines.get(caretPosInLine);
//    }
//    private void applyIndentation()
//    {
//        this.map.compute(this.editor.getDocument());
//        List<IndexedLine> lines = this.editor.getIndexedDocument().lines;
//        for (int i = 0; i < lines.size(); i++)
//        {
//            IndexedLine line = lines.get(i);
//            int caretCharPosition = line.start;
//            String expectedIndentation = this.getIndentation(this.map.indentLevelForLine(line.number));
//            if (line.isEmpty)
//            {
//                int length = expectedIndentation.length();
//                String currentIndentation = (caretCharPosition - length) < 0 ? null : this.editor.getText(caretCharPosition - length, caretCharPosition);
//                if (currentIndentation != null && !(this.editor.getText(caretCharPosition - length, caretCharPosition).equals(expectedIndentation)))
//                    this.editor.replaceText(caretCharPosition, caretCharPosition, expectedIndentation);
//            }
//            lines = this.editor.getIndexedDocument().lines;
//            this.map.compute(this.editor.getDocument());
//        }
//    }
//    private String getIndentation(int level)
//    {
//        StringBuilder builder = new StringBuilder("");
//        String indent = "    ";
//        for (int i = 0; i < level; i++)
//            builder.append(indent);
//        return builder.toString();
//    }
}
