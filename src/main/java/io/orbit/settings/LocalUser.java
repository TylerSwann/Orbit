package io.orbit.settings;


import io.orbit.App;
import io.orbit.Themes;

/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 15:32
 */
public class LocalUser
{
    public static ProjectData project;
    public static UserSetting userSettings;

    public static void applySettings()
    {
        if (userSettings == null)
            return;
        if (userSettings.getThemeFile() != null)
            Themes.setApplicationTheme(userSettings.getThemeFile());
        if (userSettings.getSyntaxThemeFile() != null)
            Themes.setSyntaxTheme(userSettings.getSyntaxThemeFile());
        if (userSettings.getEditorFont() != null)
            Themes.setEditorFont(userSettings.getEditorFont());
    }

    private LocalUser() { }
}
