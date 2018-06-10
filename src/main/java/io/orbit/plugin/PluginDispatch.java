package io.orbit.plugin;

import io.orbit.api.PluginController;
import io.orbit.webtools.WebToolsController;
import io.orbit.text.plaintext.PlainTextController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Friday March 09, 2018 at 15:51
 *
 * This class handles loading plugins.
 * PluginDispatch provides plugin-defined ui elements to the Controllers as well
 * as invokes various event methods defined in the PluginController interface
 */
public final class PluginDispatch
{
    private PluginDispatch() { }

    public static final List<PluginController> plugins;
    private static boolean hasOpened = false;

    static {
        // Later these will be read from jar files stored in the users .Orbit folder
        plugins = new ArrayList<>();
        plugins.add(new WebToolsController());
        plugins.add(new PlainTextController());
    }

    public static void open()
    {
        // TODO - finish
        if (hasOpened)
            throw new RuntimeException("PluginDispatch cannot be opened more than once!!");
        hasOpened = true;
    }

    public static List<PluginController> controllersForFileType(String fileType)
    {
        List<PluginController> validPlugins = new ArrayList<>();
        for (PluginController plugin : plugins)
            if (plugin.getFileTypes().contains(fileType))
                validPlugins.add(plugin);
        return validPlugins;
    }
}
