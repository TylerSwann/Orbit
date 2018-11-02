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
package io.orbit.webtools.projects;

import io.orbit.api.SVGIcon;
import io.orbit.api.language.Project;
import io.orbit.util.Icons;
import javafx.scene.layout.Pane;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 01, 2018
 * Time: 2:12 PM
 * Website: https://orbiteditor.com
 */
public class MeteorProject implements Project
{
    @Override
    public String getName() { return "Meteor App"; }
    @Override
    public SVGIcon getIcon() { return Icons.METEOR_JS(); }

    @Override
    public Pane getProjectCreationPane()
    {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: blue;");
        return pane;
    }

    @Override
    public boolean validate()
    {
        return false;
    }

    @Override
    public void create()
    {

    }
}