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
package io.orbit.webtools;

import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;
import io.orbit.api.SVGIcon;
import io.orbit.api.text.FileType;
import io.orbit.webtools.css.CSS3Language;
import io.orbit.webtools.css.CSSEditorController;
import io.orbit.webtools.html.HTMLEditorController;
import io.orbit.webtools.html.HTMLLanguage;
import io.orbit.webtools.javascript.JavaScriptController;
import io.orbit.webtools.javascript.JavaScriptLanguage;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:38
 */
public class WebToolsController implements PluginController
{
    private static final FileType HTML_FILE_TYPE;
    private static final FileType CSS_FILE_TYPE;
    private static final FileType JS_FILE_TYPE;

    static {
        HTML_FILE_TYPE = new FileType("html", "text/html", "HTML File", new SVGIcon(FontAwesomeBrands.HTML5), Color.valueOf("#E25133"), WebToolsController::createHTMLFile);
        CSS_FILE_TYPE = new FileType("css", "text/css", "Stylesheet", new SVGIcon(FontAwesomeBrands.CSS3_ALT), Color.valueOf("#1F71B1"), WebToolsController::createCSSFile);
        JS_FILE_TYPE = new FileType("js", "application/javascript", "JavaScript File", new SVGIcon(FontAwesomeBrands.JS_SQUARE), Color.valueOf("#EFD960"), WebToolsController::createJSFile);
    }

    @Override
    public List<FileType> getFileTypes()
    {
        return Arrays.asList(HTML_FILE(), STYLESHEET(), JS_FILE());
    }



    @Override
    public LanguageDelegate getLanguageDelegate(File file, String fileExtension)
    {
        if (fileExtension.equals(CSS_FILE_TYPE.getExtension()))
            return new CSS3Language();
        else if (fileExtension.equals(HTML_FILE_TYPE.getExtension()))
            return new HTMLLanguage();
        else if (fileExtension.equals(JS_FILE_TYPE.getExtension()))
            return new JavaScriptLanguage();
        return null;
    }

    @Override
    public EditorController getEditorController(File file, String fileExtension)
    {
        if (fileExtension.equals(CSS_FILE_TYPE.getExtension()))
            return new CSSEditorController();
        else if (fileExtension.equals(HTML_FILE_TYPE.getExtension()))
            return new HTMLEditorController();
        else if (fileExtension.equals(JS_FILE_TYPE.getExtension()))
            return new JavaScriptController();
        return null;
    }

    public static FileType STYLESHEET() { return CSS_FILE_TYPE.clone(); }
    public static FileType HTML_FILE() { return HTML_FILE_TYPE.clone(); }
    public static FileType JS_FILE() { return JS_FILE_TYPE.clone(); }

    protected static void createHTMLFile(File file) { createFile(file); }
    protected static void createCSSFile(File file) { createFile(file); }
    protected static void createJSFile(File file) { createFile(file); }

    private static void createFile(File file)
    {
        try { file.createNewFile(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
