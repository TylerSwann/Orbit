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
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 01, 2018
 * Time: 2:11 PM
 * Website: https://orbiteditor.com
 */
public class HTML5Project implements Project
{
    @Override
    public String getName() { return "HTML 5 Project"; }
    @Override
    public SVGIcon getIcon() { return new SVGIcon(FontAwesomeBrands.HTML5); }

    @Override
    public Pane getProjectCreationPane()
    {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: red;");
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