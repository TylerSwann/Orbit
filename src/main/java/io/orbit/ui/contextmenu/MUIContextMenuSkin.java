package io.orbit.ui.contextmenu;

import javafx.scene.Node;
import javafx.scene.control.Skin;

/**
 * Created by Tyler Swann on Saturday July 07, 2018 at 08:55
 */
public class MUIContextMenuSkin implements Skin<MUIContextMenu>
{
    private MUIContextMenu contextMenu;

    public MUIContextMenuSkin(MUIContextMenu contextMenu)
    {
        this.contextMenu = contextMenu;
    }

    @Override
    public MUIContextMenu getSkinnable()
    {
        return this.contextMenu;
    }

    @Override
    public Node getNode()
    {
        return this.contextMenu.getScene().getRoot();
    }

    @Override
    public void dispose()
    {
        this.contextMenu = null;
    }
}
