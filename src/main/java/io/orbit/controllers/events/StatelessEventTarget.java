package io.orbit.controllers.events;

import javafx.event.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Sunday March 11, 2018 at 16:03
 */
public interface StatelessEventTarget extends EventTarget
{
    default void fireEvent(Event event)
    {
        Event.fireEvent(this, event);
    }

    @Override
    default EventDispatchChain buildEventDispatchChain(EventDispatchChain tail)
    {
        return tail.prepend(this::dispatchEvent);
    }


    default <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler)
    {
        this.getHandlers().computeIfAbsent(eventType, key -> new ArrayList<>()).add(eventHandler);
    }

    default <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler)
    {
        this.getHandlers().computeIfPresent(eventType, (type, handler) -> {
            this.getHandlers().remove(eventHandler);
            return this.getHandlers().isEmpty() ? null : handler;
        });
    }
    default Event dispatchEvent(Event event, EventDispatchChain tail)
    {
        EventType type = event.getEventType();
        while (type != Event.ANY)
        {
            handleEvent(event, this.getHandlers().get(type));
            type = type.getSuperType();
        }
        handleEvent(event, this.getHandlers().get(Event.ANY));
        return event;
    }


    default void handleEvent(Event event, Collection<EventHandler> handlers)
    {
        if (handlers != null)
            handlers.forEach(handler -> handler.handle(event));
    }

    /**
     *
     * @return - Create property called handlers of type Map<EventType, Collection<EventHandler>> and
     *           assign it the value of an empty HashMap<>().
     */
    Map<EventType, Collection<EventHandler>> getHandlers();
}