package io.orbit.ui.navigator;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Tyler Swann on Friday July 20, 2018 at 09:27
 */
public class FileTreeMenuEvent extends Event
{
    public static final EventType<FileTreeMenuEvent> ITEM_RIGHT_CLICK = new EventType<>(Event.ANY, "ITEM_RIGHT_CLICK");
    public static final EventType<FileTreeMenuEvent> ITEM_CLICK = new EventType<>(Event.ANY, "ITEM_CLICK");
    public static final EventType<FileTreeMenuEvent> ITEM_DOUBLE_CLICK = new EventType<>(Event.ANY, "ITEM_DOUBLE_CLICK");
    public static final EventType<FileTreeMenuEvent> CUT = new EventType<>(Event.ANY, "FILE_CUT");
    public static final EventType<FileTreeMenuEvent> COPY = new EventType<>(Event.ANY, "FILE_COPY_PATH");
    public static final EventType<FileTreeMenuEvent> COPY_PATH = new EventType<>(Event.ANY, "FILE_COPY_RELATIVE_PATH");
    public static final EventType<FileTreeMenuEvent> COPY_RELATIVE_PATH = new EventType<>(Event.ANY, "FILE_COPY");
    public static final EventType<FileTreeMenuEvent> PASTE = new EventType<>(Event.ANY, "FILE_PASTE");
    public static final EventType<FileTreeMenuEvent> DELETE = new EventType<>(Event.ANY, "FILE_DELETE");
    public static final EventType<FileTreeMenuEvent> NEW_FILE = new EventType<>(Event.ANY, "NAV_NEW_FILE");
    public static final EventType<FileTreeMenuEvent> NEW_FOLDER = new EventType<>(Event.ANY, "NAV_NEW_FOLDER");
    public static final EventType<FileTreeMenuEvent> NEW_PROJECT = new EventType<>(Event.ANY, "NAV_NEW_PROJECT");

    private List<TreeItem<File>> selectedItems;

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, TreeItem<File> selectedItem)
    {
        super(null, source, eventType);
        this.selectedItems = Collections.singletonList(selectedItem);
    }

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, List<TreeItem<File>> selectedItems)
    {
        super(null, source, eventType);
        this.selectedItems = selectedItems;
    }

    @Override
    public FileTreeMenuEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (FileTreeMenuEvent) super.copyFor(newSource, newTarget);
    }

    public List<TreeItem<File>> getSelectedItems() { return selectedItems; }
}
