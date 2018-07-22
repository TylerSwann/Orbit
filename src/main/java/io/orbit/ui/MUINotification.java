package io.orbit.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;



/**
 * Created by Tyler Swann on Wednesday July 04, 2018 at 09:24
 */
@Deprecated
public class MUINotification extends PopupControl
{
    private Label titleLabel;
    private Label messageLabel;
    private AnchorPane root;
    private HBox topContainer;
    private FontIcon closeButton;

    public MUINotification(String title, String message)
    {
        this.titleLabel = new Label(title);
        this.messageLabel = new Label(message);
        this.topContainer = new HBox();
        this.root = new AnchorPane();
        this.closeButton = new FontIcon(FontAwesomeSolid.TIMES);
        this.root.getStyleClass().add("mui-notification");
        this.titleLabel.getStyleClass().add("title");
        this.messageLabel.getStyleClass().add("message");
        this.closeButton.getStyleClass().add("close-button");
        this.setAutoHide(true);
        build();
    }

    private void build()
    {
        this.root.setMinSize(300.0, 50.0);
        this.root.setPadding(new Insets(0.0, 15.0, 15.0, 15.0));
        this.topContainer.setPrefHeight(20.0);
        this.topContainer.setAlignment(Pos.TOP_CENTER);
        this.topContainer.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        this.messageLabel.setWrapText(true);
        this.messageLabel.setMaxWidth(350.0);
        this.messageLabel.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        this.closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.close());
        AnchorPane.setTopAnchor(this.topContainer, 0.0);
        AnchorPane.setLeftAnchor(this.topContainer, 0.0);
        AnchorPane.setRightAnchor(this.topContainer, 0.0);
        AnchorPane.setBottomAnchor(this.messageLabel, 0.0);
        AnchorPane.setLeftAnchor(this.messageLabel, 0.0);
        AnchorPane.setRightAnchor(this.messageLabel, 0.0);
        this.topContainer.getChildren().addAll(this.titleLabel, this.closeButton);
        this.root.getChildren().addAll(this.topContainer, this.messageLabel);
        this.getScene().setRoot(this.root);
        this.showingProperty().addListener(event -> {
            if (this.isShowing())
            {
                double topSpacing = this.topContainer.getWidth() - (this.titleLabel.getWidth() + this.closeButton.getStrokeWidth() + 0.0);
                this.topContainer.setSpacing(topSpacing);
                AnchorPane.setTopAnchor(this.messageLabel, this.topContainer.getHeight());
            }
        });
    }
    @Override
    public void show(Window owner)
    {
        super.show(owner);
        final double offsetX = 70.0;
        final double offsetY = 70.0;
        this.setX((owner.getX() + owner.getWidth()) - (this.getWidth() + offsetX));
        this.setY((owner.getY() + owner.getHeight()) - (this.getHeight() + offsetY));
        this.root.setOpacity(0.0);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), this.root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public void close()
    {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), this.root);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            if (this.isShowing())
                this.hide();
        });
        fadeTransition.play();
    }
}
