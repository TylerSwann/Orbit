package io.orbit.ui;

import io.orbit.App;
import io.orbit.api.SVGIcon;
import io.orbit.settings.ProjectFile;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.io.File;
import java.net.URL;


/**
 * Created by Tyler Swann on Wednesday May 16, 2018 at 13:40
 */
public final class LanguageIcons
{
    private LanguageIcons() {}

    /**
     * Default Icon incase the loaded icon is unavailable, can't be loaded for whatever reason,
     * or there isn't an available Icon for the file type. This will probably not happen.
     */
    private static final Ikon DEFAULT_ICON = FontAwesomeSolid.FILE_ALT;
    private static final Ikon FOLDER_ICON = FontAwesomeSolid.FOLDER_OPEN;
    private static final Ikon HTML_ICON = FontAwesomeSolid.CODE;
    private static final Ikon CSS_ICON = FontAwesomeBrands.CSS3;
    private static final Ikon JS_ICON = FontAwesomeBrands.JS_SQUARE;
    private static final URL JAVA_ICON;
    private static final Ikon SASS_ICON = FontAwesomeBrands.SASS;

    static {
        JAVA_ICON = App.class.getClassLoader().getResource("icons/java_file_icon.svg");
    }


    public static SVGIcon iconForFile(File file)
    {
        String extension = null;
        if (file.isDirectory())
            extension = "";
        else
            extension = file.getName().split("\\.")[1];
        switch (extension)
        {
            case "sass":
            case "scss": return new SVGIcon(SASS_ICON);
            case "html":
            case "htm":
            case "xhtml": return new SVGIcon(HTML_ICON);
            case "js": return new SVGIcon(JS_ICON);
            case "java": return SVGIcon.fromResource(JAVA_ICON);
            case "css": return new SVGIcon(CSS_ICON);
            case "": return new SVGIcon(FOLDER_ICON);
            default: return new SVGIcon(DEFAULT_ICON);
        }
    }
}
