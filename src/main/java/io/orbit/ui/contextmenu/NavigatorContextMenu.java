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


    public NavigatorContextMenu()
    {
        super();
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
        MUIMenu newMenu = test();
        List<MUIMenuItem> items = Arrays.asList(
                newMenu,
                cut,
                copy,
                copyPath,
                copyRelativePath,
                paste,
                delete
        );
        this.root.setPrefSize(165.0, 200.0);
        this.getItems().addAll(items);
    }
    private MUIMenu test()
    {
        MUIMenuItem file = new MUIMenuItem(FontAwesomeSolid.FILE," File");
        MUIMenuItem dir = new MUIMenuItem(FontAwesomeSolid.FOLDER," Directory");
        MUIMenuItem project = new MUIMenuItem(FontAwesomeSolid.CUBES," Project");
        MUIMenuItem packge = new MUIMenuItem(FontAwesomeRegular.FOLDER_OPEN," Package");
        MUIMenu menu = new MUIMenu("New", file, dir, project, packge);
        return menu;
    }
}
