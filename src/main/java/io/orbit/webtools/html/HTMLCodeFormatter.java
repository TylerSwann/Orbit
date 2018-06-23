package io.orbit.webtools.html;

import io.orbit.api.text.CodeEditor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Friday June 22, 2018 at 16:36
 */
public class HTMLCodeFormatter
{
    private static final Pattern VOID_TAG_PATTERN = Pattern.compile("(?<VOIDTAG><(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr)[\\sa-zA-Z0-9=\".=+\\-_:?/,]*>)");
    private static final Pattern OPEN_TAG_PATTERN = Pattern.compile("(?<OPENTAG>((<)(?!(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr))[a-zA-Z0-9]+[\\sa-zA-Z0-9=\":/.-]*>))");
    private static final Pattern CLOSE_TAG_PATTERN = Pattern.compile("(?<CLOSETAG>(</[a-zA-Z0-9\\-]+>))");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?<COMMENT>(<!--.*-->))");
    private static final Pattern TAG_PATTERN = Pattern.compile("(?<SINGLE>((<.*>).*(</[a-zA-Z0-9\\-]+>)))");

    private final CodeEditor editor;

    public HTMLCodeFormatter(CodeEditor editor)
    {
        this.editor = editor;
        this.editor.ignoreDefaultBehaviorOf(KeyCode.ENTER);
        registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String textLeftOfCaret = this.editor.getTextLeftOfCaret();
            String textRightOfCaret = this.editor.getTextRightOfCaret();
            int lineNumber = this.editor.getFocusPosition().line;
            int characterNumber = this.editor.getFocusPosition().caretPositionInDocument;
            String lineText = this.editor.getIndexedDocument().lines.get(lineNumber).text;
            switch (event.getCode())
            {
                case ENTER:
                    String currentIndent;
                    String indent;
                    if (textLeftOfCaret == null)
                    {
                        this.editor.insertText(characterNumber, "\n");
                        break;
                    }
                    /*
                     *  If the line the user is on contains an open and closed tag and the caret is at the end
                     *  of the line, use the indentation from the current line for the new line being inserted.
                     */
                    if (textLeftOfCaret.matches(String.format("\\s*%s", TAG_PATTERN.pattern())))
                    {
                        currentIndent = textLeftOfCaret.replaceAll(TAG_PATTERN.pattern(), "");
                        indent = getIndent((currentIndent.length() / 4));
                        this.editor.insertText(characterNumber, String.format("\n%s", indent));
                    }
                    /*
                     * If the text left of the caret contains and open tag, grab the current indentation,
                     * increase it by 1 (4 spaces), and insert it into the new line
                     */
                    else if (textLeftOfCaret.matches(String.format("^\\s*%s", OPEN_TAG_PATTERN.pattern())))
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
                    if (lineText.matches("\\s*"))
                        this.editor.replaceText(((characterNumber - 1) - lineText.length()), characterNumber, "");
                default: break;
            }
        });
    }

    public static String format(String source)
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

    public static void computeIndentation(String source)
    {
        Map<Integer, String> map = new HashMap<>();
        Matcher openMatcher = OPEN_TAG_PATTERN.matcher(source);
        Matcher closeMatcher = CLOSE_TAG_PATTERN.matcher(source);
        Matcher voidMatcher = VOID_TAG_PATTERN.matcher(source);
        while (openMatcher.find())
        {
            int start = openMatcher.start();
            int end = openMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("OPEN: %d -> %d\n\t%s", start, end, text));
        }
        while (closeMatcher.find())
        {
            int start = closeMatcher.start();
            int end = closeMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("CLOSE: %d -> %d\n\t%s", start, end, text));
        }
        while (voidMatcher.find())
        {
            int start = voidMatcher.start();
            int end = voidMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("VOID: %d -> %d\n\t%s", start, end, text));
        }
    }

    private static String getIndent(int level)
    {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < level; i++)
            builder.append("    ");
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
