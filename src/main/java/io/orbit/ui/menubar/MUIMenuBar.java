package io.orbit.ui.menubar;

import io.orbit.ui.contextmenu.MUIMenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import javax.swing.*;

/**
 * Created by Tyler Swann on Friday July 13, 2018 at 17:11
 */
public class MUIMenuBar extends GridPane
{
    private static final String DEFAULT_STYLE_CLASS = "mui-menu-bar";
    private HBox left = new HBox();
    private HBox right = new HBox();
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
        this.setPrefSize(200.0, 30.0);
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50.0);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50.0);
        RowConstraints row = new RowConstraints();
        row.setFillHeight(true);
        this.getRowConstraints().add(row);
        this.getColumnConstraints().addAll(leftColumn, rightColumn);
        this.left.setPrefSize(100.0, 30.0);
        this.left.setAlignment(Pos.CENTER_LEFT);
        this.left.setSpacing(5.0);
        this.left.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
        this.right.setPrefSize(100.0, 30.0);
        this.right.setAlignment(Pos.CENTER_RIGHT);
        this.right.setSpacing(5.0);
        this.right.setPadding(new Insets(0.0, 10.0, 0.0, 0.0));

        this.setAlignment(Pos.CENTER);
        this.add(this.left, 0, 0);
        this.add(this.right, 1, 0);
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
                    change.getRemoved().forEach(removed -> this.left.getChildren().remove(removed));
                if (change.wasAdded())
                    change.getAddedSubList().forEach(added -> this.left.getChildren().add(added));
            }
        });
    }
}
