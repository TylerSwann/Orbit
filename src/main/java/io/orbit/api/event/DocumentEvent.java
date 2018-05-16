package io.orbit.api.event;

import io.orbit.settings.OrbitFile;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.util.Optional;

/**
 * Created by Tyler Swann on Sunday March 11, 2018 at 15:51
 */
public class DocumentEvent extends Event
{
    public static final EventType<DocumentEvent> DOCUMENT_WAS_OPENED = new EventType<>(Event.ANY, "DOCUMENT_WAS_OPENED");
    public static final EventType<DocumentEvent> DOCUMENT_WAS_CLOSED = new EventType<>(Event.ANY, "DOCUMENT_WAS_CLOSED");
    public static final EventType<DocumentEvent> SAVE_FILE = new EventType<>(Event.ANY, "SAVE_FILE");
    public static final EventType<DocumentEvent> SAVE_NON_PROJECT_FILE = new EventType<>(Event.ANY, "SAVE_NON_PROJECT_FILE");
    public static final EventType<DocumentEvent> CLOSE_UNSAVED_FILE = new EventType<>(Event.ANY, "CLOSE_UNSAVED_FILE");
    public static final EventType<DocumentEvent> REFORMAT_DOCUMENT = new EventType<>(Event.ANY, "REFORMAT_DOCUMENT");
    public static final EventType<DocumentEvent> FIND_AND_REPLACE = new EventType<>(Event.ANY, "FIND_AND_REPLACE");

    private Optional<OrbitFile> sourceFile = Optional.empty();
    private Optional<String> contents = Optional.empty();

    public DocumentEvent(OrbitFile sourceFile, String sourceCode, Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.contents = Optional.of(sourceCode);
        this.sourceFile = Optional.of(sourceFile);
    }
    public DocumentEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    public DocumentEvent(OrbitFile sourceFile, Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.sourceFile = Optional.of(sourceFile);
    }

    @Override
    public DocumentEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (DocumentEvent) super.copyFor(newSource, newTarget);
    }

    public Optional<OrbitFile> getSourceFile() {  return this.sourceFile;  }
    public Optional<String> getContents() { return contents; }
}

