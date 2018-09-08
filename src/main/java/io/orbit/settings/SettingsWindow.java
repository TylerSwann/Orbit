package io.orbit.settings;

import io.orbit.App;
import io.orbit.ui.MUITreeItem;
import io.orbit.ui.MUITreeTableView;
import io.orbit.util.Setting;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Created by Tyler Swann on Friday February 23, 2018 at 14:58
 */
public class SettingsWindow extends Stage
{
    private MUITreeTableView<String> settingsTreeView;
    private AnchorPane contentContainer;

    public SettingsWindow(Setting[] settings)
    {
        this.setTitle("Settings");
        this.initStyle(StageStyle.UTILITY);
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1287, 749);
        this.settingsTreeView = new MUITreeTableView<>();
        settingsTreeView.setPrefSize(100.0, 160.0);
        applyAnchors(settingsTreeView);
        this.contentContainer = new AnchorPane();
        contentContainer.setPrefSize(100.0, 160.0);
        contentContainer.getStyleClass().add("background");
        SplitPane splitPane = new SplitPane(settingsTreeView, contentContainer);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getDividers().forEach(div -> div.setPosition(0.26848249027237353));
        applyAnchors(splitPane);
        root.getChildren().add(splitPane);
        MUITreeItem<String> rootBranch = new MUITreeItem<>("Settings");
        settingsTreeView.setRoot(rootBranch);
        rootBranch.setExpanded(true);
        for (Setting setting : settings)
        {
            if (setting.isParentSetting)
            {
                MUITreeItem<String> parent = new MUITreeItem<>(setting.title);
                rootBranch.getChildren().add(parent);
                this.addSetting(parent, setting.subSettings);
            }
            else
            {
                MUITreeItem<String> child = new MUITreeItem<>(setting.title);
                child.setUserData(setting.content);
                rootBranch.getChildren().add(child);
            }
        }
        addListeners();
        Scene scene = new Scene(root, 1287,749);
        this.setScene(scene);
        App.appTheme.sync(this.getScene().getStylesheets());
    }

    private void addListeners()
    {
        this.settingsTreeView.setOnItemClicked(item -> {
            if (item != null && item.getUserData() != null && item.getUserData() instanceof Node)
            {
                Node content = ((Node)item.getUserData());
                this.contentContainer.getChildren().removeAll(this.contentContainer.getChildren());
                this.contentContainer.getChildren().add(content);
            }
        });
    }

    private void applyAnchors(Node node)
    {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private void addSetting(MUITreeItem<String> parent, Setting[] settings)
    {
        for (Setting setting : settings)
        {
            if (setting.isParentSetting)
            {
                MUITreeItem<String> parentBranch = new MUITreeItem<>(setting.title);
                this.addSetting(parentBranch, setting.subSettings);
                continue;
            }
            MUITreeItem<String> child = new MUITreeItem<>(setting.title);
            child.setUserData(setting.content);
            parent.getChildren().add(child);
        }
    }
}
