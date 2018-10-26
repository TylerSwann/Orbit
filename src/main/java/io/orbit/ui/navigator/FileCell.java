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
package io.orbit.ui.navigator;

import io.orbit.api.SVGIcon;
import io.orbit.ui.LanguageIcons;
import javafx.scene.control.TreeCell;
import java.io.File;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:10
 */
public class FileCell extends TreeCell<File>
{
    @Override
    protected void updateItem(File file, boolean empty)
    {
        super.updateItem(file, empty);
        if (empty || file == null)
        {
            this.setText(null);
            this.setGraphic(null);
            return;
        }
        SVGIcon icon = LanguageIcons.iconForFile(file);
        icon.setScaleX(0.6);
        icon.setScaleY(0.6);
        this.setGraphic(icon);
        this.setText(file.getName());
    }
}
