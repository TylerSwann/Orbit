package io.orbit.controllers.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Saturday April 28, 2018 at 11:45
 */
public class SettingsEvent extends Event
{
    public static final EventType<SettingsEvent> EDIT_SYNTAX_THEME = new EventType<>(Event.ANY, "EDIT_SYNTAX_THEME");
    public static final EventType<SettingsEvent> EDIT_UI_THEME = new EventType<>(Event.ANY, "EDIT_UI_THEME");

    private Optional<File> sourceFile = Optional.empty();

    public SettingsEvent(File sourceFile, Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.sourceFile = Optional.of(sourceFile);
    }
    public SettingsEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.sourceFile = Optional.empty();
    }

    @Override
    public SettingsEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (SettingsEvent) super.copyFor(newSource, newTarget);
    }

    public Optional<File> getSourceFile() { return sourceFile; }
}
