package io.orbit.api.event;

import io.orbit.settings.OrbitFile;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.util.Optional;

/**
 * Created by Tyler Swann on Sunday March 11, 2018 at 15:51
 */
@Deprecated
public class DocumentEvent extends Event
{
    public static final EventType<DocumentEvent> DOCUMENT_WAS_OPENED = new EventType<>(Event.ANY, "DOCUMENT_WAS_OPENED");
    public static final EventType<DocumentEvent> DOCUMENT_WAS_CLOSED = new EventType<>(Event.ANY, "DOCUMENT_WAS_CLOSED");
    public static final EventType<DocumentEvent> SAVE_ALL = new EventType<>(Event.ANY, "SAVE_ALL_FILES");
    public static final EventType<DocumentEvent> SAVE_FILE = new EventType<>(Event.ANY, "SAVE_FILE");
    public static final EventType<DocumentEvent> SAVE_NON_PROJECT_FILE = new EventType<>(Event.ANY, "SAVE_NON_PROJECT_FILE");
    public static final EventType<DocumentEvent> CLOSE_UNSAVED_FILE = new EventType<>(Event.ANY, "CLOSE_UNSAVED_FILE");
    public static final EventType<DocumentEvent> FIND_AND_REPLACE = new EventType<>(Event.ANY, "FIND_AND_REPLACE");
    public static final EventType<DocumentEvent> FIND = new EventType<>(Event.ANY, "FIND");

    private Optional<OrbitFile> sourceFile = Optional.empty();

    public DocumentEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    public DocumentEvent(EventType<? extends Event> eventType, OrbitFile sourceFile)
    {
        super(null, null, eventType);
        this.sourceFile = Optional.of(sourceFile);
    }

    @Override
    public DocumentEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (DocumentEvent) super.copyFor(newSource, newTarget);
    }

    public Optional<OrbitFile> getSourceFile() {  return this.sourceFile;  }
}

