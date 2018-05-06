package io.orbit.text;

import io.orbit.api.highlighting.ANTLRHighlightingProvider;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.text.CodeEditor;
import io.orbit.util.IndexRange;
import javafx.concurrent.Task;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.*;
import java.time.Duration;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:51
 */
public class ANTLRSyntaxHighlighter implements ParseTreeListener, ANTLRErrorListener, HighlightingService<Void>
{
    private final ANTLRHighlightingProvider provider;
    private final CodeEditor editor;
    private ExecutorService service;
    private ParseTreeWalker walker;
    private Map<IndexRange, String> styleSegments = new HashMap<>();

    public ANTLRSyntaxHighlighter(ANTLRHighlightingProvider provider, CodeEditor editor)
    {
        this.provider = provider;
        this.editor = editor;
        this.walker = new ParseTreeWalker();
        this.provider.getParser().removeParseListeners();
        this.provider.getParser().removeErrorListeners();
        this.provider.getLexer().removeErrorListeners();
        this.provider.getParser().addErrorListener(this);
        this.provider.getParser().addParseListener(this);
    }

    @Override
    public void highlight(ExecutorService service)
    {
        this.service = service;
        this.editor.plainTextChanges()
                .successionEnds(Duration.ofMillis(50))
                .supplyTask(this::highlightAsynchronously)
                .awaitLatest(this.editor.plainTextChanges())
                .filterMap(t -> {
                    if (t.isSuccess())
                        return Optional.of(t.get());
                    else
                    {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                }).subscribe(this::applyHighlighting);
    }

    private Task<Integer> highlightAsynchronously()
    {
        String text = this.editor.getText();
        ANTLRSyntaxHighlighter self = this;
        Task<Integer> task = new Task<Integer>()
        {
            @Override
            protected Integer call() throws Exception
            {
                ParseTree parseTree = self.provider.getPrimaryExpression(text);
                walker.walk(self, parseTree);
                return 0;
            }
        };
        this.service.execute(task);
        return task;
    }

    private void applyHighlighting(Integer unused)
    {
        for (Map.Entry<IndexRange, String> set : this.styleSegments.entrySet())
        {
            IndexRange range = set.getKey();
            if (range.start >= 0 && range.end < this.editor.getLength())
            {
                this.editor.clearStyle(range.start, range.end);
                this.editor.setStyleClass(range.start, range.end, set.getValue());
            }
        }
    }

    private void setStyleClass(int from, int to, String styleClass)
    {
        this.styleSegments.put(new IndexRange(from, to), styleClass);
    }

    @Override
    public void visitTerminal(TerminalNode node)
    {
        if (isInvalidToken(node.getSymbol()))
            return;
        HighlightType type = this.provider.getHighlightType(node);
        if (type != HighlightType.EMPTY)
            this.setStyleClass(node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex() + 1, type.className);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx)
    {
        if (isInvalidToken(ctx.start, ctx.stop))
            return;
        int start = ctx.start.getStartIndex();
        int stop = ctx.stop.getStopIndex() + 1;
        HighlightType type = this.provider.getHighlightType(ctx);
        if (type != HighlightType.EMPTY)
            this.setStyleClass(start, stop, type.className);
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
