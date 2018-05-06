package io.orbit.controllers.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Sunday April 15, 2018 at 16:19
 */
public class IOEvent extends Event
{
    public static final EventType<IOEvent> CREATE_FILE = new EventType<>(Event.ANY, "CREATE_FILE");
    public static final EventType<IOEvent> CREATE_DIRECTORY = new EventType<>(Event.ANY, "CREATE_DIRECTORY");
    public static final EventType<IOEvent> CUT_FILE = new EventType<>(Event.ANY, "CUT_FILE");
    public static final EventType<IOEvent> COPY_FILE = new EventType<>(Event.ANY, "COPY_FILE");
    public static final EventType<IOEvent> COPY_PATH = new EventType<>(Event.ANY, "COPY_PATH");
    public static final EventType<IOEvent> COPY_RELATIVE_PATH = new EventType<>(Event.ANY, "COPY_RELATIVE_PATH");
    public static final EventType<IOEvent> PASTE_FILE = new EventType<>(Event.ANY, "PASTE_FILE");
    public static final EventType<IOEvent> DELETE_FILE = new EventType<>(Event.ANY, "DELETE_FILE");
    
    private Optional<File> targetFile = Optional.empty();
    
    public IOEvent(File targetFile, Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.targetFile = Optional.of(targetFile);
    }

    @Override
    public IOEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (IOEvent) super.copyFor(newSource, newTarget);
    }

    public Optional<File> getTargetFile() { return targetFile; }
}