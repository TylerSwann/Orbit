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

package io.orbit.ui.menubar;

import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import javax.swing.*;

/**
 * Created by Tyler Swann on Friday July 13, 2018 at 17:11
 */
public class MUIMenuBar extends GridPane
{
    private static final String DEFAULT_STYLE_CLASS = "mui-menu-bar";
    private static final String DEFAULT_LEFT_STYLE_CLASS = "left";
    private static final String DEFAULT_RIGHT_STYLE_CLASS = "right";
    protected HBox left = new HBox();
    protected HBox right = new HBox();
    private ObservableList<MUIMenuItem> rightItems = FXCollections.observableArrayList();
    private ObservableList<MUIMenuItem> leftItems = FXCollections.observableArrayList();
    public ObservableList<MUIMenuItem> getLeftItems() { return leftItems; }
    public ObservableList<MUIMenuItem> getRightItems() { return rightItems; }

    public MUIMenuBar()
    {
        build();
        registerListeners();
    }

    private void build()
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.left.getStyleClass().add(DEFAULT_LEFT_STYLE_CLASS);
        this.right.getStyleClass().add(DEFAULT_RIGHT_STYLE_CLASS);
        this.setMinSize(200.0, 30.0);
        this.setMaxHeight(35.0);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50.0);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50.0);
        RowConstraints row = new RowConstraints();
        row.setPrefHeight(25.0);
        this.getRowConstraints().add(row);
        this.getColumnConstraints().addAll(leftColumn, rightColumn);
        this.left.setPrefSize(100.0, 30.0);
        this.left.setAlignment(Pos.CENTER_LEFT);
        this.left.setSpacing(5.0);
        this.left.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
        this.right.setPrefSize(100.0, 30.0);
        this.right.setAlignment(Pos.CENTER_RIGHT);
        this.right.setSpacing(5.0);
        this.right.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));
        this.setAlignment(Pos.CENTER);
        this.add(this.left, 0, 0);
        this.add(this.right, 1, 0);
        this.right.prefHeightProperty().bind(this.heightProperty());
        this.left.prefHeightProperty().bind(this.heightProperty());
        this.setPadding(new Insets(1.0, 0.0, 1.0, 0.0));
    }

    private void registerListeners()
    {
        this.rightItems.addListener((ListChangeListener<MUIMenuItem>) change -> {
            while (change.next())
            {
                if (change.wasRemoved())
                    change.getRemoved().forEach(removed -> {
                        if (removed.prefHeightProperty().isBound())
                            removed.prefHeightProperty().unbind();
                        this.right.getChildren().remove(removed);
                    });
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(added -> {
                        if (!added.prefHeightProperty().isBound())
                            added.prefHeightProperty().bind(this.heightProperty());
                        this.right.getChildren().add(added);
                    });

                }
            }
        });
        this.leftItems.addListener((ListChangeListener<MUIMenuItem>) change -> {
            while (change.next())
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(removed -> {
                        if (removed.prefHeightProperty().isBound())
                            removed.prefHeightProperty().unbind();
                        this.left.getChildren().remove(removed);
                    });
                }
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(added -> {
                        if (!added.prefHeightProperty().isBound())
                            added.prefHeightProperty().bind(this.heightProperty());
                        this.left.getChildren().add(added);
                    });
                }
            }
        });
    }
}
