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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.util.function.BiConsumer;

public class OSXMenuBar extends MenuBar implements SystemMenuBar<Menu>
{
    private EventHandler<ActionEvent> onViewTerminal;
    private EventHandler<ActionEvent> onViewNavigator;
    private EventHandler<ActionEvent> onNewFile;
    private EventHandler<ActionEvent> onNewFolder;
    private EventHandler<ActionEvent> onNewProject;
    private EventHandler<ActionEvent> onSave;
    private EventHandler<ActionEvent> onSaveAll;
    private EventHandler<ActionEvent> onOpenFile;
    private EventHandler<ActionEvent> onOpenFolder;
    private EventHandler<ActionEvent> onSettings;
    private EventHandler<ActionEvent> onUndo;
    private EventHandler<ActionEvent> onRedo;
    private EventHandler<ActionEvent> onCut;
    private EventHandler<ActionEvent> onCopy;
    private EventHandler<ActionEvent> onPaste;
    private EventHandler<ActionEvent> onFind;
    private EventHandler<ActionEvent> onSelectAll;
    private BiConsumer<ActionEvent, FileType> onNewCustomFileType;
    private EventHandler<ActionEvent> onVisitHomepage;
    private EventHandler<ActionEvent> onViewLicense;
    private EventHandler<ActionEvent> onAbout;

    private Menu file;
    private Menu edit;
    private Menu view;
    private Menu help;

    public OSXMenuBar()
    {

    }

    @Override public Menu getFileMenu() { return this.file; }
    @Override public Menu getEditMenu() { return this.edit; }
    @Override public Menu getViewMenu() { return this.view; }

    @Override public void setOnViewTerminal(EventHandler<ActionEvent> handler) { this.onViewTerminal = handler; }
    @Override public void setOnViewNavigator(EventHandler<ActionEvent> handler) { this.onViewNavigator = handler; }
    @Override public void setOnNewFile(EventHandler<ActionEvent> handler) { this.onNewFile = handler; }
    @Override public void setOnNewFolder(EventHandler<ActionEvent> handler) { this.onNewFolder = handler; }
    @Override public void setOnNewProject(EventHandler<ActionEvent> handler) { this.onNewProject = handler; }
    @Override public void setOnSave(EventHandler<ActionEvent> handler) { this.onSave = handler; }
    @Override public void setOnSaveAll(EventHandler<ActionEvent> handler) { this.onSaveAll = handler; }
    @Override public void setOnOpenFile(EventHandler<ActionEvent> handler) { this.onOpenFile = handler; }
    @Override public void setOnOpenFolder(EventHandler<ActionEvent> handler) { this.onOpenFolder = handler; }
    @Override public void setOnSettings(EventHandler<ActionEvent> handler) { this.onSettings = handler; }
    @Override public void setOnUndo(EventHandler<ActionEvent> handler) { this.onUndo = handler; }
    @Override public void setOnRedo(EventHandler<ActionEvent> handler) { this.onRedo = handler; }
    @Override public void setOnCut(EventHandler<ActionEvent> handler) { this.onCut = handler; }
    @Override public void setOnCopy(EventHandler<ActionEvent> handler) { this.onCopy = handler; }
    @Override public void setOnPaste(EventHandler<ActionEvent> handler) { this.onPaste = handler; }
    @Override public void setOnFind(EventHandler<ActionEvent> handler) { this.onFind = handler; }
    @Override public void setOnSelectAll(EventHandler<ActionEvent> handler) { this.onSelectAll = handler; }
    @Override public void setOnNewCustomFileType(BiConsumer<ActionEvent, FileType> handler) { this.onNewCustomFileType = handler; }
    @Override public void setOnVisitHomePage(EventHandler<ActionEvent> handler) { this.onVisitHomepage = handler; }
    @Override public void setOnViewLicense(EventHandler<ActionEvent> handler) { this.onViewLicense = handler; }
    @Override public void setOnAbout(EventHandler<ActionEvent> handler) { this.onAbout = handler; }
}
