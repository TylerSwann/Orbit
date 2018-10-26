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
package io.orbit.ui.tabs;

import com.jfoenix.controls.JFXButton;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITab extends JFXButton
{
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
    private static final String DEFAULT_STYLE_CLASS = "mui-tab";
    private Node content;
    public Node getContent() {  return content;  }
    public void setContent(Node content) {  this.content = content;  }


    public MUITab(String text, Node graphic)
    {
        this(text);
        this.setGraphic(graphic);
    }

    public MUITab(String text)
    {
        this();
        this.setText(text);
    }

    public MUITab(Node graphic)
    {
        this();
        this.setGraphic(graphic);
    }

    public MUITab()
    {
        this.setButtonType(JFXButton.ButtonType.FLAT);
        this.setRipplerFill(Color.rgb(255, 255, 255, 0.3));
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public void setSelected(boolean selected)
    {
        this.pseudoClassStateChanged(SELECTED, selected);
    }
}
