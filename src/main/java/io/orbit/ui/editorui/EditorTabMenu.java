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
package io.orbit.ui.editorui;

import io.orbit.ui.contextmenu.MUIContextMenu;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:29
 */
public class EditorTabMenu extends MUIContextMenu
{
    private Runnable onClose = () -> {};
    private Runnable onCloseOthers = () -> {};
    private Runnable onCloseAll = () -> {};
    public static final String DEFAULT_STYLE_CLASS = "editor-tab-menu";

    public EditorTabMenu()
    {
        super();
        this.build();
    }

    private void build()
    {
        this.getRoot().getStyleClass().add(DEFAULT_STYLE_CLASS);
        MUIMenuItem close = new MUIMenuItem(FontAwesomeSolid.TIMES,"Close");
        MUIMenuItem closeOthers = new MUIMenuItem(FontAwesomeSolid.TIMES_CIRCLE,"Close Others");
        MUIMenuItem closeAll = new MUIMenuItem(FontAwesomeSolid.THUMBTACK,"Close All");
        close.setOnAction( __ -> this.onClose.run());
        closeOthers.setOnAction( __ -> this.onCloseOthers.run());
        closeAll.setOnAction( __ -> this.onCloseAll.run());
        this.getItems().addAll(Arrays.asList(
                close,
                closeOthers,
                closeAll
        ));
    }

    public void setOnClose(Runnable onClose) { this.onClose = onClose; }
    public void setOnCloseOthers(Runnable onCloseOthers) { this.onCloseOthers = onCloseOthers; }
    public void setOnCloseAll(Runnable onCloseAll) { this.onCloseAll = onCloseAll; }
}
