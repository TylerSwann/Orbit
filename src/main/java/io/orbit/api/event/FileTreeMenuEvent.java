package io.orbit.api.event;

import io.orbit.api.text.FileType;
import io.orbit.ui.navigator.MUIFileTreeView;
import io.orbit.ui.treeview.MUITreeItem;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Tyler Swann on Friday July 20, 2018 at 09:27
 */
public class FileTreeMenuEvent extends Event
{
    public static final EventType<FileTreeMenuEvent> ITEM_DOUBLE_CLICK = new EventType<>(Event.ANY, "ITEM_DOUBLE_CLICK");
    public static final EventType<FileTreeMenuEvent> CUT = new EventType<>(Event.ANY, "FILE_CUT");
    public static final EventType<FileTreeMenuEvent> COPY = new EventType<>(Event.ANY, "FILE_COPY_PATH");
    public static final EventType<FileTreeMenuEvent> COPY_PATH = new EventType<>(Event.ANY, "FILE_COPY_RELATIVE_PATH");
    public static final EventType<FileTreeMenuEvent> COPY_RELATIVE_PATH = new EventType<>(Event.ANY, "FILE_COPY");
    public static final EventType<FileTreeMenuEvent> PASTE = new EventType<>(Event.ANY, "FILE_PASTE");
    public static final EventType<FileTreeMenuEvent> DELETE = new EventType<>(Event.ANY, "FILE_DELETE");
    public static final EventType<FileTreeMenuEvent> NEW_FILE = new EventType<>(Event.ANY, "NAV_NEW_FILE");
    public static final EventType<FileTreeMenuEvent> NEW_FILE_TYPE = new EventType<>(Event.ANY, "NAV_NEW_FILE_TYPE");
    public static final EventType<FileTreeMenuEvent> NEW_FOLDER = new EventType<>(Event.ANY, "NAV_NEW_FOLDER");
    public static final EventType<FileTreeMenuEvent> NEW_PROJECT = new EventType<>(Event.ANY, "NAV_NEW_PROJECT");

    private List<MUITreeItem<File>> selectedItems;
    private FileType fileType;

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, MUITreeItem<File> selectedItem)
    {
        super(null, source, eventType);
        this.selectedItems = Collections.singletonList(selectedItem);
    }

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, List<MUITreeItem<File>> selectedItems)
    {
        super(null, source, eventType);
        this.selectedItems = selectedItems;
    }

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, FileType fileType, List<MUITreeItem<File>> selectedItems)
    {
        super(null, source, eventType);
        this.selectedItems = selectedItems;
        this.fileType = fileType;
    }

    @Override
    public FileTreeMenuEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (FileTreeMenuEvent) super.copyFor(newSource, newTarget);
    }

    public FileType getFileType() { return this.fileType; }
    public List<MUITreeItem<File>> getSelectedItems() { return selectedItems; }
    public List<File> getSelectedFiles()
    {
        List<MUITreeItem<File>> items = this.getSelectedItems();
        List<File> files = new ArrayList<>();
        for (MUITreeItem<File> item : items)
            files.add(item.getValue());
        return files;
    }
}
