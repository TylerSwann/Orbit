package io.orbit.plugin;

import io.orbit.api.PluginController;
import io.orbit.api.text.FileType;
import io.orbit.webtools.WebToolsController;
import io.orbit.text.plaintext.PlainTextController;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Friday March 09, 2018 at 15:51
 *
 * This class handles loading PLUGINS.
 * PluginDispatch provides plugin-defined ui elements to the Controllers as well
 * as invokes various event methods defined in the PluginController interface
 */
public final class PluginDispatch
{
    private PluginDispatch() { }

    public static final List<PluginController> PLUGINS = new ArrayList<>();
    public static final List<FileType> FILE_TYPES = new ArrayList<>();
    private static boolean hasOpened = false;


    public static void open()
    {
        // TODO - finish
        if (hasOpened)
            throw new RuntimeException("PluginDispatch cannot be opened more than once!!");
        hasOpened = true;

        // Later these will be read from jar files stored in the users .Orbit folder
        PLUGINS.add(new WebToolsController());
        PLUGINS.add(new PlainTextController());
        PLUGINS.forEach(plugin -> FILE_TYPES.addAll(plugin.getFileTypes()));
    }

    public static List<PluginController> controllersForFileType(String extension)
    {
        List<PluginController> validPlugins = new ArrayList<>();
        for (PluginController plugin : PLUGINS)
        {
            List<FileType> types = plugin.getFileTypes();
            for (FileType type : types)
                if (type.getExtension().equals(extension))
                    validPlugins.add(plugin);
        }
        return validPlugins;
    }
}
