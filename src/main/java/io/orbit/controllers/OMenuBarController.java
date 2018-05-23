package io.orbit.controllers;

import io.orbit.App;
import io.orbit.settings.LocalUser;
import io.orbit.settings.OrbitFile;
import io.orbit.settings.ProjectFile;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.StatelessEventTargetObject;
import io.orbit.controllers.events.menubar.*;
import io.orbit.ui.*;
import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 13:01
 */
public class OMenuBarController extends StatelessEventTargetObject
{
    private MenuButtonBar menuBar = new MenuButtonBar();

    public OMenuBarController(AnchorPane container)
    {
        Menu file = file();
        Menu view = view();
        Menu edit = edit();
        Menu code = code();

        this.menuBar.getLeftMenus().addAll(
                file,
                view,
                edit,
                code
        );
        this.menuBar.getRightButtons().addAll(this.rightMenu());
        AnchorPane.setTopAnchor(this.menuBar, 0.0);
        AnchorPane.setLeftAnchor(this.menuBar, 0.0);
        AnchorPane.setRightAnchor(this.menuBar, 0.0);
        container.getChildren().add(this.menuBar);
    }


    private Menu file()
    {
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem save_all = new MenuItem("Save All");
        MenuItem open = new MenuItem("Open");
        MenuItem openFolder = new MenuItem("Open Folder");
        MenuItem settings = new MenuItem("Settings");
        save.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(MenuBarFileEvent.SAVE, OEditorController.getActiveEditor().file)));
        save_all.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.SAVE_ALL)));
        settings.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.SETTINGS)));
        open.setOnAction(event -> this.showFileChooserDialog());
        openFolder.setOnAction(event -> this.showFolderChooseDialog());
        MenuItem project = new MenuItem("Project");
        MenuItem new_file = new MenuItem("File");
        new_file.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.NEW_FILE)));
        project.setOnAction(event -> this.fireEvent(new MenuBarFileEvent(this, this, MenuBarFileEvent.NEW_PROJECT)));
        Menu newMenu = new Menu( "New");
        newMenu.getItems().addAll(
                project,
                new_file
        );
        file.getItems().addAll(
                newMenu,
                save,
                save_all,
                open,
                openFolder,
                settings
        );
        return file;
    }
    private Menu edit()
    {
        Menu edit = new Menu("Edit");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem find = new MenuItem("Find");
        MenuItem select_all = new MenuItem("Select All");

        undo.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.UNDO)));
        redo.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.REDO)));
        cut.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.CUT)));
        copy.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.COPY)));
        paste.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.PASTE)));
        find.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.FIND)));
        select_all.setOnAction(event -> this.fireEvent(new MenuBarEditEvent(this, this, MenuBarEditEvent.SELECT_ALL)));

        edit.getItems().addAll (
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
    private Menu view()
    {
        Menu view = new Menu("View");
        MenuItem plugins = new MenuItem("Plugins");
        MenuItem terminal = new MenuItem("Terminal", new FontIcon(FontAwesomeSolid.CHECK));
        MenuItem navigator = new MenuItem("Navigator", new FontIcon(FontAwesomeSolid.CHECK));
        Consumer<MenuItem> toggleCheck = item -> {
            if (item.getGraphic().getOpacity() <= 0.0)
                item.getGraphic().setOpacity(1.0);
            else
                item.getGraphic().setOpacity(0.0);
        };

        plugins.setOnAction(event -> this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.PLUGINS)));
        terminal.setOnAction(event -> {
            toggleCheck.accept(terminal);
            this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.TERMINAL));
        });
        navigator.setOnAction(event -> {
            toggleCheck.accept(navigator);
            this.fireEvent(new MenuBarViewEvent(this, this, MenuBarViewEvent.NAVIGATOR));
        });
        terminal.getGraphic().setOpacity((LocalUser.userSettings.isTerminalClosed() ? 0.0 : 1.0));
        navigator.getGraphic().setOpacity((LocalUser.userSettings.isNavigatorClosed() ? 0.0 : 1.0));

        view.getItems().addAll(
                plugins,
                terminal,
                navigator
        );


        return view;
    }
    private Menu code()
    {
        Menu code = new Menu("Code");
        MenuItem reformatDocument = new MenuItem("Reformat Document");
        reformatDocument.setOnAction(event -> this.fireEvent(new MenuBarCodeEvent(OEditorController.getActiveEditor(), this, MenuBarCodeEvent.REFORMAT_ACTIVE_DOCUMENT)));
        code.getItems().add(reformatDocument);
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
            this.fireEvent(new MenuBarFileEvent(MenuBarFileEvent.OPEN, new OrbitFile(selectedFile)));
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
}









