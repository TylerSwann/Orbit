package io.orbit.ui.navigator;

import io.orbit.api.event.FileTreeMenuEvent;
import io.orbit.ui.contextmenu.NavigatorContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:00
 */
public class MUIFileTreeView extends TreeView<File>
{
    private File root;
    private NavigatorContextMenu menu;
    private TreeItem<File> selectedItem;

    public MUIFileTreeView(File root)
    {
        if (!root.isDirectory())
            throw new RuntimeException("Root file passed into MUIFileTreeView constructor can only be a directory!");
        this.menu = new NavigatorContextMenu();
        menu.setOwner(this);
        this.root = root;
        this.build();
        registerListeners();
        this.mapRootFolder();
    }

    public void forceRefresh()
    {
        this.mapRootFolder();
    }

    private void build()
    {
        this.setCellFactory(view -> new FileTreeCell());
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void registerListeners()
    {
        this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if (this.menu != null)
                this.menu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        });
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.selectedItem = newValue);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.selectedItem == null)
                return;
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
                this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.ITEM_DOUBLE_CLICK, this, this.selectedItem));
            else if (event.getButton() == MouseButton.PRIMARY)
                this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.ITEM_CLICK, this, this.selectedItem));
            else if (event.getButton() == MouseButton.SECONDARY)
                this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.ITEM_RIGHT_CLICK, this, this.selectedItem));
        });

        this.menu.setOnCut(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.CUT, this, item))));
        this.menu.setOnCopy(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY, this, item))));
        this.menu.setOnCopyPath(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_PATH, this, item))));
        this.menu.setOnCopyRelativePath(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_RELATIVE_PATH, this, item))));
        this.menu.setOnPaste(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.PASTE, this, item))));
        this.menu.setOnDelete(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.DELETE, this, item))));
        this.menu.setOnNewFile(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FILE, this, item))));
        this.menu.setOnNewFolder(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FOLDER, this, item))));
        this.menu.setOnNewProject(() -> this.getSelectedItem().ifPresent(item -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_PROJECT, this, item))));
    }

    private Optional<TreeItem<File>> getSelectedItem()
    {
        if (this.selectedItem != null)
            return Optional.of(this.selectedItem);
        return Optional.empty();
    }

    private void mapRootFolder()
    {
        if (this.getRoot() != null)
            this.setRoot(null);
        TreeItem<File> rootItem = new TreeItem<>(this.root);
        rootItem.setExpanded(true);
        this.setRoot(rootItem);
        this.mapFolder(rootItem);
    }

    private void mapFolder(TreeItem<File> root)
    {
        File folder = root.getValue();
        for (File file : folder.listFiles())
        {
            if (file.isDirectory())
            {
                TreeItem<File> subRoot = new TreeItem<>(file);
                root.getChildren().add(subRoot);
                mapFolder(subRoot);
            }
            else
            {
                TreeItem<File> item = new TreeItem<>(file);
                root.getChildren().add(item);
            }
        }
    }
}
