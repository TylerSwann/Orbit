package io.orbit.controllers.events.menubar;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by Tyler Swann on Wednesday April 04, 2018 at 11:22
 */
public class MenuBarEditEvent extends Event
{
    public static final EventType<MenuBarEditEvent> UNDO = new EventType<>(Event.ANY, "UNDO_TEXT");
    public static final EventType<MenuBarEditEvent> REDO = new EventType<>(Event.ANY, "REDO_TEXT");
    public static final EventType<MenuBarEditEvent> CUT = new EventType<>(Event.ANY, "CUT_TEXT");
    public static final EventType<MenuBarEditEvent> COPY = new EventType<>(Event.ANY, "COPY_TEXT");
    public static final EventType<MenuBarEditEvent> PASTE = new EventType<>(Event.ANY, "PASTE_TEXT");
    public static final EventType<MenuBarEditEvent> FIND = new EventType<>(Event.ANY, "FIND_TEXT");
    public static final EventType<MenuBarEditEvent> SELECT_ALL = new EventType<>(Event.ANY, "SELECT_ALL_TEXT");

    public MenuBarEditEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target , eventType);
    }
    @Override
    public MenuBarEditEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarEditEvent) super.copyFor(newSource, newTarget);
    }
}
