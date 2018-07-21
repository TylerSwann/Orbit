package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.menubar.MenuBarEvent;
import io.orbit.settings.LocalUser;
import io.orbit.ui.contextmenu.NavigatorContextMenu;
import io.orbit.ui.navigator.NavigatorTabPane;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import java.io.File;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 19:29
 */
public class ONavigatorController
{
    private AnchorPane container;
    private NavigatorTabPane navigator;
    private File root;

    public ONavigatorController(AnchorPane container)
    {
        this.root = LocalUser.userSettings.getLastModifiedProject().getProjectRoot();
        this.container = container;
        registerListeners();
    }

    /**
     * Forces the MUIFileTreeView (Project Tree View) to refresh the files in its root folder.
     */
    public void forceRefresh()
    {
        this.navigator.getProjectTreeView().forceRefresh();
    }

    private void registerListeners()
    {
        boolean navigatorIsClosed = LocalUser.userSettings.isNavigatorClosed();
        if (navigatorIsClosed)
            Platform.runLater(() -> App.applicationController().rootSplitPane.getItems().remove(container));
        else
            build();
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> {
            if (!LocalUser.userSettings.isNavigatorClosed())
                LocalUser.userSettings.setNavigatorDividerPos(App.applicationController().rootSplitPane.getDividerPositions()[0]);
        });
        Platform.runLater(() -> App.applicationController().getMenuBarController().addEventHandler(MenuBarEvent.VIEW_NAVIGATOR, event -> toggleNavigator()));
    }

    public void toggleNavigator()
    {
        boolean navigatorIsClosed = LocalUser.userSettings.isNavigatorClosed();
        LocalUser.userSettings.setNavigatorClosed(!navigatorIsClosed);
        ApplicationController controller = App.applicationController();
        if (navigatorIsClosed)
        {
            controller.rootSplitPane.getItems().add(0, this.container);
            if (this.navigator == null)
                build();
            controller.rootSplitPane.setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
        }
        else
            controller.rootSplitPane.getItems().remove(this.container);
    }

    private void build()
    {
        this.navigator = new NavigatorTabPane(root);
        this.navigator.getProjectTreeView().setContextMenu(new NavigatorContextMenu());
        AnchorPane.setTopAnchor(navigator, 0.0);
        AnchorPane.setBottomAnchor(navigator, 0.0);
        AnchorPane.setLeftAnchor(navigator, 0.0);
        AnchorPane.setRightAnchor(navigator, 0.0);
        container.getChildren().add(navigator);
        Platform.runLater(() -> {
            ApplicationController controller = App.applicationController();
            controller.rootSplitPane.setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
            controller.rootSplitPane
                    .getDividers()
                    .get(0)
                    .positionProperty()
                    .addListener(event -> {
                        if (controller.rootSplitPane.getDividers().size() > 0)
                            LocalUser.userSettings.setNavigatorDividerPos(controller.rootSplitPane.getDividerPositions()[0]);
                    });
        });
    }
}
