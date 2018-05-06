package io.orbit.util;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday March 31, 2018 at 14:23
 */
public class SyncedObservableList<T>
{
    private final ObservableList<T> list;
    private final List<ObservableList<T>> syncedLists = new ArrayList<>();

    public SyncedObservableList(Collection<? extends T> initialValue)
    {
        this.list = FXCollections.observableArrayList(initialValue);
        this.list.addListener(this::updateSyncedLists);
    }

    public SyncedObservableList()
    {
        this.list = FXCollections.observableArrayList(new ArrayList<>());
        this.list.addListener(this::updateSyncedLists);
    }

    private void updateSyncedLists(Observable observable)
    {
        this.syncedLists.forEach(syncedList -> {
            if (isDifferent(syncedList))
                syncedList.setAll(this.list);
        });
    }

    private boolean isDifferent(ObservableList<T> syncedList)
    {
        if (syncedList.size() != this.list.size())
            return true;
        for (int i = 0; i < this.list.size(); i++)
        {
            T found = syncedList.get(i);
            T required = syncedList.get(i);
            if (!required.equals(found))
                return true;
        }
        return false;
    }

    public void sync(ObservableList<T> otherList)
    {
        this.syncedLists.add(otherList);
        updateSyncedLists(null);
    }

    public void append(T object) { this.list.add(object); }
    public void remove(T object) { this.list.remove(object); }
    public T get(int index) { return this.list.get(index); }
}
