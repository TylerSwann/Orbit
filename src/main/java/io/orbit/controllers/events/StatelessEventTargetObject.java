package io.orbit.controllers.events;

import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 15:33
 */
public class StatelessEventTargetObject implements StatelessEventTarget
{
    private Map<EventType, Collection<EventHandler>> handlers = new HashMap<>();

    @Override
    public Map<EventType, Collection<EventHandler>> getHandlers() { return handlers; }
}
