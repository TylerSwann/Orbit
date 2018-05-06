package io.orbit.controllers.events.menubar;

import javafx.event.Event;

import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by Tyler Swann on Thursday March 15, 2018 at 13:49
 */
public class MenuBarEvent extends Event
{
    public static final EventType<MenuBarEvent> PLAY_CLICK = new EventType<>(Event.ANY, "PLAY_CLICK");
    public static final EventType<MenuBarEvent> STOP_CLICK = new EventType<>(Event.ANY, "STOP_CLICK");

    public MenuBarEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    @Override
    public MenuBarEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarEvent) super.copyFor(newSource, newTarget);
    }
}

