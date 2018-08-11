package io.orbit.ui.navigator;

import io.orbit.api.SVGIcon;
import io.orbit.ui.LanguageIcons;
import javafx.scene.control.TreeCell;
import java.io.File;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:10
 */
public class FileCell extends TreeCell<File>
{
    @Override
    protected void updateItem(File file, boolean empty)
    {
        super.updateItem(file, empty);
        if (empty)
            return;
        SVGIcon icon = LanguageIcons.iconForFile(file);
        icon.setScaleX(0.6);
        icon.setScaleY(0.6);
        this.setGraphic(icon);
        this.setText(file.getName());
    }
}
