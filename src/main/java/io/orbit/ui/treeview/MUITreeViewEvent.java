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
    MouseEvent getMouseEvent() { return this.mouseEvent; }
}
