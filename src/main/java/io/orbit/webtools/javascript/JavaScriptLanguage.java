/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.orbit.webtools.javascript;

import io.orbit.api.language.LanguageDelegate;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import io.orbit.webtools.WebToolsController;
import io.orbit.webtools.javascript.highlighting.JavaScriptHighlighter;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 20, 2018
 * Time: 5:13 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptLanguage implements LanguageDelegate
{
    public static final String[] KEYWORDS = new String[] {
            "break", "do", "instanceof", "typeof", "case", "else",
            "new", "var", "let", "catch", "finally", "return", "void",
            "continue", "for", "switch", "while", "debugger", "function", "this",
            "with", "default", "if", "throw", "delete", "in", "try",
            "class", "enum", "extends", "super", "const", "export", "import",
            "implements", "private", "public", "interface", "package", "protected", "static", "yield"
    };

    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new JavaScriptHighlighter();
    }

    @Override
    public FileType getFileNameExtension()
    {
        return WebToolsController.JS_FILE();
    }
}
