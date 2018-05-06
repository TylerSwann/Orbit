package io.orbit.api.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Saturday April 07, 2018 at 17:01
 */
public class CodeEditorEvent extends Event
{
    public static final EventType<CodeEditorEvent> COPY = new EventType<>(Event.ANY, "COPY");
    public static final EventType<CodeEditorEvent> CUT = new EventType<>(Event.ANY, "CUT");
    public static final EventType<CodeEditorEvent> PASTE = new EventType<>(Event.ANY, "PASTE");
    public static final EventType<CodeEditorEvent> UNDO = new EventType<>(Event.ANY, "UNDO");
    public static final EventType<CodeEditorEvent> REDO = new EventType<>(Event.ANY, "REDO");
    public static final EventType<CodeEditorEvent> SEGMENT_LETTER_CHANGE = new EventType<>(Event.ANY, "SEGMENT_LETTER_CHANGE");
    public static final EventType<CodeEditorEvent> SEGMENT_CHANGE = new EventType<>(Event.ANY, "SEGMENT_CHANGE");
    public static final EventType<CodeEditorEvent> FILE_WAS_MODIFIED = new EventType<>(Event.ANY, "FILE_WAS_MODIFIED");

    public Optional<String> oldSegment = Optional.empty();
    public Optional<String> newSegment = Optional.empty();
    public Optional<File> modifiedFile = Optional.empty();

    public CodeEditorEvent(EventType<? extends Event> eventType, File modifiedFile)
    {
        super(null, null, eventType);
        this.modifiedFile = Optional.of(modifiedFile);
    }
    public CodeEditorEvent(EventType<? extends Event> eventType, String oldSegment, String newSegment)
    {
        this(eventType);
        this.oldSegment = Optional.of(oldSegment);
        this.newSegment = Optional.of(newSegment);
    }

    public CodeEditorEvent(EventType<? extends Event> eventType)
    {
        super(null, null, eventType);
    }

    public CodeEditorEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    @Override
    public CodeEditorEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (CodeEditorEvent) super.copyFor(newSource, newTarget);
    }
}
