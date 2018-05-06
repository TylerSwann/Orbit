package io.orbit.controllers;

import io.orbit.App;
import io.orbit.LocalUser;
import io.orbit.ProjectFile;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.StatelessEventTarget;
import io.orbit.controllers.events.menubar.*;
import io.orbit.ui.MUIMenuBar;
import io.orbit.ui.MUIMenuButton;
import io.orbit.ui.MUIMenuItem;
import io.orbit.ui.MUISubMenu;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 13:01
 */
public class OMenuBarController implements StatelessEventTarget
{
    private MUIMenuBar menuBar;
    private final Map<EventType, Collection<EventHandler>> handlers = new HashMap<>();

    public OMenuBarController(AnchorPane container)
    {

        MUIMenuButton view = view();
        MUIMenuButton file = file();
        MUIMenuButton edit = edit();
        MUIMenuButton code = code();
        MUIMenuButton[] rightMenu = rightMenu();

        this.menuBar = new MUIMenuBar(new MUIMenuButton[]{
                file,
                edit,
                code,
                view
        }, rightMenu);

        AnchorPane.setTopAnchor(this.menuBar, 0.0);
        AnchorPane.setLeftAnchor(this.menuBar, 0.0);
        AnchorPane.setRightAnchor(this.menuBar, 0.0);
        //container.getChildren().add(this.menuBar);
    }



    private MUISubMenu getViewMenu()
    {
        MUIMenuItem plugins = new MUIMenuItem("Plugins");
        return new MUISubMenu("View", plugins);
    }

