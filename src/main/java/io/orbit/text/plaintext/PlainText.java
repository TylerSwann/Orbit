package io.orbit.text.plaintext;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.SVGIcon;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday June 02, 2018 at 15:23
 */
public class PlainText implements LanguageDelegate
{
    private static final FileType TEXT_FILE;

    static {
        TEXT_FILE = new FileType("txt",
                "text",
                "Text File",
                new SVGIcon(FontAwesomeSolid.FILE_ALT),
                Color.rgb(50, 50, 50),
                PlainText::createTextFile);
    }

    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        Map<String, HighlightType> highlightMap = new HashMap<>();
        highlightMap.put("empty", HighlightType.EMPTY);
        return new RegexSyntaxHighlighter(new RegexStylePattern(Pattern.compile("(?<EMPTY>.)"), highlightMap));
    }

    @Override
    public FileType getFileNameExtension() { return TEXT_FILE.clone(); }

    private static void createTextFile(File file)
    {
        try
        {
            boolean success = file.createNewFile();
            if (!success)
                System.out.println(String.format("Couldn't create text file at path %s", file.getPath()));
        }
        catch (IOException ex) {}
    }

    public static FileType TEXT_FILE() { return TEXT_FILE.clone(); }
}
