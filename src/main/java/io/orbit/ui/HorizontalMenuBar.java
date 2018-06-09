package io.orbit.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 29, 2018 at 13:52
 */
@Deprecated
public class HorizontalMenuBar extends HBox
{
    private MenuBar leftBar;
    private MenuBar rightBar;
    private ObservableList<Menu> leftMenus = FXCollections.observableArrayList();
    private ObservableList<Menu> rightMenus = FXCollections.observableArrayList();

    public HorizontalMenuBar()
    {
        this.leftBar = new MenuBar();
        this.rightBar = new MenuBar();
        registerListeners();
        this.setPrefHeight(30.0);
        this.setPrefWidth(400.0);
        this.setAlignment(Pos.CENTER);
        this.rightBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        this.getChildren().addAll(this.leftBar, this.rightBar);
        this.rightBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        this.leftBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public HorizontalMenuBar(List<Menu> leftMenus, List<Menu> rightMenus)
    {
        this(leftMenus.toArray(new Menu[leftMenus.size()]), rightMenus.toArray(new Menu[rightMenus.size()]));
    }

    public HorizontalMenuBar(Menu[] leftMenus, Menu[] rightMenus)
    {
        this();
        this.leftBar.getMenus().addAll(leftMenus);
        this.rightBar.getMenus().addAll(rightMenus);
    }

    private void registerListeners()
    {
        this.leftMenus.addListener((ListChangeListener<Menu>) c -> {
            this.leftBar.getMenus().clear();
            this.leftBar.getMenus().addAll(this.leftMenus);
        });
        this.rightMenus.addListener((ListChangeListener<Menu>) c -> {
            this.rightBar.getMenus().clear();
            this.rightBar.getMenus().addAll(this.rightMenus);
        });
        this.widthProperty().addListener(event -> {
            double halfWidth = this.getWidth() / 2.0;
            this.leftBar.setPrefWidth(halfWidth);
            this.rightBar.setPrefWidth(halfWidth);
        });
    }

    public ObservableList<Menu> getLeftMenus() { return leftMenus; }
    public ObservableList<Menu> getRightMenus() { return rightMenus; }
}
