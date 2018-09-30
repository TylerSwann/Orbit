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

    static {
        HTML_FILE_TYPE = new FileType("html", "text/html", "HTML File", new SVGIcon(FontAwesomeBrands.HTML5), WebToolsController::createHTMLFile);
        CSS_FILE_TYPE = new FileType("css", "text/css", "Stylesheet", new SVGIcon(FontAwesomeBrands.CSS3_ALT), WebToolsController::createCSSFile);
    }

    @Override
    public List<FileType> getFileTypes()
    {
        return Arrays.asList(HTML_FILE(), STYLESHEET());
    }



    @Override
    public LanguageDelegate getLanguageDelegate(File file, String fileExtension)
    {
        if (fileExtension.equals("css"))
            return new CSS3Language();
        else if (fileExtension.equals("html"))
            return new HTMLLanguage();
        return null;
    }

    @Override
    public EditorController getEditorController(File file, String fileExtension)
    {
        if (fileExtension.equals("css"))
            return new CSSEditorController();
        else if (fileExtension.equals("html"))
            return new HTMLEditorController();
        return null;
    }

    public static FileType STYLESHEET() { return CSS_FILE_TYPE.clone(); }
    public static FileType HTML_FILE() { return HTML_FILE_TYPE.clone(); }

    protected static void createHTMLFile(File file)
    {
        try
        {
            file.createNewFile();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    protected static void createCSSFile(File file)
    {
        try { file.createNewFile(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
