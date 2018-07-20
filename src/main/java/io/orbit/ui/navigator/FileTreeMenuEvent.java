package io.orbit.ui.navigator;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;
import java.io.File;


/**
 * Created by Tyler Swann on Friday July 20, 2018 at 09:27
 */
public class FileTreeMenuEvent extends Event
{
    public static final EventType<FileTreeMenuEvent> ITEM_RIGHT_CLICK = new EventType<>(Event.ANY, "ITEM_RIGHT_CLICK");
    public static final EventType<FileTreeMenuEvent> ITEM_CLICK = new EventType<>(Event.ANY, "ITEM_CLICK");
    public static final EventType<FileTreeMenuEvent> ITEM_DOUBLE_CLICK = new EventType<>(Event.ANY, "ITEM_DOUBLE_CLICK");

    private TreeItem<File> selectedItem;

    public FileTreeMenuEvent(EventType<FileTreeMenuEvent> eventType, MUIFileTreeView source, TreeItem<File> selectedItem)
    {
        super(null, source, eventType);
        this.selectedItem = selectedItem;
    }

    @Override
    public FileTreeMenuEvent copyFor(Object newSource, EventTarget newTarget)
    {
        return (FileTreeMenuEvent) super.copyFor(newSource, newTarget);
    }

    public TreeItem<File> getSelectedItem() { return selectedItem; }
}
