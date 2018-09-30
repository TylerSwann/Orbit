package io.orbit.ui.menubar;

import io.orbit.api.text.FileType;
import io.orbit.plugin.PluginDispatch;
import io.orbit.settings.LocalUser;
import io.orbit.ui.colorpicker.MUIPopupColorPicker;
import io.orbit.ui.contextmenu.MUIContextMenu;
import io.orbit.ui.contextmenu.MUIMenu;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by Tyler Swann on Saturday July 14, 2018 at 16:02
 */
public class ApplicationMenuBar extends MUIMenuBar implements SystemMenuBar<MUIMenuItem>
{
    private static final String CONTEXT_MENU_STYLE_CLASS = "application-context-menu";

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

    private MUIMenuItem file;
    private MUIMenuItem edit;
    private MUIMenuItem view;

    public ApplicationMenuBar()
    {
        super();
        this.file = file();
        this.edit = edit();
        this.view = view();
        this.getLeftItems().addAll(this.file, this.edit, this.view);
        this.getRightItems().addAll(iconBar());
        this.right.getStyleClass().add("mui-icon-bar");
    }

    private MUIMenuItem file()
    {
        MUIMenuItem file = new MUIMenuItem("File");
        
        MUIMenu newMenu = new MUIMenu("New");
        MUIMenuItem newFile = new MUIMenuItem(FontAwesomeSolid.FILE_ALT, "File");
        MUIMenuItem newFolder = new MUIMenuItem(FontAwesomeSolid.FOLDER, "Folder");
        MUIMenuItem newProject = new MUIMenuItem(FontAwesomeSolid.CUBES, "Project");
        MUIMenuItem settings = new MUIMenuItem(FontAwesomeSolid.COG, "Settings");
        newMenu.getItems().add(newFile);
        PluginDispatch.FILE_TYPES.forEach(type -> {
            MUIMenuItem fileTypeItem = new MUIMenuItem(type.getIcon(), type.getDisplayText());
            fileTypeItem.setOnAction(event -> this.onNewCustomFileType.accept(event, type));
            newMenu.getItems().add(fileTypeItem);
        });
        newMenu.getItems().addAll(Arrays.asList(newFolder, newProject));

        MUIMenuItem save = new MUIMenuItem(FontAwesomeRegular.SAVE, "Save");
        MUIMenuItem save_all = new MUIMenuItem(FontAwesomeSolid.SAVE,"Save All");
        MUIMenuItem open = new MUIMenuItem(FontAwesomeSolid.FILE_ALT,"Open File");
        MUIMenuItem openFolder = new MUIMenuItem(FontAwesomeSolid.FOLDER,"Open Folder");
        MUIContextMenu menu = new MUIContextMenu(this);
        menu.getItems().addAll(Arrays.asList(
                newMenu,
                save,
                save_all,
                open,
                openFolder,
                settings
        ));
        newFile.setOnAction(event -> this.onNewFile.handle(event));
        newFolder.setOnAction(event -> this.onNewFolder.handle(event));
        newProject.setOnAction(event -> this.onNewProject.handle(event));
        save.setOnAction(event -> this.onSave.handle(event));
        save_all.setOnAction(event -> this.onSaveAll.handle(event));
        open.setOnAction(event -> this.onOpenFile.handle(event));
        openFolder.setOnAction(event -> this.onOpenFolder.handle(event));
        settings.setOnAction(event -> this.onSettings.handle(event));

        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        newMenu.getSubmenu().getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        menu.getRoot().getStyleClass().add("file");
        file.setMUIContextMenu(menu);
        return file;
    }

