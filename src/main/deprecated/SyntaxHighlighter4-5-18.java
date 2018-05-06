package io.orbit.text;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.LanguageDelegate;
import io.orbit.ui.MUIGutterButton;
import io.orbit.util.IndexRange;
import javafx.animation.PauseTransition;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.time.Duration;
import java.util.*;

/**
 * Created by Tyler Swann on Thursday February 01, 2018 at 15:50
 */
public class SyntaxHighlighter implements ParseTreeListener, ANTLRErrorListener
{
    private Map<Integer, MUIGutterButton> gutterButtons = new HashMap<>();

    private LanguageDelegate language;
    private CodeEditor editor;
    private final ParseTreeWalker walker;
    private int reHighlightStart = 0;
    private int reHighlightEnd = -1;
    private IndexRange reHighlightRange;

    public SyntaxHighlighter(CodeEditor editor, LanguageDelegate language)
    {
        gutterButtons.clear();
        this.language = language;
        this.editor = editor;
        this.walker = new ParseTreeWalker();
        this.language.getParser().removeParseListeners();
        this.language.getParser().removeErrorListeners();
        this.language.getLexer().removeErrorListeners();
        this.language.getParser().addErrorListener(this);
        this.language.getParser().addParseListener(this);
        this.reHighlightEnd = this.editor.getText().length();
        this.editor.setParagraphGraphicFactory(line -> {
            MUIGutterButton gutterButton = new MUIGutterButton(line + 1);
            gutterButtons.put(line + 1, gutterButton);
            return gutterButton;
        });

        this.editor.plainTextChanges()
                .successionEnds(Duration.ofMillis(10))
                .addObserver(event -> this.highlight());

        this.editor.plainTextChanges().addObserver(event -> {
            if (reHighlightStart == -1 && event.getRemovalEnd() > 0)
                this.reHighlightStart = event.getRemovalEnd();
            this.reHighlightEnd = event.getInsertionEnd();
        });
    }

    public void highlight()
    {
        this.reHighlightRange = new IndexRange(this.reHighlightStart, this.reHighlightEnd);
        if (this.reHighlightRange.isNegative)
            return;
        this.reset(this.reHighlightStart, this.reHighlightEnd);
        this.walker.walk(this, this.language.getPrimaryExpression(this.editor.getText()));
        wait(javafx.util.Duration.millis(5.0), () -> {
            this.reHighlightEnd = -1;
            this.reHighlightEnd = -1;
        });
    }

    private void wait(javafx.util.Duration duration, Runnable then)
    {
        PauseTransition transition = new PauseTransition(duration);
        transition.setOnFinished(event -> then.run());
        transition.play();
    }

    private void setStyleClass(int from, int to, String styleClass)
    {
        IndexRange range = new IndexRange(from, to);
        if (this.reHighlightRange.isInRange(range))
            this.editor.setStyleClass(from, to, styleClass);
    }



    private boolean isInvalidToken(Token start, Token stop)
    {
        return isInvalidToken(start) || isInvalidToken(stop);
    }
    private boolean isInvalidToken(Token token)
    {
        return token == null ||
                token.getStartIndex() < 0 ||
                token.getStopIndex() < 0 ||
                token.getStopIndex() >= this.editor.getText().length() - 1;
    }


    @Override
    public void visitTerminal(TerminalNode node)
    {
        if (isInvalidToken(node.getSymbol()))
            return;
        HighlightType type = this.language.getHighlightType(node);
        if (type != HighlightType.EMPTY)
            this.setStyleClass(node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex() + 1, type.className);
    }

    private void reset()
    {
        this.editor.clearStyle(0, this.editor.getText().length());
    }

    private void reset(int start, int end)
    {
        this.editor.clearStyle(start, end);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx)
    {
        if (isInvalidToken(ctx.start, ctx.stop))
            return;
        int start = ctx.start.getStartIndex();
        int stop = ctx.stop.getStopIndex() + 1;
        HighlightType type = this.language.getHighlightType(ctx);
        if (type != HighlightType.EMPTY)
            this.setStyleClass(start, stop, type.className);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) { }
    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) { }
    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) { }
    @Override
    public void exitEveryRule(ParserRuleContext ctx) { }
    @Override
    public void visitErrorNode(ErrorNode node) { }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) { }
}
