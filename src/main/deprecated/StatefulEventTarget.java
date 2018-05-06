package io.orbit.controllers.events;

import io.orbit.util.TriSet;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * Created by Tyler Swann on Friday March 23, 2018 at 14:59
 */
public interface StatefulEventTarget extends StatelessEventTarget
{
    default <T extends Event> void addStatefulEventHandler(EventType<T> eventType, ObservableValue<?> observableValue, Object requiredValue, EventHandler<? super T> eventHandler)
    {
        this.getHandlers().computeIfAbsent(eventType, key -> new ArrayList<>()).add(eventHandler);
        this.getStatefulHandlers().computeIfAbsent(eventType, key -> new ArrayList<>()).add(new TriSet<>(observableValue, requiredValue, eventHandler));
    }

    default <T extends Event> void removeStatefulEventHandler(EventType<T> eventType, ObservableValue<?> observableValue, Object requiredValue, EventHandler<? super T> eventHandler)
    {
        this.getStatefulHandlers().computeIfPresent(eventType, (type, handler) -> {
            this.getStatefulHandlers().remove(eventHandler);
            return this.getStatefulHandlers().isEmpty() ? null : handler;
        });
    }

    @Override
    default Event dispatchEvent(Event event, EventDispatchChain tail)
    {
        EventType type = event.getEventType();
        while (type != Event.ANY)
        {
            handleEvent(event, this.getHandlers().get(type));
            handleStatefulEvent(event, this.getStatefulHandlers().get(type));
            type = type.getSuperType();
        }
        handleEvent(event, this.getHandlers().get(Event.ANY));
        handleStatefulEvent(event, this.getStatefulHandlers().get(Event.ANY));
        return event;
    }

    @Override
    default void handleEvent(Event event, Collection<EventHandler> handlers)
    {
        if (handlers != null)
            handlers.forEach(handler -> handler.handle(event));
    }

    default <E extends ObservableValue> void handleStatefulEvent(Event event, Collection<TriSet<E, Object, EventHandler>> handlers)
    {
        if (handlers != null)
        {
            for (TriSet<E, Object, EventHandler> handler : handlers)
            {
                if (handler.first.getValue().equals(handler.second))
                    handler.third.handle(event);
            }
        }
    }

    /**
     *
     * @return - Create property called statefulHandlers of type Map<EventType, Collection<TriSet<ObservableValue, Object, EventHandler>>> and
     *           assign it the value of an empty HashMap<>().
     */
    Map<EventType, Collection<TriSet<ObservableValue, Object, EventHandler>>> getStatefulHandlers();
}
