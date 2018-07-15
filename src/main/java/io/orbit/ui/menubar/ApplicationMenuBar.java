package io.orbit.ui.menubar;

import io.orbit.ui.contextmenu.MUIContextMenu;
import io.orbit.ui.contextmenu.MUIMenu;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Arrays;

/**
 * Created by Tyler Swann on Saturday July 14, 2018 at 16:02
 */
public class ApplicationMenuBar extends MUIMenuBar implements SystemMenuBar
{
    private static final String CONTEXT_MENU_STYLE_CLASS = "application-context-menu";

    private EventHandler<ActionEvent> onViewPlugins;
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

    public ApplicationMenuBar()
    {
        super();
        this.getLeftItems().addAll(
                file(),
                edit(),
                view()
        );
    }

    private MUIMenuItem file()
    {
        MUIMenuItem file = new MUIMenuItem("File");
        
        MUIMenu newMenu = new MUIMenu("New");
        MUIMenuItem newFile = new MUIMenuItem(FontAwesomeSolid.FILE_ALT, "File");
        MUIMenuItem newFolder = new MUIMenuItem(FontAwesomeSolid.FOLDER, "Folder");
        MUIMenuItem newProject = new MUIMenuItem(FontAwesomeSolid.CUBES, "Project");
        newMenu.getItems().addAll(Arrays.asList(newFile, newFolder, newProject));

        MUIMenuItem save = new MUIMenuItem(FontAwesomeRegular.SAVE, "Save");
        MUIMenuItem save_all = new MUIMenuItem(FontAwesomeSolid.SAVE,"Save All");
        MUIMenuItem open = new MUIMenuItem(FontAwesomeSolid.FILE_ALT,"Open File");
        MUIMenuItem openFolder = new MUIMenuItem(FontAwesomeSolid.FOLDER,"Open Folder");
        MUIMenuItem settings = new MUIMenuItem(FontAwesomeSolid.WRENCH,"Settings");

        MUIContextMenu menu = new MUIContextMenu(this);
        menu.getItems().addAll(Arrays.asList(
                newMenu,
                save,
                save_all,
                open,
                openFolder,
                settings
        ));
        newFile.setOnAction(this.onNewFile);
        newFolder.setOnAction(this.onNewFolder);
        newProject.setOnAction(this.onNewProject);
        save.setOnAction(this.onSave);
        save_all.setOnAction(this.onSaveAll);
        open.setOnAction(this.onOpenFile);
        openFolder.setOnAction(this.onOpenFolder);
        settings.setOnAction(this.onSettings);

        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        newMenu.getSubmenu().getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
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

        undo.setOnAction(this.onUndo);
        redo.setOnAction(this.onRedo);
        cut.setOnAction(this.onCut);
        copy.setOnAction(this.onCopy);
        paste.setOnAction(this.onPaste);
        find.setOnAction(this.onFind);
        select_all.setOnAction(this.onSelectAll);

        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        edit.setMUIContextMenu(menu);
        return edit;
    }


    private MUIMenuItem view()
    {
        MUIMenuItem view = new MUIMenuItem("View");

        MUIMenuItem plugins = new MUIMenuItem(FontAwesomeSolid.PLUG,"Plugins");
        MUIMenuItem terminal = new MUIMenuItem(FontAwesomeSolid.CHECK,"Terminal");
        MUIMenuItem navigator = new MUIMenuItem(FontAwesomeSolid.CHECK,"Navigator");
        terminal.setContentDisplay(ContentDisplay.RIGHT);
        navigator.setContentDisplay(ContentDisplay.RIGHT);

        plugins.setOnAction(this.onViewPlugins);
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

        MUIContextMenu menu = new MUIContextMenu(this);
        menu.getItems().addAll(Arrays.asList(
                plugins,
                terminal,
                navigator
        ));
        menu.getRoot().getStyleClass().add(CONTEXT_MENU_STYLE_CLASS);
        view.setMUIContextMenu(menu);
        return view;
    }

    @Override
    public void setOnViewPlugins(EventHandler<ActionEvent> handler) { this.onViewPlugins = handler; }
    @Override
    public void setOnViewTerminal(EventHandler<ActionEvent> handler) { this.onViewTerminal = handler; }
    @Override
    public void setOnViewNavigator(EventHandler<ActionEvent> handler) { this.onViewNavigator = handler; }
    @Override
    public void setOnNewFile(EventHandler<ActionEvent> handler) { this.onNewFile = handler; }
    @Override
    public void setOnNewFolder(EventHandler<ActionEvent> handler) { this.onNewFolder = handler; }
    @Override
    public void setOnNewProject(EventHandler<ActionEvent> handler) { this.onNewProject = handler; }
    @Override
    public void setOnSave(EventHandler<ActionEvent> handler) { this.onSave = handler; }
    @Override
    public void setOnSaveAll(EventHandler<ActionEvent> handler) { this.onSaveAll = handler; }
    @Override
    public void setOnOpenFile(EventHandler<ActionEvent> handler) { this.onOpenFile = handler; }
    @Override
    public void setOnOpenFolder(EventHandler<ActionEvent> handler) { this.onOpenFolder = handler; }
    @Override
    public void setOnSettings(EventHandler<ActionEvent> handler) { this.onSettings = handler; }
    @Override
    public void setOnUndo(EventHandler<ActionEvent> handler) { this.onUndo = handler; }
    @Override
    public void setOnRedo(EventHandler<ActionEvent> handler) { this.onRedo = handler; }
    @Override
    public void setOnCut(EventHandler<ActionEvent> handler) { this.onCut = handler; }
    @Override
    public void setOnCopy(EventHandler<ActionEvent> handler) { this.onCopy = handler; }
    @Override
    public void setOnPaste(EventHandler<ActionEvent> handler) { this.onPaste = handler; }
    @Override
    public void setOnFind(EventHandler<ActionEvent> handler) { this.onFind = handler; }
    @Override
    public void setOnSelectAll(EventHandler<ActionEvent> handler) { this.onSelectAll = handler; }
}
