package io.orbit.api.language;

import io.orbit.api.NotNullable;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;


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
    FileType getFileNameExtension();
}
