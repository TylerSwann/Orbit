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
