package io.orbit.ui.editorui;

import io.orbit.ui.contextmenu.MUIContextMenu;
import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday July 22, 2018 at 15:29
 */
public class EditorTabMenu extends MUIContextMenu
{
    private Runnable onClose = () -> {};
    private Runnable onCloseOthers = () -> {};
    private Runnable onCloseAll = () -> {};
    public static final String DEFAULT_STYLE_CLASS = "editor-tab-menu";

    public EditorTabMenu()
    {
        super();
        this.build();
    }

    private void build()
    {
        this.getRoot().getStyleClass().add(DEFAULT_STYLE_CLASS);
        MUIMenuItem close = new MUIMenuItem(FontAwesomeSolid.TIMES,"Close");
        MUIMenuItem closeOthers = new MUIMenuItem(FontAwesomeSolid.TIMES_CIRCLE,"Close Others");
        MUIMenuItem closeAll = new MUIMenuItem(FontAwesomeSolid.THUMBTACK,"Close All");
        close.setOnAction( __ -> this.onClose.run());
        closeOthers.setOnAction( __ -> this.onCloseOthers.run());
        closeAll.setOnAction( __ -> this.onCloseAll.run());
        this.getItems().addAll(Arrays.asList(
                close,
                closeOthers,
                closeAll
        ));
    }

    public void setOnClose(Runnable onClose) { this.onClose = onClose; }
    public void setOnCloseOthers(Runnable onCloseOthers) { this.onCloseOthers = onCloseOthers; }
    public void setOnCloseAll(Runnable onCloseAll) { this.onCloseAll = onCloseAll; }
}
