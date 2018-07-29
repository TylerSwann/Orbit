package io.orbit.ui.navigator;

import javafx.css.PseudoClass;
import javafx.scene.control.TreeCell;

import java.io.File;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:10
 */
public class FileTreeCell extends TreeCell<File>
{

    private static PseudoClass HIGHLIGHTED = PseudoClass.getPseudoClass("highlighted");

    @Override
    protected void updateItem(File item, boolean empty)
    {
        super.updateItem(item, empty);
        if (empty || this.getItem() == null)
            this.setText(null);
        else
            this.setText(this.getItem().getName());
    }

    public void setHighlighted(boolean highlighted) { this.pseudoClassStateChanged(HIGHLIGHTED, highlighted); }
}
