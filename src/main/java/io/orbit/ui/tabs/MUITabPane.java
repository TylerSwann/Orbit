package io.orbit.ui.tabs;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITabPane extends AnchorPane
{
    public static final String DEFAULT_STYLE_CLASS = "mui-tab-pane";

    private MUITabBar tabBar;
    private boolean hasInit = false;

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
        this.selectedTabProperty().addListener(__ -> {
            if (this.getSelectedTab() != null)
                this.goTo(this.getSelectedTab());
            if (!hasInit)
            {
                hasInit = true;
                Platform.runLater(() -> {
                    if (this.getTabs().size() > 0)
                        this.goTo(this.getTabs().get(0));
                });
            }
        });
    }

    private void goTo(MUITab tab)
    {
        if (this.getSelectedTab() != null)
            this.getChildren().remove(this.getSelectedTab().getContent());
        if (tab.getContent() == null)
            return;
        if (this.tabBar.getHeight() <= 0.0)
            setTopAnchor(tab.getContent(), this.tabBar.getPrefHeight());
        else
            setTopAnchor(tab.getContent(), this.tabBar.getHeight());

        setBottomAnchor(tab.getContent(), 0.0);
        setLeftAnchor(tab.getContent(), 0.0);
        setRightAnchor(tab.getContent(), 0.0);
        this.getChildren().add(tab.getContent());
        this.tabBar.toFront();
    }

    public ObservableValue<MUITab> selectedTabProperty() {  return this.tabBar.selectedTabProperty();  }
    public MUITab getSelectedTab() {  return this.tabBar.getSelectedTab();  }
    public void select(MUITab tab) {  this.tabBar.select(tab);  }
    public void select(int index) {  this.tabBar.select(index);  }
    public ObservableList<MUITab> getTabs() { return this.tabBar.getTabs(); }
}
