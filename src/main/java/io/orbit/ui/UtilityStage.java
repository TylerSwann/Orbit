package io.orbit.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Tyler Swann on Thursday May 03, 2018 at 15:58
 */
public class UtilityStage extends Stage
{
    private static final double spread = 20.0;
    private static final double taskBarHeight = 30.0;
    public UtilityStage()
    {
        this.initStyle(StageStyle.TRANSPARENT);
        this.sceneProperty().addListener(event -> {
            if (this.getScene() == null)
                return;
            Scene scene = this.getScene();
            if (scene.getRoot() != null)
            {
                scene.setFill(Color.TRANSPARENT);
                this.build(scene.getRoot());
            }
        });
    }

    private void build(Parent sceneRoot)
    {

        AnchorPane pane = new AnchorPane();
        Pane taskbar = new Pane();
        taskbar.setStyle("-fx-background-color: blue;");
        taskbar.setPrefSize(100.0, 0.0);
        AnchorPane.setTopAnchor(taskbar, 0.0);
        AnchorPane.setLeftAnchor(taskbar, spread);
        AnchorPane.setRightAnchor(taskbar, spread);
        AnchorPane.setTopAnchor(sceneRoot, taskBarHeight);
        AnchorPane.setLeftAnchor(sceneRoot, 10.0);
        AnchorPane.setRightAnchor(sceneRoot, 10.0);
        AnchorPane.setBottomAnchor(sceneRoot, 10.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        DropShadow shadow = new DropShadow(10.0, Color.BLACK);
        sceneRoot.setEffect(shadow);
        pane.setStyle("-fx-background-insets: 10; -fx-background-color: red;");
        pane.getChildren().addAll(taskbar, sceneRoot);
        //sceneRoot.setStyle("-fx-background-insets: 20;");
        //this.getScene().setRoot(pane);
    }

}
