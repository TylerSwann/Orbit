package io.orbit.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Tyler Swann on Thursday May 03, 2018 at 15:58
 */
@Deprecated
public class UtilityStage extends PopupControl
{
    private static final double spread = 20.0;
    private static final double taskBarHeight = 30.0;

    public UtilityStage(Parent content)
    {

    }

    private void build(Parent content)
    {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1500.0, 900.0);

    }
}
