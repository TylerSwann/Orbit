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
package io.orbit.ui.treeview;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class MUITreeViewEvent<T> extends Event
{
    public static final EventType<MUITreeViewEvent> ITEM_EXPANSION_CHANGE = new EventType<>(Event.ANY, "ITEM_EXPANSION_CHANGE");
    public static final EventType<MUITreeViewEvent> ITEM_SELECTED = new EventType<>("TREE_ITEM_SELECTED");
    public static final EventType<MUITreeViewEvent> ITEM_DOUBLE_CLICKED = new EventType<>("TREE_ITEM_DOUBLE_CLICKED");
    public static final EventType<MUITreeViewEvent> CONTEXT_MENU_REQUEST = new EventType<>("TREE_ITEM_CONTEXT_MENU_REQUEST");

    private MUITreeItem<T> target;
    private MouseEvent mouseEvent;

    public MUITreeViewEvent(EventType<? extends Event> eventType, MUITreeItem<T> target)
    {
        super(eventType);
        this.target = target;
    }

    MUITreeViewEvent(EventType<? extends Event> eventType, MouseEvent event, MUITreeItem<T> target)
    {
        super(eventType);
        this.target = target;
        this.mouseEvent = event;
    }

    public MUITreeItem<T> getTarget() { return this.target; }
    public MouseEvent getMouseEvent() { return this.mouseEvent; }
}
