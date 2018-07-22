package io.orbit.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by Tyler Swann on Sunday April 29, 2018 at 14:09
 */
@Deprecated
public class MenuButtonBar extends HBox
{
    private MenuBar leftBar;
    private HBox rightBar;
    private ObservableList<Menu> leftMenus = FXCollections.observableArrayList();
    private ObservableList<Button> rightButtons = FXCollections.observableArrayList();

    public MenuButtonBar()
    {
        this.leftBar = new MenuBar();
        this.rightBar = new HBox();
        registerListeners();
        this.setPrefHeight(30.0);
        this.setPrefWidth(400.0);
        this.rightBar.setPadding(new Insets(0.0, 10.0, 0.0, 0.0));
        this.setAlignment(Pos.CENTER);
        this.rightBar.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().addAll(this.leftBar, this.rightBar);
        this.rightBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        this.leftBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
//        this.leftBar.setUseSystemMenuBar(true);
    }

    public MenuButtonBar(List<Menu> leftMenus, List<Button> rightButtons)
    {
        this(leftMenus.toArray(new Menu[leftMenus.size()]), rightButtons.toArray(new Button[rightButtons.size()]));
    }

    public MenuButtonBar(Menu[] leftMenus, Button[] rightButtons)
    {
        this();
        this.leftBar.getMenus().addAll(leftMenus);
        this.rightBar.getChildren().addAll(rightButtons);
    }

    private void registerListeners()
    {
        this.leftMenus.addListener((ListChangeListener<Menu>) c -> {
            this.leftBar.getMenus().clear();
            this.leftBar.getMenus().addAll(this.leftMenus);
        });
        this.rightButtons.addListener((ListChangeListener<Button>) c -> {
            this.rightBar.getChildren().clear();
            this.rightBar.getChildren().addAll(this.rightButtons);
        });
        this.widthProperty().addListener(event -> {
            double halfWidth = this.getWidth() / 2.0;
            this.leftBar.setPrefWidth(halfWidth);
            this.rightBar.setPrefWidth(halfWidth);
        });
    }

    public ObservableList<Menu> getLeftMenus() { return leftMenus; }
    public ObservableList<Button> getRightButtons() { return rightButtons; }
}
