package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.settings.LocalUser;
import io.orbit.controllers.events.MenuBarEvent;
import io.orbit.ui.terminal.MUITerminal;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tyler Swann on Friday February 09, 2018 at 14:05
 */
public class OTerminalController
{
    private AnchorPane container;
    private MUITerminal terminal;


    public OTerminalController(AnchorPane container)
    {
        this.container = container;
        boolean terminalIsClosed = LocalUser.settings.isTerminalClosed();
        if (terminalIsClosed)
            Platform.runLater(() -> App.controller().getContentSplitPane().getItems().remove(container));
        else
            buildTerminal();
        App.addOnCloseHandler(() -> {
            if (this.terminal != null)
            {
                this.terminal.dispose();
                this.terminal = null;
            }
            if (!LocalUser.settings.isTerminalClosed())
                LocalUser.settings.setTerminalDividerPos(App.controller().getContentSplitPane().getDividerPositions()[0]);
        });
        Platform.runLater(() -> App.controller().getMenuBarController().addEventHandler(MenuBarEvent.VIEW_TERMINAL, event -> toggleTerminal()));
    }

    public void toggleTerminal()
    {
        boolean terminalIsClosed = LocalUser.settings.isTerminalClosed();
        LocalUser.settings.setTerminalClosed(!terminalIsClosed);
        ApplicationController controller = App.controller();
        if (terminalIsClosed)
        {
            controller.getContentSplitPane().getItems().add(this.container);
            if (terminal == null)
                buildTerminal();
            controller.getContentSplitPane().setDividerPosition(0, LocalUser.settings.getTerminalDividerPos());
        }
        else
            controller.getContentSplitPane().getItems().remove(this.container);
    }

    private void buildTerminal()
    {
        this.terminal = new MUITerminal();
        this.terminal.open();
        AnchorPane.setTopAnchor(terminal, 0.0);
        AnchorPane.setBottomAnchor(terminal, 0.0);
        AnchorPane.setLeftAnchor(terminal, 0.0);
        AnchorPane.setRightAnchor(terminal, 0.0);
        container.getChildren().add(terminal);
        Platform.runLater(() -> {
            ApplicationController controller = App.controller();
            controller.getContentSplitPane().setDividerPosition(0, LocalUser.settings.getTerminalDividerPos());
            controller.getContentSplitPane()
                    .getDividers()
                    .get(0)
                    .positionProperty()
                    .addListener(event -> {
                        if (controller.getContentSplitPane().getDividers().size() > 0)
                            LocalUser.settings.setTerminalDividerPos(controller.getContentSplitPane().getDividerPositions()[0]);
                    });
        });
    }
}