package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.settings.LocalUser;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.menubar.MenuBarViewEvent;
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
            Platform.runLater(() -> App.applicationController().contentSplitPane.getItems().remove(container));
        else
            buildTerminal();
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> {
            if (!LocalUser.userSettings.isTerminalClosed())
                LocalUser.userSettings.setTerminalDividerPos(App.applicationController().contentSplitPane.getDividerPositions()[0]);
        });
        Platform.runLater(() -> App.applicationController().getMenuBarController().addEventHandler(MenuBarViewEvent.TERMINAL, event -> toggleTerminal()));
    }

    public void toggleTerminal()
    {
        boolean terminalIsClosed = LocalUser.userSettings.isTerminalClosed();
        LocalUser.userSettings.setTerminalClosed(!terminalIsClosed);
        ApplicationController controller = App.applicationController();
        if (terminalIsClosed)
        {
            controller.contentSplitPane.getItems().add(this.container);
            if (terminal == null)
                buildTerminal();
            controller.contentSplitPane.setDividerPosition(0, LocalUser.userSettings.getTerminalDividerPos());
        }
        else
            controller.contentSplitPane.getItems().remove(this.container);
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
            ApplicationController controller = App.applicationController();
            controller.contentSplitPane.setDividerPosition(0, LocalUser.userSettings.getTerminalDividerPos());
            controller.contentSplitPane
                    .getDividers()
                    .get(0)
                    .positionProperty()
                    .addListener(event -> {
                        if (controller.contentSplitPane.getDividers().size() > 0)
                            LocalUser.userSettings.setTerminalDividerPos(controller.contentSplitPane.getDividerPositions()[0]);
                    });
        });
    }
}