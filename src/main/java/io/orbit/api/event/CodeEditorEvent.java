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
    public static final EventType<CodeEditorEvent> CUT = new EventType<>(Event.ANY, "CUT");
    public static final EventType<CodeEditorEvent> COPY = new EventType<>(Event.ANY, "COPY");
    public static final EventType<CodeEditorEvent> PASTE = new EventType<>(Event.ANY, "PASTE");
    public static final EventType<CodeEditorEvent> UNDO = new EventType<>(Event.ANY, "UNDO");
    public static final EventType<CodeEditorEvent> REDO = new EventType<>(Event.ANY, "REDO");
    public static final EventType<CodeEditorEvent> SAVE = new EventType<>(Event.ANY, "SAVE_FILE");
    public static final EventType<CodeEditorEvent> SAVE_ALL = new EventType<>(Event.ANY, "SAVE_ALL_FILES");
    public static final EventType<CodeEditorEvent> FIND = new EventType<>(Event.ANY, "FIND");
    public static final EventType<CodeEditorEvent> FIND_AND_REPLACE = new EventType<>(Event.ANY, "FIND_AND_REPLACE");
    public static final EventType<CodeEditorEvent> CLOSE = new EventType<>(Event.ANY, "CLOSE_EDITOR");
    public static final EventType<CodeEditorEvent> FILE_WAS_MODIFIED = new EventType<>(Event.ANY, "FILE_WAS_MODIFIED");
    public static final EventType<CodeEditorEvent> SELECT_ALL = new EventType<>(Event.ANY, "SELECT_ALL");


    private Optional<File> file = Optional.empty();

    public CodeEditorEvent(EventType<? extends Event> eventType, File file)
    {
        super(null, null, eventType);
        this.file = Optional.of(file);
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

    public Optional<File> getFile() { return file; }
}
