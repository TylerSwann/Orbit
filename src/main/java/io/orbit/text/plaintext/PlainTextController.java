package io.orbit.text.plaintext;

import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;
import io.orbit.api.SVGIcon;
import io.orbit.api.text.FileType;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday June 02, 2018 at 15:27
 */
public class PlainTextController implements PluginController
{
    @Override
    public LanguageDelegate getLanguageDelegate(File file, String fileExtension)
    {
        if (fileExtension.equals("txt"))
            return new PlainText();
        else
            return null;
    }

    @Override
    public List<FileType> getFileTypes()
    {
        return Collections.singletonList(new FileType("txt", "text", new SVGIcon(FontAwesomeSolid.FILE_ALT)));
    }

    @Override
    public EditorController getEditorController(File file, String fileExtension)
    {
        if (fileExtension.equals("txt"))
            return new PlainTextEditorController();
        else
            return null;
    }
}
