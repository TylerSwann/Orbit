package io.orbit.webtools.html;

import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.CodeFormatter;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.EventStreams;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Friday June 22, 2018 at 16:36
 */
public class HTMLCodeFormatter extends CodeFormatter
{
    private static final Pattern VOID_TAG_PATTERN = Pattern.compile("(?<VOIDTAG><(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr)[\\sa-zA-Z0-9=\".=+\\-_:?/,]*>)");
    private static final Pattern OPEN_TAG_PATTERN = Pattern.compile("(?<OPENTAG>((<)(?!(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr))[a-zA-Z0-9]+[\\sa-zA-Z0-9=\":/.-]*>))");
    private static final Pattern CLOSE_TAG_PATTERN = Pattern.compile("(?<CLOSETAG>(</[a-zA-Z0-9\\-]+>))");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?<COMMENT>(<!--.*-->))");
    private static final Pattern TAG_PATTERN = Pattern.compile("(?<SINGLE>((<.*>).*(</[a-zA-Z0-9\\-]+>)))");

    private boolean hasSelectedText = false;

    public HTMLCodeFormatter(CodeEditor editor)
    {
        super(editor);
        InputMap<KeyEvent> disabledEnterKey = InputMap.consume(EventPattern.keyPressed(KeyCode.ENTER).onlyIf(event -> !(this.isPaused())));
        Nodes.addInputMap(this.editor, disabledEnterKey);
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
                            boolean leftIsOpenTag = textLeftOfCaret.matches(String.format("\\s*%s", OPEN_TAG_PATTERN.pattern()));
                            boolean rightIsCloseTag = textRightOfCaret.matches(CLOSE_TAG_PATTERN.pattern());
                            /*
                             * If the text left of the caret is a open tag and the text right of the caret is a
                             * close tag, insert two new lines and increase the indentation between those elements
                             */
                            if (leftIsOpenTag && rightIsCloseTag)
                            {
                                currentIndent = lineText.replaceAll(TAG_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4) + 1);
                                this.editor.insertText(characterNumber, String.format("\n%s\n%s", indent, currentIndent));
                                this.editor.moveTo(characterNumber + indent.length() + 1);
                            }
                            /*
                             *  If the line the user is on contains an open and closed tag and the caret is at the end
                             *  of the line, use the indentation from the current line for the new line being inserted.
                             */
                            else if (textLeftOfCaret.matches(String.format("\\s*%s", TAG_PATTERN.pattern())))
                            {
                                currentIndent = textLeftOfCaret.replaceAll(TAG_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4));
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            /*
                             * If the text left of the caret contains and open tag, grab the current indentation,
                             * increase it by 1 (4 spaces), and insert it into the new line
                             */
                            else if (leftIsOpenTag)
                            {
                                currentIndent = lineText.replaceAll(OPEN_TAG_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4) + 1);
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            /*
                             * If the text left of the caret matches a void element (an element with no closing tag ex: input, meta, img),
                             * use the indentation of the current line for the new line being inserted
                             */
                            else if (textLeftOfCaret.matches(String.format("^\\s*%s", VOID_TAG_PATTERN.pattern())))
                            {
                                currentIndent = textLeftOfCaret.replaceAll(VOID_TAG_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4));
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            /*
                             * If the text left of the caret matches a closing tag, use the
                             * indentation of the current line for the new line being inserted
                             */
                            else if (textLeftOfCaret.matches(String.format("^\\s*%s", CLOSE_TAG_PATTERN.pattern())))
                            {
                                currentIndent = textLeftOfCaret.replaceAll(CLOSE_TAG_PATTERN.pattern(), "");
                                indent = getIndent((currentIndent.length() / 4));
                                this.editor.insertText(characterNumber, String.format("\n%s", indent));
                            }
                            /*
                             * If the text left of the caret is nothing but white space, use the
                             * indentation of the current line for the new line being inserted
                             */
                            else if (textLeftOfCaret.matches("^\\s+"))
                                this.editor.insertText(characterNumber, String.format("\n%s", textLeftOfCaret));
                            /*
                             *  If all else fails, simply insert a new line character.
                             */
                            else
                                this.editor.insertText(characterNumber, "\n");
                            break;
                        case BACK_SPACE:
                            if (lineText.matches("\\s*") && !this.hasSelectedText && !textLeftOfCaret.equals(""))
                                this.editor.replaceText(((characterNumber - 1) - lineText.length()), characterNumber, "");
                            break;
                        default: break;
                    }
                });
    }

    @Override
    public String reformat(String source)
    {
        int indentLevel = -1;
        source = trimmed(source);
        StringBuilder builder = new StringBuilder("");
        for (String line : source.split("\n"))
        {
            if (line.matches("^[\\s\n]+"))
            {
                builder.append(String.format("%s\n", getIndent(indentLevel)));
                continue;
            }
            Matcher openMatcher = OPEN_TAG_PATTERN.matcher(line);
            Matcher closeMatcher = CLOSE_TAG_PATTERN.matcher(line);
            Matcher voidMatcher = VOID_TAG_PATTERN.matcher(line);
            Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
            if (commentMatcher.matches())
            {
                builder.append(String.format("%s%s\n", getIndent(indentLevel + 1), line));
                continue;
            }
            while (openMatcher.find())
                indentLevel++;
            if (voidMatcher.matches())
                builder.append(String.format("%s%s\n", getIndent(indentLevel + 1), line));
            else
                builder.append(String.format("%s%s\n", getIndent(indentLevel), line));
            while (closeMatcher.find())
                indentLevel--;
        }
        return builder.toString();
    }

    private static String trimmed(String source)
    {
        StringBuilder builder = new StringBuilder();
        for (String line : source.split("\n"))
        {
            String trimmed = line.replaceAll("^\\s+", "");
            builder.append(String.format("%s\n", trimmed));
        }
        return builder.toString();
    }
}
//        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
//            String textLeftOfCaret = this.editor.getTextLeftOfCaret();
//            String textRightOfCaret = this.editor.getTextRightOfCaret();
//            int lineNumber = this.editor.getFocusPosition().line;
//            int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
//            String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
//            switch (event.getCode())
//            {
//                case ENTER:
//                    String currentIndent;
//                    String indent;
//                    if (textLeftOfCaret == null)
//                    {
//                        this.editor.insertText(characterNumber, "\n");
//                        break;
//                    }
//                    /*
//                     *  If the line the user is on contains an open and closed tag and the caret is at the end
//                     *  of the line, use the indentation from the current line for the new line being inserted.
//                     */
//                    if (textLeftOfCaret.matches(String.format("\\s*%s", TAG_PATTERN.pattern())))
//                    {
//                        currentIndent = textLeftOfCaret.replaceAll(TAG_PATTERN.pattern(), "");
//                        indent = getIndent((currentIndent.length() / 4));
//                        this.editor.insertText(characterNumber, String.format("\n%s", indent));
//                    }
//                    /*
//                     * If the text left of the caret contains and open tag, grab the current indentation,
//                     * increase it by 1 (4 spaces), and insert it into the new line
//                     */
//                    else if (textLeftOfCaret.matches(String.format("^\\s*%s", OPEN_TAG_PATTERN.pattern())))
//                    {
//                        currentIndent = lineText.replaceAll(OPEN_TAG_PATTERN.pattern(), "");
//                        indent = getIndent((currentIndent.length() / 4) + 1);
//                        this.editor.insertText(characterNumber, String.format("\n%s", indent));
//                    }
//                    /*
//                     * If the text left of the caret matches a void element (an element with no closing tag ex: input, meta, img),
//                     * use the indentation of the current line for the new line being inserted
//                     */
//                    else if (textLeftOfCaret.matches(String.format("^\\s*%s", VOID_TAG_PATTERN.pattern())))
//                    {
//                        currentIndent = textLeftOfCaret.replaceAll(VOID_TAG_PATTERN.pattern(), "");
//                        indent = getIndent((currentIndent.length() / 4));
//                        this.editor.insertText(characterNumber, String.format("\n%s", indent));
//                    }
//                    /*
//                     * If the text left of the caret matches a closing tag, use the
//                     * indentation of the current line for the new line being inserted
//                     */
//                    else if (textLeftOfCaret.matches(String.format("^\\s*%s", CLOSE_TAG_PATTERN.pattern())))
//                    {
//                        currentIndent = textLeftOfCaret.replaceAll(CLOSE_TAG_PATTERN.pattern(), "");
//                        indent = getIndent((currentIndent.length() / 4));
//                        this.editor.insertText(characterNumber, String.format("\n%s", indent));
//                    }
//                    /*
//                     * If the text left of the caret is nothing but white space, use the
//                     * indentation of the current line for the new line being inserted
//                     */
//                    else if (textLeftOfCaret.matches("^\\s+"))
//                        this.editor.insertText(characterNumber, String.format("\n%s", textLeftOfCaret));
//                    /*
//                     *  If all else fails, simply insert a new line character.
//                     */
//                    else
//                        this.editor.insertText(characterNumber, "\n");
//                    break;
//                case BACK_SPACE:
//                    if (lineText.matches("\\s*"))
//                        this.editor.replaceText(((characterNumber - 1) - lineText.length()), characterNumber, "");
//                default: break;
//            }
//        });
//
//    private static String getIndent(int level)
//    {
//        StringBuilder builder = new StringBuilder("");
//        for (int i = 0; i < level; i++)
//            builder.append("    ");
//        return builder.toString();
//    }