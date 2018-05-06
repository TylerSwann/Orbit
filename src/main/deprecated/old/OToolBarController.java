package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ui.MUIMenuBar;
import io.orbit.ui.MUIMenuButton;;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * Created by Tyler Swann on Friday February 09, 2018 at 13:13
 */
@Deprecated
public class OToolBarController
{
    private MUIMenuBar navBar;
    private double dividerPos = 0.7921;
    private boolean isClosed = false;

    public OToolBarController(AnchorPane container)
    {
        //TODO - fix divider open/close issues where the divider isn't disabled
        MUIMenuButton terminal = new MUIMenuButton(FontAwesomeSolid.TERMINAL, "Terminal", ContentDisplay.LEFT);
        MUIMenuButton problems = new MUIMenuButton(FontAwesomeSolid.EXCLAMATION_TRIANGLE, "Problems", ContentDisplay.LEFT);
        this.navBar = new MUIMenuBar(
                new MUIMenuButton[]{
                        terminal,
                        problems
                },
                new MUIMenuButton[]{}
        );
        this.navBar.setPrefHeight(30.0);
        this.navBar.setSidePadding(50.0);
        AnchorPane.setRightAnchor(this.navBar, 0.0);
        AnchorPane.setLeftAnchor(this.navBar, 0.0);
        AnchorPane.setBottomAnchor(this.navBar, 0.0);
        container.getChildren().add(this.navBar);
        terminal.setOnAction(event -> toggleTerminal());
        problems.setOnAction(event -> toggleTerminal());
    }

    private void toggleTerminal()
    {
        SplitPane.Divider div = App.applicationController().contentSplitPane.getDividers().get(0);
        if (isClosed)
            div.setPosition(this.dividerPos);
        else
        {
            this.dividerPos = div.getPosition();
            div.setPosition(1.0);
        }
        this.isClosed = !this.isClosed;
    }
}