    private MUIMenuItem edit()
    {
        MUIMenuItem edit = new MUIMenuItem("Edit");

        MUIMenuItem undo = new MUIMenuItem(FontAwesomeSolid.UNDO,"Undo");
        MUIMenuItem redo = new MUIMenuItem(FontAwesomeSolid.REDO,"Redo");
        MUIMenuItem cut = new MUIMenuItem(FontAwesomeSolid.CUT,"Cut");
        MUIMenuItem copy = new MUIMenuItem(FontAwesomeSolid.COPY,"Copy");
        MUIMenuItem paste = new MUIMenuItem(FontAwesomeSolid.PASTE,"Paste");
        MUIMenuItem find = new MUIMenuItem(FontAwesomeSolid.SEARCH,"Find");
        MUIMenuItem select_all = new MUIMenuItem(FontAwesomeSolid.CLIPBOARD,"Select All");

        MUIContextMenu menu = new MUIContextMenu(this);
        menu.getItems().addAll(Arrays.asList(
                undo,
                redo,
                cut,
                copy,
                paste,
                find,
                select_all
        ));

        undo.setOnAction(event -> this.onUndo.handle(event));
        redo.setOnAction(event -> this.onRedo.handle(event));
        cut.setOnAction(event -> this.onCut.handle(event));
        copy.setOnAction(event -> this.onCopy.handle(event));
        paste.setOnAction(event -> this.onPaste.handle(event));
        find.setOnAction(event -> this.onFind.handle(event));
        select_all.setOnAction(event -> this.onSelectAll.handle(event));

        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        menu.getRoot().getStyleClass().add("edit");
        edit.setMUIContextMenu(menu);
        return edit;
    }


    private MUIMenuItem view()
    {
        MUIMenuItem view = new MUIMenuItem("View");

        MUIMenuItem terminal = new MUIMenuItem(FontAwesomeSolid.CHECK,"Terminal");
        MUIMenuItem navigator = new MUIMenuItem(FontAwesomeSolid.CHECK,"Navigator");
        MUIMenuItem colorPicker = new MUIMenuItem(FontAwesomeSolid.PAINT_BRUSH, "Color Picker");

        terminal.setContentDisplay(ContentDisplay.RIGHT);
        navigator.setContentDisplay(ContentDisplay.RIGHT);

        colorPicker.setOnAction(__ -> {
            MUIPopupColorPicker popupColorPicker = new MUIPopupColorPicker();
            popupColorPicker.show(this.getScene().getWindow());
        });

        terminal.setOnAction(event -> {
            if (terminal.getGraphic().getOpacity() <= 0.0)
                terminal.getGraphic().setOpacity(1.0);
            else
                terminal.getGraphic().setOpacity(0.0);
            this.onViewTerminal.handle(event);
        });
        navigator.setOnAction(event -> {
            if (navigator.getGraphic().getOpacity() <= 0.0)
                navigator.getGraphic().setOpacity(1.0);
            else
                navigator.getGraphic().setOpacity(0.0);
            this.onViewNavigator.handle(event);
        });
        terminal.getGraphic().setOpacity((LocalUser.settings.isTerminalClosed() ? 0.0 : 1.0));
        navigator.getGraphic().setOpacity((LocalUser.settings.isNavigatorClosed() ? 0.0 : 1.0));

        MUIContextMenu menu = new MUIContextMenu(this);
        menu.getItems().addAll(Arrays.asList(
                terminal,
                navigator,
                colorPicker
        ));
        terminal.getStyleClass().add("check-item");
        navigator.getStyleClass().add("check-item");
        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        menu.getRoot().getStyleClass().add("view");
        view.setMUIContextMenu(menu);
        return view;
    }

    private List<MUIMenuItem> iconBar()
    {
        MUIMenuItem play = new MUIMenuItem(FontAwesomeSolid.PLAY_CIRCLE, "");
        MUIMenuItem stop = new MUIMenuItem(FontAwesomeSolid.STOP_CIRCLE, "");
        MUIMenuItem search = new MUIMenuItem(FontAwesomeSolid.SEARCH, "");
        return Arrays.asList(play, stop, search);
    }

    @Override public MUIMenuItem getFileMenu() { return this.file; }
    @Override public MUIMenuItem getEditMenu() { return this.edit; }
    @Override public MUIMenuItem getViewMenu() { return this.view; }

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
}
