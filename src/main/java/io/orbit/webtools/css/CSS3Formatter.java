package io.orbit.webtools.css;

import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.CodeFormatter;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStreams;

import java.util.regex.Pattern;


/**
 * Created by Tyler Swann on Saturday June 23, 2018 at 17:08
 */
public class CSS3Formatter extends CodeFormatter
{
    private static final Pattern LEFT_BRACKET_PATTERN = Pattern.compile(".*?\\{.*?");
    private static final Pattern RIGHT_BRACKET_PATTERN = Pattern.compile(".*?}.*?");
    private static final Pattern BLOCK_PATTERN = Pattern.compile("\\{(\\s*?.*?)*?}");
    private static final Pattern PROPERTY_PATTERN = Pattern.compile("([a-zA-Z-_]+:\\s*).*?[a-zA-Z0-9.#%()]+;");

    private boolean hasSelectedText = false;

    public CSS3Formatter(CodeEditor editor)
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
        EventStreams.eventsOf(this.editor, KeyEvent.KEY_PRESSED)
                .pauseWhen(paused)
                .addObserver(event -> {
                    String textLeftOfCaret = this.editor.getTextLeftOfCaret() == null ? "" : this.editor.getTextLeftOfCaret();
                    String textRightOfCaret = this.editor.getTextRightOfCaret() == null ? "" : this.editor.getTextRightOfCaret();
                    int lineNumber = this.editor.getFocusPosition().line;
                    int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
                    String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
                    switch (event.getCode())
                    {
                        case ENTER:
                            String currentIndent;
                            String indent;
                            boolean leftIsOpenBracket = textLeftOfCaret.matches(LEFT_BRACKET_PATTERN.pattern());
                            boolean rightIsCloseBracket = textRightOfCaret.matches(RIGHT_BRACKET_PATTERN.pattern());
                            if (leftIsOpenBracket && rightIsCloseBracket)
                            {
                                currentIndent = lineText.replaceAll(BLOCK_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4) + 1);
                                this.editor.insertText(characterNumber, String.format("\n%s\n%s", indent, currentIndent));
                                this.editor.moveTo(characterNumber + indent.length() + 1);
                            }
                            else if (leftIsOpenBracket)
                            {
                                currentIndent = textLeftOfCaret.replaceAll(LEFT_BRACKET_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4) + 1);
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            else if (textLeftOfCaret.matches("^\\s+"))
                                this.editor.insertText(characterNumber, String.format("\n%s", textLeftOfCaret));
                            else if (textLeftOfCaret.matches(String.format("^\\s*%s", PROPERTY_PATTERN.pattern())))
                            {
                                currentIndent = textLeftOfCaret.replaceAll(PROPERTY_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4));
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            else
                                this.editor.insertText(characterNumber, "\n");
                            break;
                        case BACK_SPACE:
                            if (lineText.matches("\\s*") && !this.hasSelectedText && !textLeftOfCaret.equals(""))
                                this.editor.replaceText(((characterNumber - 1) - lineText.length()), characterNumber, "");
                            break;
                        case TAB:
                            this.editor.insertText(characterNumber, getIndent(1));
                            break;
                        default: break;
                    }
                });
    }

    @Override
    public String reformat(String source)
    {
        return "";
    }
}
