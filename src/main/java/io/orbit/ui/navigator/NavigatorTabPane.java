/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.orbit.ui.navigator;

import io.orbit.ui.tabs.MUITab;
import io.orbit.ui.tabs.MUITabPane;
import javafx.application.Platform;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.File;

/**
 * Created by Tyler Swann on Friday July 20, 2018 at 15:21
 */
public class NavigatorTabPane extends MUITabPane
{
    public static final String DEFAULT_STYLE_CLASS = "navigator-tab-pane";
    private MUIFileTreeView projectTreeView;

    public NavigatorTabPane(File root)
    {
        this.projectTreeView = new MUIFileTreeView(root);
        this.build();
    }

    private void build()
    {
        this.getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        this.getTabs().addAll(
                projectStructureTab(),
                gitTab()
        );
        Platform.runLater(() -> this.select(0));
    }

    private MUITab gitTab()
    {
        MUITab gitTab = new MUITab(new FontIcon(FontAwesomeBrands.GITHUB_SQUARE));
        return gitTab;
    }

    private MUITab projectStructureTab()
    {
        MUITab projectTab = new MUITab(new FontIcon(FontAwesomeSolid.FOLDER_OPEN));
        projectTab.setContent(this.projectTreeView);
        return projectTab;
    }

    public MUIFileTreeView getProjectTreeView() { return projectTreeView; }
}
