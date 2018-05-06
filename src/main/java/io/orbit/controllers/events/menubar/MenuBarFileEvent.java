package io.orbit.controllers.events.menubar;

import io.orbit.OrbitFile;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Wednesday April 04, 2018 at 11:22
 */
public class MenuBarFileEvent extends Event
{
    public static final EventType<MenuBarFileEvent> NEW_PROJECT = new EventType<>(Event.ANY, "NEW_PROJECT");
    public static final EventType<MenuBarFileEvent> NEW_FILE = new EventType<>(Event.ANY, "NEW_FILE");
    public static final EventType<MenuBarFileEvent> SAVE = new EventType<>(Event.ANY, "SAVE");
    public static final EventType<MenuBarFileEvent> SAVE_ALL = new EventType<>(Event.ANY, "SAVE_ALL");
    public static final EventType<MenuBarFileEvent> OPEN = new EventType<>(Event.ANY, "OPEN");
    public static final EventType<MenuBarFileEvent> SETTINGS = new EventType<>(Event.ANY, "SETTINGS");
    public static final EventType<MenuBarFileEvent> OPEN_FOLDER = new EventType<>(Event.ANY, "OPEN_FOLDER");

    public Optional<OrbitFile> selectedFile = Optional.empty();

    public MenuBarFileEvent(EventType<? extends Event> eventType, OrbitFile selectedFile)
    {
        this(null, null, eventType);
        this.selectedFile = Optional.of(selectedFile);
    }
    public MenuBarFileEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target , eventType);
    }
    @Override
    public MenuBarFileEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarFileEvent) super.copyFor(newSource, newTarget);
    }
}

