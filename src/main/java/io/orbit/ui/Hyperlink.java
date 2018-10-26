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
package io.orbit.ui;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 15:30
 */
public interface Hyperlink
{
    URI getURI();
    void setURI(URI uri);
    default void trigger()
    {
        Desktop desktop = Desktop.getDesktop();
        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE))
        {
            System.out.println("ERROR: java.awt.Desktop is not available on this system");
            return;
        }
        try
        {
            desktop.browse(this.getURI());
        }
        catch (IOException ex)
        {
            System.out.println("ERROR triggering hyperlink!");
            ex.printStackTrace();
        }
    }
}
