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
package io.orbit.webtools;

import io.orbit.api.Project;
import io.orbit.api.SVGIcon;
import javafx.scene.layout.Pane;
import java.io.File;
import java.net.URL;

/**
 * Created by Tyler Swann on Friday May 04, 2018 at 14:03
 */
public class WebProject implements Project
{
    @Override
    public String getName()
    {
        return "Web Project";
    }

    @Override
    public SVGIcon getIcon()
    {
        try
        {
            URL url = getClass().getClassLoader().getResource("icons/web_project_icon.svg");
            if (url != null)
                return new SVGIcon(url);
        }
        catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    @Override
    public boolean start(String projectName, File rootDir)
    {
        return true;
    }
    @Override
    public Pane[] getProjectCreationSteps()
    {
        Pane one = new Pane();
        Pane two = new Pane();
        Pane three = new Pane();
        Pane four = new Pane();
        Pane five = new Pane();
        one.setStyle("-fx-background-color: red;");
        two.setStyle("-fx-background-color: blue;");
        three.setStyle("-fx-background-color: green;");
        four.setStyle("-fx-background-color: cornflowerblue;");
        five.setStyle("-fx-background-color: yellow;");
        return new Pane[]{ one, two, three, four, five };
    }
}
