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

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:04
 */
public class MUIIconButton extends JFXButton
{
    private static final String DEFAULT_STYLE_CLASS = "mui-icon-button";
    private static final String ICON_STYLE_CLASS = "mui-icon";

    private FontIcon icon;

    public MUIIconButton(Ikon icon)
    {
        this.icon = new FontIcon(icon);
        this.icon.getStyleClass().add(ICON_STYLE_CLASS);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setGraphic(this.icon);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
}
