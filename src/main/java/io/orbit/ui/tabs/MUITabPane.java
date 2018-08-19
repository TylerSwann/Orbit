package io.orbit.ui.tabs;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITabPane extends AnchorPane
{
    public static final String DEFAULT_STYLE_CLASS = "mui-tab-pane";
    private MUITabBar tabBar;
    private MUITab currentTab;

    public MUITabPane()
    {
        this.tabBar = new MUITabBar();
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        setTopAnchor(this.tabBar, 0.0);
        setLeftAnchor(this.tabBar, 0.0);
        setRightAnchor(this.tabBar, 0.0);
        this.getChildren().add(this.tabBar);
        this.registerListeners();
    }

    private void registerListeners()
    {
        if (this.getTabs().size() > 0)
            this.goTo(this.getTabs().get(0));
        this.getTabs().addListener((ListChangeListener<MUITab>) change -> {
            while (change.next())
                change.getAddedSubList().forEach(tab -> tab.addEventHandler(MouseEvent.MOUSE_CLICKED, __ -> this.goTo(tab)));
        });
    }

    private void goTo(MUITab tab)
    {
        if (currentTab != null)
            this.getChildren().remove(this.currentTab.getContent());
        if (tab.getContent() == null)
            return;
        setTopAnchor(tab.getContent(), this.tabBar.getHeight());
        setBottomAnchor(tab.getContent(), 0.0);
        setLeftAnchor(tab.getContent(), 0.0);
        setRightAnchor(tab.getContent(), 0.0);
        this.currentTab = tab;
        this.getChildren().add(tab.getContent());
    }

    public ObservableList<MUITab> getTabs()
    {
        return this.tabBar.getTabs();
    }
}
