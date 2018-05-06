package io.orbit.controllers.events.menubar;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by Tyler Swann on Wednesday April 04, 2018 at 11:22
 */
public class MenuBarViewEvent extends Event
{
    public static final EventType<MenuBarViewEvent> PLUGINS = new EventType<>(Event.ANY, "PLUGINS");
    public static final EventType<MenuBarViewEvent> TERMINAL = new EventType<>(Event.ANY, "TERMINAL");
    @Deprecated
    public static final EventType<MenuBarViewEvent> PROBLEMS = new EventType<>(Event.ANY, "PROBLEMS");
    public static final EventType<MenuBarViewEvent> NAVIGATOR = new EventType<>(Event.ANY, "NAVIGATOR");

    public MenuBarViewEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target , eventType);
    }
    @Override
    public MenuBarViewEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarViewEvent) super.copyFor(newSource, newTarget);
    }
}
