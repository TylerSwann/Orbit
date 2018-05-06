package io.orbit.text;

import io.orbit.api.highlighting.ANTLRHighlightingProvider;
import io.orbit.api.highlighting.HighlightingProvider;
import io.orbit.api.highlighting.RegexHighlightingProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:39
 */
@Deprecated
public class SyntaxHighlighter
{
    private HighlightingService highlightingService;
    private ExecutorService service;

    public SyntaxHighlighter(HighlightingProvider highlightingProvider, OrbitEditor editor)
    {
        if (highlightingProvider instanceof RegexHighlightingProvider)
            highlightingService = new RegexSyntaxHighlighter(((RegexHighlightingProvider) highlightingProvider), editor);
        else if (highlightingProvider instanceof ANTLRHighlightingProvider)
            highlightingService = new ANTLRSyntaxHighlighter(((ANTLRHighlightingProvider) highlightingProvider), editor);
        else
            throw new IllegalArgumentException("HighlightingProvider provided to SyntaxHighlighter if neither RegexHighlightingProvider nor ANTLRHighlightingProvider!");
        service = Executors.newSingleThreadExecutor();
        this.highlightingService.highlight(service);
    }
}
