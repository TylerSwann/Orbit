package io.orbit.ui.navigator;

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.Tab;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

/**
 * Created by Tyler Swann on Friday July 20, 2018 at 15:21
 */
public class NavigatorTabPane extends JFXTabPane
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
    }

    private Tab gitTab()
    {
        Tab gitTab = new Tab();
        FontIcon gitIcon = new FontIcon(FontAwesomeBrands.GITHUB_SQUARE);
        gitTab.setGraphic(gitIcon);
        return gitTab;
    }

    private Tab projectStructureTab()
    {
        Tab projectTab = new Tab();
        FontIcon projectIcon = new FontIcon(FontAwesomeSolid.FOLDER_OPEN);
        projectTab.setGraphic(projectIcon);
        projectTab.setContent(this.projectTreeView);
        return projectTab;
    }

    public MUIFileTreeView getProjectTreeView() { return projectTreeView; }
}
