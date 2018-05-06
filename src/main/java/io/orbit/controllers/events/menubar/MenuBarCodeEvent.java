package io.orbit.controllers.events.menubar;

import io.orbit.text.OrbitEditor;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.util.Optional;

/**
 * Created by Tyler Swann on Thursday April 26, 2018 at 14:24
 */

public class MenuBarCodeEvent extends Event
{
    public static final EventType<MenuBarCodeEvent> REFORMAT_ACTIVE_DOCUMENT = new EventType<>(Event.ANY, "REFORMAT_ACTIVE_DOCUMENT");
    private Optional<OrbitEditor> targetEditor = Optional.empty();

    public Optional<OrbitEditor> getTargetEditor() { return targetEditor; }

    public MenuBarCodeEvent(OrbitEditor source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.targetEditor = Optional.of(source);
    }

    public MenuBarCodeEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target , eventType);
    }
    @Override
    public MenuBarCodeEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarCodeEvent) super.copyFor(newSource, newTarget);
    }
}
