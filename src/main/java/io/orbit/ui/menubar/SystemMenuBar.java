/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
    void setOnVisitHomePage(EventHandler<ActionEvent> handler);
    void setOnViewLicense(EventHandler<ActionEvent> handler);
    void setOnAbout(EventHandler<ActionEvent> handler);

    T getFileMenu();
    T getEditMenu();
    T getViewMenu();

    Scene getScene();
}
