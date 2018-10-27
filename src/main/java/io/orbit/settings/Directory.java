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
package io.orbit.settings;

import io.orbit.App;
import io.orbit.util.JSON;
import javafx.application.Platform;

import javax.swing.JFileChooser;
import java.io.File;
import java.net.URL;

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 17:22
 */
public class Directory
{
    public static final File ORBIT_PROJECTS = new File(String.format("%s\\OrbitProjects", new JFileChooser().getFileSystemView().getDefaultDirectory().getPath()));
    public static final File APPLICATION_ROOT = new File(String.format("%s\\.Orbit", System.getProperty("user.home")));
    public static final File PLUGINS_FOLDER = new File(String.format("%s\\PLUGINS", APPLICATION_ROOT));
    public static final File USER_SETTINGS = new File(String.format("%s\\user_settings.json", APPLICATION_ROOT));
    public static final File THEMES = new File(String.format("%s\\themes", APPLICATION_ROOT));
    public static final File USER_SYNTAX_THEMES = new File(String.format("%s\\themes\\syntax", APPLICATION_ROOT));
    public static final File USER_APP_THEMES = new File(String.format("%s\\themes\\application", APPLICATION_ROOT));
    public static final File USER_FONTS = new File(String.format("%s\\fonts", THEMES));
    public static final File APP_FONTS;
    public static final File DEFAULT_THEME;
    public static final File DEFAULT_SYNTAX_THEME;

    static {
        URL defaultTheme = Directory.class.getClassLoader().getResource("css/Default.css");
        URL defaultSyntaxTheme = Directory.class.getClassLoader().getResource("css/DefaultSyntax.css");
        URL droidSansMono = Directory.class.getClassLoader().getResource("fonts/DroidSansMono.ttf");
        if (defaultTheme == null || defaultSyntaxTheme == null || droidSansMono == null)
            throw new RuntimeException("ERROR: Default.css and DefaultSyntax.css cannot be null!");
        DEFAULT_THEME = new File(defaultTheme.getFile());
        DEFAULT_SYNTAX_THEME = new File(defaultSyntaxTheme.getFile());
        APP_FONTS = new File(droidSansMono.getFile()).getParentFile();
    }

    private static boolean hasInitialized = false;

    /**
     * Checks to see if the required directories have been created
     */
    public static void checkDefaultDirectories()
    {
        if (!hasInitialized)
            App.addOnCloseHandler(() -> Platform.runLater(Directory::saveUserData));
        hasInitialized = true;
        File[] directories = new File[]{
                ORBIT_PROJECTS,
                APPLICATION_ROOT,
                PLUGINS_FOLDER,
                THEMES,
                USER_SYNTAX_THEMES,
                USER_APP_THEMES,
                USER_FONTS
        };
        for (File directory : directories)
        {
            if (!directory.exists() || !directory.isDirectory())
            {
                boolean created = directory.mkdir();
                if (!created)
                    throw new RuntimeException(String.format("Unable to create required directory at %s", directory.getPath()));
            }
        }
        if (!USER_SETTINGS.exists())
            JSON.writeToFile(UserSetting.DEFAULT_SETTINGS, USER_SETTINGS);
    }

    private static void saveUserData()
    {
        UserSetting userSettings;
        if (LocalUser.settings != null)
        {
            userSettings = LocalUser.settings;
            JSON.writeToFile(userSettings, USER_SETTINGS);
        }
    }
    private Directory() { }
}