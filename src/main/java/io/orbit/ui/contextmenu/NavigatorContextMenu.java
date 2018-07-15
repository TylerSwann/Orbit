package io.orbit.ui.contextmenu;

import javafx.scene.Node;
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
        newMenu.getSubmenu().root.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.root.setPrefSize(175.0, 200.0);
        this.getItems().addAll(items);
    }
}