    private MUIMenuButton file()
    {
        MUIMenuButton file = new MUIMenuButton("File");

        MUIMenuItem save = new MUIMenuItem("Save");
        MUIMenuItem save_all = new MUIMenuItem("Save All");
        MUIMenuItem open = new MUIMenuItem("Open");
        MUIMenuItem openFolder = new MUIMenuItem("Open Folder");
        MUIMenuItem settings = new MUIMenuItem("Settings");
        save.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.SAVE)));
        save_all.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.SAVE_ALL)));
        settings.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.SETTINGS)));
        open.setOnAction(event -> this.showFileChooserDialog());
        openFolder.setOnAction(event -> this.showFolderChooseDialog());


        Platform.runLater(() -> App.applicationController().getSettingsController().bind(settings));

        /* New Sub Menu */
        MUIMenuItem project = new MUIMenuItem("Project");
        MUIMenuItem new_file = new MUIMenuItem("File");
        new_file.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.NEW_FILE)));
        project.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.NEW_PROJECT)));
        MUISubMenu newMenu = new MUISubMenu( "New",
                project,
                new_file
        );

        file.getMenu().getItems().addAll(
                newMenu,
                save,
                save_all,
                open,
                openFolder,
                settings
        );
        return file;
    }


    private MUIMenuButton edit()
    {
        MUIMenuButton edit = new MUIMenuButton("Edit");
        MUIMenuButton undo = new MUIMenuItem("Undo");
        MUIMenuButton redo = new MUIMenuItem("Redo");
        MUIMenuButton cut = new MUIMenuItem("Cut");
        MUIMenuButton copy = new MUIMenuItem("Copy");
        MUIMenuButton paste = new MUIMenuItem("Paste");
        MUIMenuButton find = new MUIMenuItem("Find");
        MUIMenuButton select_all = new MUIMenuItem("Select All");

        undo.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.UNDO)));
        redo.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.REDO)));
        cut.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.CUT)));
        copy.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.COPY)));
        paste.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.PASTE)));
        find.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.FIND)));
        select_all.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.SELECT_ALL)));

        edit.getMenu().getItems().addAll (
                undo,
                redo,
                cut,
                copy,
                paste,
                find,
                select_all
        );
        return edit;
    }
    private MUIMenuButton view()
    {
        MUIMenuButton view = new MUIMenuButton("View");
        MUIMenuItem plugins = new MUIMenuItem("Plugins");
        MUIMenuItem terminal = new MUIMenuItem(FontAwesomeSolid.CHECK,"Terminal", ContentDisplay.LEFT);
        MUIMenuItem problems = new MUIMenuItem(FontAwesomeSolid.CHECK,"Problems", ContentDisplay.LEFT);
        MUIMenuItem navigator = new MUIMenuItem(FontAwesomeSolid.CHECK,"Navigator", ContentDisplay.LEFT);
        Consumer<MUIMenuItem> toggleCheck = item -> {
            if (item.isIconHidden())
                item.setIconHidden(false);
            else
                item.setIconHidden(true);
        };

        plugins.setOnAction(event -> this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.PLUGINS)));
        terminal.setOnAction(event -> {
            toggleCheck.accept(terminal);
            this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.TERMINAL));
        });
        problems.setOnAction(event -> {
            toggleCheck.accept(problems);
            this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.PROBLEMS));
        });
        navigator.setOnAction(event -> {
            toggleCheck.accept(navigator);
            this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.NAVIGATOR));
        });

        terminal.setIconOpacity((LocalUser.userSettings.isTerminalClosed() ? 0.0 : 1.0));
        problems.setIconOpacity((LocalUser.userSettings.isTerminalClosed() ? 0.0 : 1.0));
        navigator.setIconOpacity((LocalUser.userSettings.isNavigatorClosed() ? 0.0 : 1.0));

        view.getMenu().getItems().addAll(
                plugins,
                terminal,
                problems,
                navigator
        );


        return view;
    }
    private MUIMenuButton code()
    {
        MUIMenuButton code = new MUIMenuButton("Code");
        MUIMenuButton reformatDocument = new MUIMenuButton("Reformat Document");
        reformatDocument.setOnAction(event -> this.fireEvent(new MenuBarCodeEvent(OEditorController.getActiveEditor(), this, MenuBarCodeEvent.REFORMAT_ACTIVE_DOCUMENT)));
        code.getMenu().getItems().add(reformatDocument);
        return code;
    }

    private MUIMenuButton[] rightMenu()
    {
        MUIMenuButton play = new MUIMenuButton(FontAwesomeSolid.PLAY_CIRCLE, "PLAY", ContentDisplay.LEFT);
        MUIMenuButton stop = new MUIMenuButton(FontAwesomeSolid.STOP_CIRCLE, "STOP", ContentDisplay.LEFT);
        MUIMenuButton[] iconButtons = new MUIMenuButton[]{
                play,
                stop
        };
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_LOAD, event -> Platform.runLater(() -> Arrays.stream(iconButtons).forEach(iconButton -> {
            iconButton.setIconScale(1.25);
            iconButton.setPrefWidth(iconButton.getWidth() + 20.0);
            iconButton.setIconXOffset(-4);
        })));

        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.PLAY_CLICK)));
        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.STOP_CLICK)));
        return iconButtons;
    }

    private void showFileChooserDialog()
    {
        FileChooser chooser = new FileChooser();
        File selectedFile = chooser.showOpenDialog(this.menuBar.getScene().getWindow());
        if (selectedFile != null && selectedFile.exists())
            this.fireEvent(new MenuBarFileEvent(MenuBarFileEvent.OPEN, new ProjectFile(selectedFile)));
    }

    private void showFolderChooseDialog()
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Folder");
        File root = chooser.showDialog(this.menuBar.getScene().getWindow());
        if (root != null && root.exists() && root.isDirectory())
        {
            this.fireEvent(new MenuBarFileEvent(MenuBarFileEvent.OPEN_FOLDER, new ProjectFile(root)));
            LocalUser.userSettings.getLastModifiedProject().setProjectRoot(root);
        }

    }

    public Map<EventType, Collection<EventHandler>> getHandlers() { return this.handlers; }
}









