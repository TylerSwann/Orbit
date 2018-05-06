package io.orbit.controllers.events;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.*;

/**
 * Created by Tyler Swann on Friday March 30, 2018 at 14:04
 */
public class EventProperty<T extends Event>
{
    private Map<EventType, Collection<EventHandler>> listeners = new HashMap<>();

    public void fire(Event event)
    {
         Collection<EventHandler> listeners = this.listeners.get(event.getEventType());
         if (listeners == null)
             return;
        listeners.forEach(listener -> {
            if (listener != null)
                listener.handle(event);
        });
    }

    public <E extends EventType<T>> void addEventListener(E eventType, EventHandler<? super T> listener)
    {
        this.listeners.computeIfAbsent(eventType, key -> new ArrayList<>()).add(listener);
    }
}
