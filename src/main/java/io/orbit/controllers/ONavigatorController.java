package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.settings.LocalUser;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.menubar.MenuBarViewEvent;
import io.orbit.ui.MUITabPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Created by Tyler Swann on Thursday February 15, 2018 at 17:04
 */
public class ONavigatorController
{
    private MUITabPane navigatorTabPane;
    private AnchorPane container;

    public ONavigatorController(AnchorPane container)
    {
        this.container = container;
        boolean navigatorIsClosed = LocalUser.userSettings.isNavigatorClosed();
        if (navigatorIsClosed)
            Platform.runLater(() -> App.applicationController().rootSplitPane.getItems().remove(container));
        else
            build();
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> {
            if (!LocalUser.userSettings.isNavigatorClosed())
                LocalUser.userSettings.setNavigatorDividerPos(App.applicationController().rootSplitPane.getDividerPositions()[0]);
        });
        Platform.runLater(() -> App.applicationController().getMenuBarController().addEventHandler(MenuBarViewEvent.NAVIGATOR, event -> toggleNavigator()));
    }

    public void toggleNavigator()
    {
        boolean navigatorIsClosed = LocalUser.userSettings.isNavigatorClosed();
        LocalUser.userSettings.setNavigatorClosed(!navigatorIsClosed);
        ApplicationController controller = App.applicationController();
        if (navigatorIsClosed)
        {
            controller.rootSplitPane.getItems().add(0, this.container);
            if (this.navigatorTabPane == null)
                build();
            controller.rootSplitPane.setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
        }
        else
            controller.rootSplitPane.getItems().remove(this.container);
    }


    private void build()
    {
        this.navigatorTabPane = new MUITabPane();
        Tab gitTab = new Tab();
        Tab projectStructureTab = new Tab();
        FontIcon gitIcon = new FontIcon(FontAwesomeBrands.GITHUB_SQUARE);
        FontIcon projectIcon = new FontIcon(FontAwesomeSolid.FOLDER_OPEN);
        VBox gitIconContainer = new VBox(gitIcon);
        VBox projectIconContainer = new VBox(projectIcon);

        gitIcon.setFill(Color.BLACK);
        projectIcon.setFill(Color.BLACK);

        double scale = 1.5;
        double tabHeight = 25.5;
        double tabWidth = 100.0;

        gitIcon.setScaleX(scale);
        gitIcon.setScaleY(scale);
        projectIcon.setScaleX(scale);
        projectIcon.setScaleY(scale);
        gitIconContainer.setPrefSize(tabWidth, tabHeight);
        projectIconContainer.setPrefSize(tabWidth, tabHeight);
        gitIconContainer.setAlignment(Pos.CENTER);
        projectIconContainer.setAlignment(Pos.CENTER);
        projectIconContainer.setPadding(Insets.EMPTY);

        gitTab.setGraphic(gitIconContainer);
        projectStructureTab.setGraphic(projectIconContainer);
        this.navigatorTabPane.setPadding(Insets.EMPTY);
        navigatorTabPane.getTabs().addAll(projectStructureTab, gitTab);
        navigatorTabPane.getStyleClass().add("project-tab-pane");

        AnchorPane.setTopAnchor(this.navigatorTabPane, 0.0);
        AnchorPane.setBottomAnchor(this.navigatorTabPane, 0.0);
        AnchorPane.setLeftAnchor(this.navigatorTabPane, 0.0);
        AnchorPane.setRightAnchor(this.navigatorTabPane, 0.0);

        container.getChildren().add(this.navigatorTabPane);

        Platform.runLater(() -> {
            ApplicationController controller = App.applicationController();
            controller.rootSplitPane.setDividerPosition(0, LocalUser.userSettings.getNavigatorDividerPos());
            controller.getProjectTreeViewController().setTab(projectStructureTab);
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
