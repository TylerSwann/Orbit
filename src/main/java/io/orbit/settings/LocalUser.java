package io.orbit.settings;


import io.orbit.Themes;

/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 15:32
 */
public class LocalUser
{
    public static ProjectData project;
    public static UserSetting settings;

    public static void applySettings()
    {
        if (settings == null)
            return;
        if (settings.getThemeFile() != null)
            Themes.setApplicationTheme(settings.getThemeFile());
        if (settings.getSyntaxThemeFile() != null)
            Themes.setSyntaxTheme(settings.getSyntaxThemeFile());
        if (settings.getEditorFont() != null)
            Themes.setEditorFont(settings.getEditorFont());
    }

    private LocalUser() { }
}
