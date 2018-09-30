package io.orbit.ui.menubar;

import io.orbit.api.text.FileType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.util.function.BiConsumer;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 15:15
 */
public interface SystemMenuBar<T>
{
    void setOnViewTerminal(EventHandler<ActionEvent> handler);
    void setOnViewNavigator(EventHandler<ActionEvent> handler);
    void setOnNewFile(EventHandler<ActionEvent> handler);
    void setOnNewFolder(EventHandler<ActionEvent> handler);
    void setOnNewProject(EventHandler<ActionEvent> handler);
    void setOnSave(EventHandler<ActionEvent> handler);
    void setOnSaveAll(EventHandler<ActionEvent> handler);
    void setOnOpenFile(EventHandler<ActionEvent> handler);
    void setOnOpenFolder(EventHandler<ActionEvent> handler);
    void setOnSettings(EventHandler<ActionEvent> handler);
    void setOnUndo(EventHandler<ActionEvent> handler);
    void setOnRedo(EventHandler<ActionEvent> handler);
    void setOnCut(EventHandler<ActionEvent> handler);
    void setOnCopy(EventHandler<ActionEvent> handler);
    void setOnPaste(EventHandler<ActionEvent> handler);
    void setOnFind(EventHandler<ActionEvent> handler);
    void setOnSelectAll(EventHandler<ActionEvent> handler);
    void setOnNewCustomFileType(BiConsumer<ActionEvent, FileType> handler);

    T getFileMenu();
    T getEditMenu();
    T getViewMenu();

    Scene getScene();
}
