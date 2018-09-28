package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.settings.LocalUser;
import io.orbit.controllers.events.MenuBarEvent;
import io.orbit.ui.MUITerminalPane;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tyler Swann on Friday February 09, 2018 at 14:05
 */
public class OTerminalController
{
    private AnchorPane container;
    private MUITerminalPane terminal;


    public OTerminalController(AnchorPane container)
    {
        this.container = container;
        boolean terminalIsClosed = LocalUser.userSettings.isTerminalClosed();
        if (terminalIsClosed)
            Platform.runLater(() -> App.controller().getContentSplitPane().getItems().remove(container));
        else
            buildTerminal();
        App.addOnCloseHandler(() -> {
            if (!LocalUser.userSettings.isTerminalClosed())
                LocalUser.userSettings.setTerminalDividerPos(App.controller().getContentSplitPane().getDividerPositions()[0]);
        });
        Platform.runLater(() -> App.controller().getMenuBarController().addEventHandler(MenuBarEvent.VIEW_TERMINAL, event -> toggleTerminal()));
    }

    public void toggleTerminal()
    {
        boolean terminalIsClosed = LocalUser.userSettings.isTerminalClosed();
        LocalUser.userSettings.setTerminalClosed(!terminalIsClosed);
        ApplicationController controller = App.controller();
        if (terminalIsClosed)
        {
            controller.getContentSplitPane().getItems().add(this.container);
            if (terminal == null)
                buildTerminal();
            controller.getContentSplitPane().setDividerPosition(0, LocalUser.userSettings.getTerminalDividerPos());
        }
        else
            controller.getContentSplitPane().getItems().remove(this.container);
    }

    private void buildTerminal()
    {
        this.terminal = new MUITerminalPane();
        terminal.print("Microsoft Windows [Version 10.0.16299.192]");
        terminal.print("(c) 2017 Microsoft Corporation. All rights reserved.");
        terminal.print("");
        terminal.input("C:\\Users\\TylersDesktop>", System.out::println);
        AnchorPane.setTopAnchor(terminal, 0.0);
        AnchorPane.setBottomAnchor(terminal, 0.0);
        AnchorPane.setLeftAnchor(terminal, 0.0);
        AnchorPane.setRightAnchor(terminal, 0.0);
        container.getChildren().add(terminal);
        Platform.runLater(() -> {
            ApplicationController controller = App.controller();
            controller.getContentSplitPane().setDividerPosition(0, LocalUser.userSettings.getTerminalDividerPos());
            controller.getContentSplitPane()
                    .getDividers()
                    .get(0)
                    .positionProperty()
                    .addListener(event -> {
                        if (controller.getContentSplitPane().getDividers().size() > 0)
                            LocalUser.userSettings.setTerminalDividerPos(controller.getContentSplitPane().getDividerPositions()[0]);
                    });
        });
    }
}