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
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:38
 */
public class WebToolsController implements PluginController
{
    @Override
    public List<FileType> getFileTypes()
    {
        FileType html = new FileType("html", "text/html", new SVGIcon(FontAwesomeBrands.HTML5));
        FileType css = new FileType("css", "text/css", new SVGIcon(FontAwesomeBrands.CSS3_ALT));
        return Arrays.asList(html, css);
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
}
