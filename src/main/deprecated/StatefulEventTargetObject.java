package io.orbit.controllers.events;

import io.orbit.util.TriSet;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 15:34
 */
public class StatefulEventTargetObject extends StatelessEventTargetObject implements StatefulEventTarget
{
    private Map<EventType, Collection<TriSet<ObservableValue, Object, EventHandler>>> statefulHandlers = new HashMap<>();

    @Override
    public Map<EventType, Collection<TriSet<ObservableValue, Object, EventHandler>>> getStatefulHandlers() { return statefulHandlers; }
}
