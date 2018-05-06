package io.orbit.text;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Tyler Swann on Wednesday February 21, 2018 at 15:02
 */
public class CodeFormatter
{
    private TextDocumentEditor editor;
    private LanguageDelegate language;
    private int tabSize = 4;
    private String indent = "    ";
    //private ParserRuleList syntaxMap;

    public CodeFormatter()
    {

    }
    public CodeFormatter(TextDocumentEditor editor, LanguageDelegate language)
    {
        this.editor = editor;
        this.language = language;
    }

    public void format() throws UnsupportedOperationException
    {
        if (this.editor == null || this.language == null)
            throw new UnsupportedOperationException("CodeFormatter must have TextDocumentEditor and LanguageDelegate to function. These properties must not be null.");
        InputMap<Event> disabledButtons = InputMap.consume(EventPattern.anyOf(
                EventPattern.keyPressed(KeyCode.TAB),
                EventPattern.keyPressed(KeyCode.ENTER),
                EventPattern.keyPressed(KeyCode.BACK_SPACE)
        ));
        Nodes.addInputMap(this.editor, disabledButtons);
        IndentationMap map = new IndentationMap(this.editor.getDocument(), this.language.getHierarchicalPair());
        this.editor.caretPositionProperty().addListener(event -> {
            System.out.println(map.indentLevelForLine(getCurrentLine().number));
        });
//        if (this.language.getHierarchicalRuleContext() != null)
//        {
//            List<Class<? extends ParserRuleContext>> hierarchicalContext = new ArrayList<>();
//            hierarchicalContext.add(this.language.getHierarchicalRuleContext());
//            this.syntaxMap = new ParserRuleList(hierarchicalContext);
//            this.syntaxMap.map(this.language.getPrimaryExpression(this.editor.getText()));
//            // new Timer(1000, event -> System.out.println(this.syntaxMap.size())).start();
//            this.editor.caretPositionProperty().addListener(event -> this.printIndentationLevel(this.editor.getFocusPosition().line));
//        }
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventFilter(KeyEvent.KEY_RELEASED, event -> {

            if (!event.isConsumed())
            {
                this.indentDocument(event);
                this.autocompletePairs(event);
            }
            event.consume();
        });
    }



    private void autocompletePairs(KeyEvent event)
    {
        ArrayList<CharacterPair> pairs = new ArrayList<>(Arrays.asList(this.language.getAutoCompletingPairs()));
        String key = CharacterPair.keyEventText(event);
        String keyToInsert = null;
        pairs.addAll(Collections.singletonList(this.language.getHierarchicalPair()));
        int caretCharPosition = this.editor.getCaretPosition();
        for (CharacterPair charPair : pairs)
            if (key.equals(charPair.left))
                keyToInsert = charPair.right;
        if (keyToInsert == null)
        {
            for (CharacterPair charPair : pairs)
                if (key.equals(charPair.right) && this.editor.getText(caretCharPosition - 1, caretCharPosition).equals(charPair.right))
                    System.out.println(this.editor.getText(caretCharPosition - 1, caretCharPosition));
            //this.editor.deleteText(caretCharPosition - 1, caretCharPosition);
            return;
        }
//        if (getCurrentLine().isBlank)
//        {
//            int end = (this.caretCharPosition() - this.tabSize);
//            if (end >= 0)
//                this.editor.deleteText(end, caretCharPosition());
//        }
        this.editor.replaceText(caretCharPosition, caretCharPosition, keyToInsert);
        this.editor.moveTo(caretCharPosition);
        //this.editor.displaceCaret(caretCharPosition);
    }
    /*
        private int caretCharPosition() { return this.editor.getCaretPosition(); }
        private int caretLine() {  return this.editor.getFocusPosition().line;  }
        private Line getCurrentLine()
    * */
    private void indentDocument(KeyEvent event)
    {
        KeyCode key = event.getCode();

        Line line = this.getCurrentLine();
        int charNumberAtEnd = line.characterIndexAtEndOfLine;
        int charNumberAtStart = line.characterIndexAtStartOfLine;
        int characterCountInLine = line.numberOfCharactersInLine;

        switch (key)
        {
            case TAB:
                this.editor.replaceText(caretCharPosition(), caretCharPosition(), this.indent);
                break;
            case BACK_SPACE:
                if (line.isBlank)
                    this.editor.deleteText((caretCharPosition() - characterCountInLine - 1), caretCharPosition());
                else if (!this.editor.getSelectedText().equals(""))
                    this.editor.deleteText(this.editor.getCaretSelectionBind().startPositionProperty().getValue(), this.editor.getCaretSelectionBind().endPositionProperty().getValue());
                else
                    this.editor.deleteText(caretCharPosition() - 1, caretCharPosition());
                break;
            case ENTER:
                CharacterPair hierarchicalPair = this.language.getHierarchicalPair();
                String leftChar = caretCharPosition() - 1 < 0 ? null : this.editor.getText(caretCharPosition() - 1, caretCharPosition() );
                String rightChar = caretCharPosition() + 1 > this.editor.getLength() ? null : this.editor.getText(caretCharPosition() , caretCharPosition() + 1);
                if (line.text.length() < 2 )
                    this.editor.replaceText(caretCharPosition() , caretCharPosition() , "\n");
                else if (rightChar != null && leftChar != null && hierarchicalPair.right.equals(rightChar) && hierarchicalPair.left.equals(leftChar))
                {
                    int charPos = caretCharPosition();
                    String formattedLine = String.format("%s\n%s\n%s", leftChar, indent, rightChar);
                    this.editor.replaceText(charPos - 1, charPos + 1, formattedLine);
                    this.editor.moveTo(this.tabSize + charPos + 1);
                }
                else
                {
                    String newLineIndent = caretCharPosition() == charNumberAtEnd - 1 ? String.format("\n%s", indent) : "\n";
                    this.editor.replaceText(caretCharPosition() , caretCharPosition() , newLineIndent);
                }
                break;
        }
    }

//    private void printIndentationLevel(int line)
//    {
//        if (this.syntaxMap.hasRuleAtLine(line))
//            System.out.println(this.syntaxMap.parentTargetCountAtLine(line) + 1);
//    }


    private Document document() { return new Document(this.editor.getDocument()); }
    private int caretCharPosition() { return this.editor.getCaretPosition(); }
    private int caretLine() {  return this.editor.getFocusPosition().line;  }
    private Line getCurrentLine() { return this.document().lines[this.editor.getFocusPosition().line]; }

    public String getOuterTextOnCurrentLine(boolean left)
    {
        Document document = new Document(this.editor.getDocument());
        int caretCharPosition = this.editor.getCaretPosition();
        int caretLine = this.editor.getFocusPosition().line;
        int start = document.lines[caretLine].characterIndexAtStartOfLine;
        int end = document.lines[caretLine].characterIndexAtEndOfLine;
        if (left)
            return this.editor.getText(start, caretCharPosition);
        else
            return this.editor.getText(caretCharPosition, end);
    }


    private String multiply(String word, int times)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++)
            builder.append(word);
        return builder.toString();
    }

    public void setTabSize(int tabSize)
    {
        this.tabSize = tabSize;
        this.indent = multiply(" ", tabSize);
    }
    public int getTabSize() { return this.tabSize; }
    public TextDocumentEditor getEditor() { return editor; }
    public void setEditor(TextDocumentEditor editor) { this.editor = editor; }
    public LanguageDelegate getLanguage() { return language; }
    public void setLanguage(LanguageDelegate language) { this.language = language; }
}
