package io.orbit.ui.navigator;

import io.orbit.api.event.FileTreeMenuEvent;
import io.orbit.ui.contextmenu.NavigatorContextMenu;
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
public class MUIFileTreeView extends TreeView<File>
{
    private static final String DEFAULT_STYLE_CLASS = "file-tree-view";

    private File root;
    private NavigatorContextMenu menu;
    private TreeItem<File> selectedItem;
    private List<TreeItem<File>> selectedItems = new ArrayList<>();
    private List<TreeItem<File>> branches = new ArrayList<>();

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
        this.getStyleClass().remove("tree-view");
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void registerListeners()
    {
        this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if (this.menu != null)
                this.menu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        });
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.selectedItem = newValue);
        this.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<File>>) __ -> this.selectedItems = this.getSelectionModel().getSelectedItems());
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

        this.menu.setOnCut(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.CUT, this, this.selectedItems)));
        this.menu.setOnCopy(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY, this, this.selectedItems)));
        this.menu.setOnCopyPath(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_PATH, this, this.selectedItems)));
        this.menu.setOnCopyRelativePath(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.COPY_RELATIVE_PATH, this, this.selectedItems)));
        this.menu.setOnPaste(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.PASTE, this, this.selectedItems)));
        this.menu.setOnDelete(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.DELETE, this, this.selectedItems)));
        this.menu.setOnNewFile(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FILE, this, this.selectedItems)));
        this.menu.setOnNewFolder(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_FOLDER, this, this.selectedItems)));
        this.menu.setOnNewProject(() -> this.fireEvent(new FileTreeMenuEvent(FileTreeMenuEvent.NEW_PROJECT, this, this.selectedItems)));
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

    public void addFiles(File... files)
    {
        this.branches.clear();
        populateMap(this.getRoot());
        for (File file : files)
        {
            TreeItem<File> item = new TreeItem<>(file);
            getItemWithFile(file.getParentFile()).ifPresent(parent -> parent.getChildren().add(item));
        }
    }

    public void removeFiles(File... files)
    {
        this.branches.clear();
        populateMap(this.getRoot());
        for (File file : files)
        {
            this.getItemWithFile(file).ifPresent(item -> item.getParent().getChildren().remove(item));
        }
    }

    public void forceRefresh()
    {
        this.mapRootFolder();
    }

    private Optional<TreeItem<File>> getItemWithFile(File file)
    {
        for (TreeItem<File> item : this.branches)
        {
            if (item.getValue().equals(file))
                return Optional.of(item);
        }
        return Optional.empty();
    }

    private void populateMap(TreeItem<File> root)
    {
        this.branches.add(root);
        for (TreeItem<File> child : root.getChildren())
        {
            if (child.getChildren().size() > 0)
                populateMap(child);
            else
                this.branches.add(child);
        }
    }

    private void build()
    {
        this.setCellFactory(view -> new FileCell());
        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
