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
package io.orbit.ui.tabs;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:02
 */
public class MUITabPane extends AnchorPane
{
    public static final String DEFAULT_STYLE_CLASS = "mui-tab-pane";

    private MUITabBar tabBar;

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
        });
    }

    private void goTo(MUITab tab)
    {
        List<Node> children = new ArrayList<>(this.getChildren());
        children.forEach(child -> {
            if (!(child instanceof MUITabBar))
                this.getChildren().remove(child);
        });
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
