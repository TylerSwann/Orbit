package io.orbit.webtools.formatting;

import org.fxmisc.richtext.model.StyledDocument;

import java.util.Collection;

/**
 * Created by Tyler Swann on Saturday April 21, 2018 at 12:45
 */
public interface IndentationMap
{
    int indentLevelForLine(int lineNumber);
    void compute(StyledDocument<Collection<String>, String, Collection<String>> document);
}
