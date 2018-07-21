package io.orbit.ui.navigator;

import io.orbit.ui.contextmenu.MUIContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:00
 */
public class MUIFileTreeView extends TreeView<File>
{
    private File root;
    private MUIContextMenu menu;
    private TreeItem<File> selectedItem;

    public MUIFileTreeView(File root)
    {
        if (!root.isDirectory())
            throw new RuntimeException("Root file passed into MUIFileTreeView constructor can only be a directory!");
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
    }

    private void mapRootFolder()
    {
        if (this.getRoot() != null)
            this.setRoot(null);
        TreeItem<File> rootItem = new TreeItem<>(this.root);
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

    public void setContextMenu(MUIContextMenu menu)
    {
        menu.setOwner(this);
        this.menu = menu;
    }
}
