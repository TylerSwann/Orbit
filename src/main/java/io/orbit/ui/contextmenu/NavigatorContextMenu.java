package io.orbit.ui.contextmenu;

import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Friday July 06, 2018 at 19:48
 */
public class NavigatorContextMenu extends MUIContextMenu
{
    private static final String DEFAULT_STYLE_CLASS = "navigator-context-menu";

    private Runnable onCut = () -> {};
    private Runnable onCopy = () -> {};
    private Runnable onCopyPath = () -> {};
    private Runnable onCopyRelativePath = () -> {};
    private Runnable onPaste = () -> {};
    private Runnable onDelete = () -> {};
    private Runnable onNewFile = () -> {};
    private Runnable onNewFolder = () -> {};
    private Runnable onNewProject = () -> {};

    public NavigatorContextMenu()
    {
        super();
        this.root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        build();
    }

    private void build()
    {
        MUIMenuItem cut = new MUIMenuItem(FontAwesomeSolid.CUT," Cut");
        MUIMenuItem copy = new MUIMenuItem(FontAwesomeSolid.COPY," Copy");
        MUIMenuItem copyPath = new MUIMenuItem(FontAwesomeRegular.COPY," Copy Path");
        MUIMenuItem copyRelativePath = new MUIMenuItem(FontAwesomeSolid.COPY," Copy Relative Path");
        MUIMenuItem paste = new MUIMenuItem(FontAwesomeSolid.PASTE," Paste");
        MUIMenuItem delete = new MUIMenuItem(FontAwesomeSolid.TRASH," Delete");

        MUIMenuItem file = new MUIMenuItem(FontAwesomeSolid.FILE," File");
        MUIMenuItem directory = new MUIMenuItem(FontAwesomeSolid.FOLDER," Directory");
        MUIMenuItem project = new MUIMenuItem(FontAwesomeSolid.CUBES," Project");
        MUIMenu newMenu = new MUIMenu("New", file, directory, project);
        List<MUIMenuItem> items = Arrays.asList(
                newMenu,
                cut,
                copy,
                copyPath,
                copyRelativePath,
                paste,
                delete
        );

        cut.setOnAction(__ -> this.onCut.run());
        copy.setOnAction(__ -> this.onCopy.run());
        copyPath.setOnAction(__ -> this.onCopyPath.run());
        copyRelativePath.setOnAction(__ -> this.onCopyRelativePath.run());
        paste.setOnAction(__ -> this.onPaste.run());
        delete.setOnAction(__ -> this.onDelete.run());
        file.setOnAction(__ -> this.onNewFile.run());
        directory.setOnAction(__ -> this.onNewFolder.run());
        project.setOnAction(__ -> this.onNewProject.run());

        newMenu.getSubmenu().root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.root.setPrefSize(175.0, 200.0);
        this.getItems().addAll(items);
    }

    public void setOnCut(Runnable onCut) { this.onCut = onCut; }
    public void setOnCopy(Runnable onCopy) { this.onCopy = onCopy; }
    public void setOnCopyPath(Runnable onCopyPath) { this.onCopyPath = onCopyPath; }
    public void setOnCopyRelativePath(Runnable onCopyRelativePath) { this.onCopyRelativePath = onCopyRelativePath; }
    public void setOnPaste(Runnable onPaste) { this.onPaste = onPaste; }
    public void setOnDelete(Runnable onDelete) { this.onDelete = onDelete; }
    public void setOnNewFile(Runnable onNewFile) { this.onNewFile = onNewFile; }
    public void setOnNewFolder(Runnable onNewFolder) { this.onNewFolder = onNewFolder; }
    public void setOnNewProject(Runnable onNewProject) { this.onNewProject = onNewProject; }
}
