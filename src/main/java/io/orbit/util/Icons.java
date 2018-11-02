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
package io.orbit.util;

import io.orbit.App;
import io.orbit.api.SVGIcon;

import java.io.IOException;
import java.net.URL;

/**
 * Created By: Tyler Swann.
 * Date: Sunday, Oct 28, 2018
 * Time: 5:07 PM
 * Website: https://orbiteditor.com
 */
public class Icons
{
    public static SVGIcon METEOR_JS()
    {
        try
        {
            URL url = App.class.getClassLoader().getResource("images/icons/meteor-icon.svg");
            return new SVGIcon(url);
        }
        catch (IOException ex) { ex.printStackTrace(); }
        return null;
    }
}