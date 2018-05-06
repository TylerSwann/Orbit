package io.orbit.api;

import io.orbit.api.autocompletion.AutoCompletionProvider;
import io.orbit.api.highlighting.SyntaxHighlighter;


/**
 * Created by Tyler Swann on Thursday February 01, 2018 at 15:50
 */
public interface LanguageDelegate
{

    @NotNullable
    SyntaxHighlighter getSyntaxHighlighter();


    /**
     *
     * The file name extension of your language.
     * DO NOT include the dot at the beginning.
     * example:
     * for the java language:
     * return "java";
     *
     * @return - The file name extension of your language.
     */
    @NotNullable
    String getFileNameExtension();

    @Nullable
    default AutoCompletionProvider getAutoCompletionProvider() {  return null;  }
}
