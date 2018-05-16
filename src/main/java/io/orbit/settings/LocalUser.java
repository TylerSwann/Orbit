package io.orbit.settings;


import io.orbit.App;

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
            App.setApplicationTheme(userSettings.getThemeFile());
        if (userSettings.getSyntaxThemeFile() != null)
            App.setSyntaxTheme(userSettings.getSyntaxThemeFile());
        if (userSettings.getEditorFont() != null)
            App.setEditorFont(userSettings.getEditorFont());
    }

    private LocalUser() { }
}
