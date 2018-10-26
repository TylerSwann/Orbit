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
 */

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
