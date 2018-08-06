package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.MenuBarEvent;
import io.orbit.settings.LocalUser;
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
    private OProjectViewController projectViewController;

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

    public void addFiles(File... files)
    {
        this.navigator.getProjectTreeView().addFiles(files);
    }

    public void removeFiles(File... files)
    {
        this.navigator.getProjectTreeView().removeFiles(files);
    }

    private void registerListeners()
    {
        boolean navigatorIsClosed = LocalUser.userSettings.isNavigatorClosed();
        if (navigatorIsClosed)
            Platform.runLater(() -> App.applicationController().getRootSplitPane().getItems().remove(container));
        else
            build();
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> {
            if (!LocalUser.userSettings.isNavigatorClosed())
                LocalUser.userSettings.setNavigatorDividerPos(App.applicationController().getRootSplitPane().getDividerPositions()[0]);
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
            controller.getRootSplitPane().getItems().add(0, this.container);
            if (this.navigator == null)
                build();
            controller.getRootSplitPane().setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
        }
        else
            controller.getRootSplitPane().getItems().remove(this.container);
    }

    private void build()
    {
        this.navigator = new NavigatorTabPane(root);
        this.projectViewController = new OProjectViewController(this.navigator.getProjectTreeView());
        AnchorPane.setTopAnchor(navigator, 0.0);
        AnchorPane.setBottomAnchor(navigator, 0.0);
        AnchorPane.setLeftAnchor(navigator, 0.0);
        AnchorPane.setRightAnchor(navigator, 0.0);
        container.getChildren().add(navigator);
        Platform.runLater(() -> {
            ApplicationController controller = App.applicationController();
            controller.getRootSplitPane().setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
            controller.getRootSplitPane()
                    .getDividers()
                    .get(0)
                    .positionProperty()
                    .addListener(event -> {
                        if (controller.getRootSplitPane().getDividers().size() > 0)
                            LocalUser.userSettings.setNavigatorDividerPos(controller.getRootSplitPane().getDividerPositions()[0]);
                    });
        });
    }

    public OProjectViewController getProjectViewController() { return projectViewController; }
}
