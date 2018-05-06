package old;

import io.orbit.text.CodeEditor;
import old.test.LanguageDelegate;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 16:44
 */
public class SyntaxHighlighter implements ParseTreeListener, ANTLRErrorListener
{
    private static ArrayList<String> keywordStyleClasses;
    private static ArrayList<String> errorStyleClasses;
    private static ArrayList<String> typeStyleClasses;
    private static ArrayList<String> blockCommentStyleClasses;
    private static ArrayList<String> lineCommentStyleClasses;
    private CodeEditor editor;
    private LanguageDelegate language;
    private ParseTreeWalker walker;

    static {
        keywordStyleClasses = new ArrayList<>();
        errorStyleClasses = new ArrayList<>();
        typeStyleClasses = new ArrayList<>();
        blockCommentStyleClasses = new ArrayList<>();
        lineCommentStyleClasses = new ArrayList<>();
        keywordStyleClasses.add("keyword");
        errorStyleClasses.add("error");
        typeStyleClasses.add("type");
        blockCommentStyleClasses.add("block-comment");
        lineCommentStyleClasses.add("line-comment");
    }

    public SyntaxHighlighter(CodeEditor editor, LanguageDelegate language)
    {
        this.language = language;
        this.editor = editor;
        this.language.getParser().addErrorListener(this);
        this.walker = new ParseTreeWalker();
    }

    public void highlight()
    {
        this.editor.clearStyle(0, this.editor.getText().length());
        ParserRuleContext context = this.language.getPrimaryExpression(this.editor.getText());
        walker.walk(this, context);
    }

    private void apply(TerminalNode node, ArrayList<String> styleClasses)
    {
        this.apply(node.getSymbol(), styleClasses);
    }

    private void apply(Token token, ArrayList<String> styleClasses)
    {
        if (token.getStartIndex() < 0 || token.getStopIndex() < 0) return;
        this.editor.setStyle(token.getStartIndex(), token.getStopIndex() + 1, styleClasses);
    }


    private void apply(int line, ArrayList<String> styleClasses)
    {
        this.editor.clearParagraphStyle(line);
        this.editor.setStyle(line, styleClasses);
    }

    private void apply(ParserRuleContext context, ArrayList<String> styleClasses)
    {
        this.editor.setStyle(context.start.getStartIndex(), context.stop.getStopIndex() + 1, styleClasses);
    }

    /**
     *
     * These methods are required to be implemented by ANTLRErrorListener.
     * The method syntaxError is called when the parser finds syntax error.
     *
     * @param recognizer
     * @param offendingSymbol
     * @param line
     * @param charPositionInLine
     * @param msg
     * @param e
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
    {
        if (offendingSymbol instanceof Token)
            this.apply(((Token)offendingSymbol), errorStyleClasses);
    }

    private void typeis(Class<? extends TerminalNode> term, TerminalNode context)
    {
        if (context.getClass() == term)
        {

        }
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) { }
    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) { }
    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) { }

    /**
     * These methods are required to be implemented by ParseTreeListener.
     * When the parser identifies a syntax rule, this method is called. I then check to see
     * if the TerminalNode's type, a unique integer value, matches any of specified types given by
     * the LanguageDelegate. The syntax is then highlighted appropriately by adding the correct style class.
     * @param node
     */
    @Override
    public void visitTerminal(TerminalNode node)
    {
        int type = node.getSymbol().getType();
        if (ruleMatches(type, this.language.getKeywords()))
            this.apply(node, keywordStyleClasses);
        else if (ruleMatches(type, this.language.getTypeRule()))
            this.apply(node, typeStyleClasses);
        else if (ruleMatches(type, this.language.getBlockCommentRule()))
            this.apply(node, blockCommentStyleClasses);
        else if (ruleMatches(type, this.language.getLineCommentRule()))
            this.apply(node, lineCommentStyleClasses);
    }

    private boolean ruleMatches(int type, int[] rules)
    {
        for (int rule : rules)
            if (rule == type)
                return true;
        return false;
    }

    @Override
    public void visitErrorNode(ErrorNode node)
    {
        this.apply(node.getSymbol(), errorStyleClasses);
    }
    @Override
    public void enterEveryRule(ParserRuleContext ctx)
    {
        int rule = ctx.getRuleIndex();
        if (ruleMatches(rule, this.language.getTypeRule()))
            this.apply(ctx, typeStyleClasses);
    }
    @Override
    public void exitEveryRule(ParserRuleContext ctx) { }
}
