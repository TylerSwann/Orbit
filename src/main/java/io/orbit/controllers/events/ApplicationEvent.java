package io.orbit.controllers.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;



/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 15:20
 */
public class ApplicationEvent extends Event
{
    public static final EventType<ApplicationEvent> WILL_CLOSE = new EventType<>(Event.ANY, "WILL_CLOSE");
    public static final EventType<ApplicationEvent> WILL_LOAD = new EventType<>(Event.ANY, "WILL_LOAD");

    public ApplicationEvent(EventType<? extends Event> eventType)
    {
        super(null, null, eventType);
    }

    public ApplicationEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    @Override
    public ApplicationEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (ApplicationEvent) super.copyFor(newSource, newTarget);
    }
}
