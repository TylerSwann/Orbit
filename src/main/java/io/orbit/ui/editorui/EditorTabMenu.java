package io.orbit.ui.editorui;

import io.orbit.ui.contextmenu.MUIContextMenu;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.scene.control.Tab;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:29
 */
public class EditorTabMenu extends MUIContextMenu
{
    private Consumer<Tab> onClose = __ -> {};
    private Consumer<Tab> onCloseOthers = __ -> {};
    private Consumer<Tab> onCloseAll = __ -> {};

    public EditorTabMenu()
    {
        super();
        this.build();
    }

    private void build()
    {
        MUIMenuItem close = new MUIMenuItem(FontAwesomeSolid.TIMES,"Close");
        MUIMenuItem closeOthers = new MUIMenuItem(FontAwesomeSolid.TIMES_CIRCLE,"Close Others");
        MUIMenuItem closeAll = new MUIMenuItem(FontAwesomeSolid.THUMBTACK,"Close All");
        close.setOnAction( __ -> this.onClose.accept(null));
        closeOthers.setOnAction( __ -> this.onCloseOthers.accept(null));
        closeAll.setOnAction( __ -> this.onCloseAll.accept(null));
        this.getItems().addAll(Arrays.asList(
                close,
                closeOthers,
                closeAll
        ));
    }

    public void setOnClose(Consumer<Tab> onClose) { this.onClose = onClose; }
    public void setOnCloseOthers(Consumer<Tab> onCloseOthers) { this.onCloseOthers = onCloseOthers; }
    public void setOnCloseAll(Consumer<Tab> onCloseAll) { this.onCloseAll = onCloseAll; }
}
