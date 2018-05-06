package io.orbit.webtools;

import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 14:38
 */
public class WebToolsController implements PluginController
{
    @Override
    public List<String> getFileTypes()
    {
        return Arrays.asList("css", "html");
    }



    @Override
    public LanguageDelegate getLanguageDelegate(File file, String fileExtension)
    {
        if (fileExtension.equals("css"))
            return new CSS3();
        else if (fileExtension.equals("html"))
            return new HTML();
        return null;
    }

    @Override
    public EditorController getEditorController(File file, String fileExtension)
    {
        if (fileExtension.equals("css"))
            return new CSSController();
        else if (fileExtension.equals("html"))
            return new HTMLController();
        return null;
    }
}
