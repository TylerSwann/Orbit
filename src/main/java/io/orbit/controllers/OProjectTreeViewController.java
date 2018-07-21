package io.orbit.controllers;


import io.orbit.App;
import io.orbit.settings.LocalUser;
import io.orbit.settings.OrbitFile;
import io.orbit.settings.ProjectFile;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.api.event.DocumentEvent;
import io.orbit.controllers.events.IOEvent;
import io.orbit.ui.contextmenu.NavigatorContextMenu;
import io.orbit.util.StatelessEventTargetObject;
import io.orbit.controllers.events.menubar.MenuBarEvent;
import io.orbit.ui.LanguageIcons;
import io.orbit.ui.MUITreeItem;
import io.orbit.ui.MUITreeTableView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 15:53
 */
@Deprecated
public class OProjectTreeViewController extends StatelessEventTargetObject
{
    private MUITreeTableView<String> treeView;
    private ObservableList<File> modifiedFiles = FXCollections.observableArrayList();
    private static PseudoClass HIGHLIGHTED = PseudoClass.getPseudoClass("highlighted");
    private ContextMenu branchMenu;
    private File rootFolder;

    public OProjectTreeViewController()
    {
        File root = LocalUser.userSettings.getLastModifiedProject().getProjectRoot();
        if (root != null)
            setRootFolder(root);
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_LOAD, event -> {
            OMenuBarController menuBarController = App.applicationController().getMenuBarController();
            OEditorController editorController = App.applicationController().getEditorController();
            menuBarController.addEventHandler(MenuBarEvent.OPEN_FOLDER, fileEvent -> {
                if (fileEvent.selectedFile.isPresent())
                {
                    File file = fileEvent.selectedFile.get();
                    if (file.isDirectory())
                        this.setRootFolder(file);
                }
            });
            editorController.addEventHandler(CodeEditorEvent.FILE_WAS_MODIFIED, textEvent -> textEvent.modifiedFile.ifPresent(this.modifiedFiles::add));
            menuBarController.addEventHandler(MenuBarEvent.SAVE_ALL, fileEvent -> this.modifiedFiles.clear());
            editorController.addEventHandler(DocumentEvent.SAVE_FILE, saveEvent -> saveEvent.getSourceFile().ifPresent(file -> this.modifiedFiles.remove(file)));
            editorController.addEventHandler(DocumentEvent.CLOSE_UNSAVED_FILE, closeEvent -> closeEvent.getSourceFile().ifPresent(this.modifiedFiles::remove));
        });
    }

    public void setTab(Tab container)
    {
        if (this.treeView == null)
            this.treeView = new MUITreeTableView<>();
        this.treeView.setOnItemDoubleClicked(treeItem -> {
            if (treeItem.getUserData() == null || !(treeItem.getUserData() instanceof ProjectFile)) { return; }
            ProjectFile file = ((ProjectFile)treeItem.getUserData());
            App.applicationController().getTabPaneController().openTab(file);
        });
        AnchorPane.setTopAnchor(this.treeView, 0.0);
        AnchorPane.setBottomAnchor(this.treeView, 0.0);
        AnchorPane.setLeftAnchor(this.treeView, 0.0);
        AnchorPane.setRightAnchor(this.treeView, 0.0);
        container.setContent(this.treeView);
    }

    public void setRootFolder(File folder)
    {
        this.rootFolder = folder;
        if (this.treeView == null)
        {
            this.treeView = new MUITreeTableView<>();
            Platform.runLater(this::build);
        }
        if (this.treeView.getRoot() != null)
            this.treeView.setRoot(null);

        MUITreeItem<String> rootBranch = new MUITreeItem<>(folder.getName());
        rootBranch.setUserData(new ProjectFile(folder));
        this.treeView.setRoot(rootBranch);
        this.mapFolder(folder, rootBranch);
    }

    public void forceRefresh()
    {
        if (this.rootFolder != null)
            this.setRootFolder(this.rootFolder);
    }

    private void build()
    {
        this.treeView.setContextMenu(new NavigatorContextMenu());
        this.treeView.setCellFactory(treeView -> {
            TreeCell<String> cell = new TreeCell<>();
            cell.itemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null)
                    cell.setText("");
                else
                {
                    cell.setText(newVal);
                    MUITreeItem<String> item = (MUITreeItem<String>) cell.getTreeItem();
                    if (item != null && item.getUserData() != null && item.getUserData() instanceof OrbitFile)
                    {
                        OrbitFile file = (OrbitFile) item.getUserData();
                        cell.setGraphic(LanguageIcons.iconForFile(file));
                    }
                }
            });
            this.modifiedFiles.addListener((ListChangeListener<File>) c -> {
                MUITreeItem<String> treeItem = (MUITreeItem<String>) cell.getTreeItem();
                if (treeItem != null && treeItem.getUserData() != null && treeItem.getUserData() instanceof ProjectFile)
                {
                    if (this.modifiedFiles.contains(treeItem.getUserData()))
                        cell.pseudoClassStateChanged(HIGHLIGHTED, true);
                    else
                        cell.pseudoClassStateChanged(HIGHLIGHTED, false);
                }
            });
            return cell;
        });
        this.treeView.setOnItemRightClicked((item, event) -> {
            //new NavigatorContextMenu().show(App.PRIMARY_STAGE.get(), event.getScreenX(), event.getScreenY());
//            if (item != null && item.getUserData() != null && item.getUserData() instanceof ProjectFile)
//            {
//                ProjectFile file = (ProjectFile) item.getUserData();
//                if (this.branchMenu == null)
//                    this.branchMenu = createBranchContextMenu();
//                this.branchMenu.setUserData(file);
//                this.branchMenu.show(this.treeView, event.getScreenX(), event.getScreenY());
//            }
        });
    }

    private void mapFolder(File folder, MUITreeItem<String> branch)
    {
        if (!folder.exists() || !folder.isDirectory())
            return;
        File[] projectFiles = folder.listFiles();
        if (projectFiles == null)
            return;
        if (projectFiles.length == 0)
        {
            MUITreeItem<String> blank = new MUITreeItem<>();
            branch.getChildren().add(blank);
            return;
        }
        branch.setExpanded(true);
        for (File file : projectFiles)
        {
            MUITreeItem<String> childBranch = new MUITreeItem<>(file.getName());
            childBranch.setExpanded(true);
            branch.getChildren().add(childBranch);
            if (file.isDirectory())
            {
                ProjectFile projectFile = new ProjectFile(file);
                childBranch.setUserData(projectFile);
                this.mapFolder(file, childBranch);
            }
            else
            {
                ProjectFile projectFile = new ProjectFile(file);
                childBranch.setUserData(projectFile);
            }
        }
    }


    private ContextMenu createBranchContextMenu()
    {
        Menu newMenu = new Menu("New");
        MenuItem newFile = new MenuItem("File");
        MenuItem newDirectory = new MenuItem("Directory");
        newMenu.getItems().addAll(newFile, newDirectory);

        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem copyPath = new MenuItem("Copy Path");
        MenuItem copyRelativePath = new MenuItem("Copy Relative Path");
        MenuItem paste = new MenuItem("Paste");
        MenuItem delete = new MenuItem("Delete");

        newFile.setOnAction(event -> this.fireMenuEvent(IOEvent.CREATE_FILE));
        newDirectory.setOnAction(event -> this.fireMenuEvent(IOEvent.CREATE_DIRECTORY));
        cut.setOnAction(event -> this.fireMenuEvent(IOEvent.CUT_FILE));
        copy.setOnAction(event -> this.fireMenuEvent(IOEvent.COPY_FILE));
        copyPath.setOnAction(event -> this.fireMenuEvent(IOEvent.COPY_PATH));
        copyRelativePath.setOnAction(event -> this.fireMenuEvent(IOEvent.COPY_RELATIVE_PATH));
        paste.setOnAction(event -> this.fireMenuEvent(IOEvent.PASTE_FILE));
        delete.setOnAction(event -> this.fireMenuEvent(IOEvent.DELETE_FILE));

        ContextMenu menu = new ContextMenu(
                newMenu,
                cut,
                copy,
                copyPath,
                copyRelativePath,
                paste,
                delete
        );
        return menu;
    }

    private void fireMenuEvent(EventType<IOEvent> eventType)
    {
        Object userData = this.branchMenu.getUserData();
        ProjectFile file = userData == null || !(userData instanceof ProjectFile ) ? null : (ProjectFile) userData;
        if (file != null)
            this.fireEvent(new IOEvent(file, this, this, eventType));
        else
        {
            App.applicationController()
                    .getStatusBarController().
                    showSnackBarMessage("ERROR Couldn't perform the requested action. Sorry..", 3000);
        }
    }

    private class StringTreeCell extends TreeCell<String>
    {
        @Override
        protected void updateItem(String item, boolean empty)
        {
            if (item == null || empty)
            {
                this.setText(null);
                this.setGraphic(null);
            }
            else
                this.setText(item);
        }
    }
}