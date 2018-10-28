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
package io.orbit.ui.navigator;

import io.orbit.api.SVGIcon;
import io.orbit.api.event.FileTreeMenuEvent;
import io.orbit.api.text.FileType;
import io.orbit.plugin.PluginDispatch;
import io.orbit.ui.LanguageIcons;
import io.orbit.ui.contextmenu.NavigatorContextMenu;
import io.orbit.ui.treeview.MUITreeItem;
import io.orbit.ui.treeview.MUITreeView;
import io.orbit.ui.treeview.MUITreeViewEvent;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.*;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:00
 */
public class MUIFileTreeView extends MUITreeView<File>
{
    private static final String DEFAULT_STYLE_CLASS = "file-tree-view";

    private File root;
    private NavigatorContextMenu menu;

    public MUIFileTreeView(File root)
    {
        super(Collections.singletonList(buildTree(root)));
        if (!root.isDirectory())
            throw new RuntimeException("Root file passed into MUIFileTreeView constructor can only be a directory!");
        this.menu = new NavigatorContextMenu();
        menu.setOwner(this);
        this.root = root;
        registerListeners();
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setCellFactory(File::getName);
        this.setCellGraphicFactory(cell -> {
            FileType type = PluginDispatch.fileTypeOf(cell.getValue());
            // TODO - make colors work
            if (type != null)
            {
                SVGIcon icon = type.getIcon().clone();
                icon.setStyle(String.format(
                        "-fx-icon-color: rgb(%d, %d, %d)",
                        (int)(type.getThemeColor().getRed() * 255.0),
                        (int)(type.getThemeColor().getGreen() * 255.0),
                        (int)(type.getThemeColor().getBlue() * 255.0)
                ));
                return icon;
            }
            return LanguageIcons.iconForFile(cell.getValue());
        });
    }

    private void registerListeners()
    {
        this.addEventHandler(MUITreeViewEvent.CONTEXT_MENU_REQUEST, event -> {
            if (this.menu != null)
                this.menu.show(this.getScene().getWindow(), event.getMouseEvent().getScreenX(), event.getMouseEvent().getScreenY());
        });
        this.addEventHandler(MUITreeViewEvent.ITEM_DOUBLE_CLICKED, event -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.ITEM_DOUBLE_CLICK, this, this.getSelectedItems())));
        this.menu.setOnCut(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.CUT, this, this.getSelectedItems())));
        this.menu.setOnCopy(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY, this, this.getSelectedItems())));
        this.menu.setOnCopyPath(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_PATH, this, this.getSelectedItems())));
        this.menu.setOnCopyRelativePath(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_RELATIVE_PATH, this, this.getSelectedItems())));
        this.menu.setOnPaste(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.PASTE, this, this.getSelectedItems())));
        this.menu.setOnDelete(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.DELETE, this, this.getSelectedItems())));
        this.menu.setOnNewFile(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FILE, this, this.getSelectedItems())));
        this.menu.setOnNewFileType(type -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FILE_TYPE, this, type, this.getSelectedItems())));
        this.menu.setOnNewFolder(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FOLDER, this, this.getSelectedItems())));
        this.menu.setOnNewProject(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_PROJECT, this, this.getSelectedItems())));
    }

    private void mapRootFolder()
    {
        this.getBranches().removeAll(this.getBranches());
        this.getBranches().addAll(Collections.singletonList(buildTree(this.root)));
    }

    private static MUITreeItem<File> buildTree(File root)
    {
        MUITreeItem<File> rootItem = new MUITreeItem<>(root);
        rootItem.setExpanded(true);
        mapFolder(rootItem);
        return rootItem;
    }

    private static void mapFolder(MUITreeItem<File> root)
    {
        File folder = root.getValue();
        for (File file : folder.listFiles())
        {
            if (file.isDirectory())
            {
                MUITreeItem<File> subRoot = new MUITreeItem<>(file);
                root.getBranches().add(subRoot);
                mapFolder(subRoot);
            }
            else
            {
                MUITreeItem<File> item = new MUITreeItem<>(file);
                root.getBranches().add(item);
            }
        }
    }

    public void addFiles(File... files)
    {
        for (File file : files)
        {
            MUITreeItem<File> item = new MUITreeItem<>(file);
            getItemWithFile(file.getParentFile()).ifPresent(parent -> parent.getBranches().add(item));
        }
    }

    public void removeFiles(File... files)
    {
        Arrays.stream(files).forEach(file -> {
            this.getItemWithFile(file).ifPresent(this::removeFromTree);
        });
    }

    public void forceRefresh()
    {
        this.mapRootFolder();
    }

    private Optional<MUITreeItem<File>> getItemWithFile(File file)
    {
        for (MUITreeItem<File> item : this.getTree())
        {
            if (item.getValue().equals(file))
                return Optional.of(item);
        }
        return Optional.empty();
    }
}
