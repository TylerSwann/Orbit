package io.orbit.controllers.events.menubar;

import io.orbit.settings.OrbitFile;
import io.orbit.text.OrbitEditor;
import javafx.event.Event;

import javafx.event.EventTarget;
import javafx.event.EventType;

import java.util.Optional;

/**
 * Created by Tyler Swann on Thursday March 15, 2018 at 13:49
 */
public class MenuBarEvent extends Event
{
    // Right Part of Menu Bar
    public static final EventType<MenuBarEvent> PLAY = new EventType<>(Event.ANY, "PLAY");
    public static final EventType<MenuBarEvent> STOP = new EventType<>(Event.ANY, "STOP");

    // File Menu
    public static final EventType<MenuBarEvent> NEW_FILE = new EventType<>(Event.ANY, "NEW_FILE");
    public static final EventType<MenuBarEvent> NEW_FOLDER = new EventType<>(Event.ANY, "NEW_FOLDER");
    public static final EventType<MenuBarEvent> NEW_PROJECT = new EventType<>(Event.ANY, "NEW_PROJECT");
    public static final EventType<MenuBarEvent> SAVE = new EventType<>(Event.ANY, "SAVE");
    public static final EventType<MenuBarEvent> SAVE_ALL = new EventType<>(Event.ANY, "SAVE_ALL");
    public static final EventType<MenuBarEvent> OPEN_FILE = new EventType<>(Event.ANY, "OPEN_FILE");
    public static final EventType<MenuBarEvent> OPEN_FOLDER = new EventType<>(Event.ANY, "OPEN_FOLDER");
    public static final EventType<MenuBarEvent> SETTINGS = new EventType<>(Event.ANY, "SETTINGS");

    // Edit menu
    public static final EventType<MenuBarEvent> UNDO = new EventType<>(Event.ANY, "UNDO_TEXT");
    public static final EventType<MenuBarEvent> REDO = new EventType<>(Event.ANY, "REDO_TEXT");
    public static final EventType<MenuBarEvent> CUT = new EventType<>(Event.ANY, "CUT_TEXT");
    public static final EventType<MenuBarEvent> COPY = new EventType<>(Event.ANY, "COPY_TEXT");
    public static final EventType<MenuBarEvent> PASTE = new EventType<>(Event.ANY, "PASTE_TEXT");
    public static final EventType<MenuBarEvent> FIND = new EventType<>(Event.ANY, "FIND_TEXT");

    // View Menu
    public static final EventType<MenuBarEvent> SELECT_ALL = new EventType<>(Event.ANY, "SELECT_ALL_TEXT");
    public static final EventType<MenuBarEvent> VIEW_PLUGINS = new EventType<>(Event.ANY, "VIEW_PLUGINS");
    public static final EventType<MenuBarEvent> VIEW_TERMINAL = new EventType<>(Event.ANY, "VIEW_TERMINAL");
    public static final EventType<MenuBarEvent> VIEW_NAVIGATOR = new EventType<>(Event.ANY, "VIEW_NAVIGATOR");

    // Code Menu
    public static final EventType<MenuBarEvent> REFORMAT_ACTIVE_DOCUMENT = new EventType<>(Event.ANY, "REFORMAT_ACTIVE_DOCUMENT");

    public Optional<OrbitFile> selectedFile = Optional.empty();
    private Optional<OrbitEditor> targetEditor;

    public MenuBarEvent(Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
    }

    public MenuBarEvent(EventType<? extends Event> eventType, OrbitEditor targetEditor)
    {
        super(null, null, eventType);
        this.targetEditor = Optional.of(targetEditor);
    }

    public MenuBarEvent(EventType<? extends Event> eventType, OrbitFile sourceFile)
    {
        super(null, null, eventType);
        this.selectedFile = Optional.of(sourceFile);
    }

    @Override
    public MenuBarEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return  (MenuBarEvent) super.copyFor(newSource, newTarget);
    }

    public Optional<OrbitEditor> getTargetEditor() { return targetEditor; }
}

