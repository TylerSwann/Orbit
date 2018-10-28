/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.orbit.ui.notification;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.MUIIconButton;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Oct 26, 2018
 * Time: 7:38 PM
 * Website: https://orbiteditor.com
 */
public class MUINotification extends HBox
{
    enum NotificationPos  { BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT }
    private static final String DEFAULT_STYLE_CLASS = "mui-notification";

    private Label label;
    private FontIcon fontIcon;
    private MUIIconButton closeButton;
    private JFXButton button;
    private final boolean includeButton;
    private AnchorPane owner;
    private boolean isShowing = false;
    private double timeout = 4000.0;

    private MUINotification(String message, Ikon iconCode, boolean includeButton)
    {
        this.includeButton = includeButton;
        this.label = new Label(message);

        this.closeButton = new MUIIconButton(MaterialDesign.MDI_CLOSE);
        if (includeButton)
            this.button = new JFXButton();
        else
            this.fontIcon = new FontIcon(iconCode);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.build();
    }

    public static MUINotification error(String message)
    {
        MUINotification notification = new MUINotification(message, FontAwesomeSolid.EXCLAMATION_CIRCLE, false);
        notification.getStyleClass().add("error");
        return notification;
    }
    public static MUINotification success(String message)
    {
        MUINotification notification = new MUINotification(message, FontAwesomeSolid.CHECK_CIRCLE, false);
        notification.getStyleClass().add("success");
        return notification;
    }
    public static MUINotification warning(String message)
    {
        MUINotification notification = new MUINotification(message, FontAwesomeSolid.EXCLAMATION_TRIANGLE, false);
        notification.getStyleClass().add("warning");
        return notification;
    }
    public static MUINotification informational(String message)
    {
        MUINotification notification = new MUINotification(message, FontAwesomeSolid.INFO_CIRCLE, false);
        notification.getStyleClass().add("informational");
        return notification;
    }
    public static MUINotification button(String message, String buttonText)
    {
        MUINotification notification = new MUINotification(message, null, true);
        notification.button.setText(buttonText);
        return notification;
    }

    private void build()
    {
        if (includeButton)
            this.getChildren().addAll(this.label, this.button, this.closeButton);
        else
            this.getChildren().addAll(this.fontIcon, this.label, this.closeButton);
        this.closeButton.setDisableVisualFocus(true);
        this.setPrefSize(340.0, 56.0);
        this.setAlignment(Pos.CENTER);
        this.closeButton.setOnAction(event -> this.hide());
        this.label.setPrefWidth(230.0);
        this.label.setWrapText(true);
        this.label.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
    }

    public void show(AnchorPane owner, NotificationPos position)
    {
        this.isShowing = true;
        this.owner = owner;
        owner.getChildren().add(this);
        switch (position)
        {
            case BOTTOM_RIGHT:
                AnchorPane.setBottomAnchor(this, 20.0);
                AnchorPane.setRightAnchor(this, 20.0);
                break;
            case BOTTOM_LEFT:
                AnchorPane.setBottomAnchor(this, 20.0);
                AnchorPane.setLeftAnchor(this, 20.0);
                break;
            case TOP_LEFT:
                AnchorPane.setTopAnchor(this, 20.0);
                AnchorPane.setLeftAnchor(this, 20.0);
                break;
            case TOP_RIGHT:
                AnchorPane.setTopAnchor(this, 20.0);
                AnchorPane.setRightAnchor(this, 20.0);
                break;
        }
        this.toFront();
        transition(true, null);
        PauseTransition transition = new PauseTransition(Duration.millis(this.timeout));
        transition.setOnFinished(__ -> this.hide());
        transition.play();
    }

    public void hide()
    {
        if (!this.isShowing)
            return;
        this.isShowing = false;
        transition(false, ()-> this.owner.getChildren().remove(this));
    }

    private void transition(boolean in, Runnable completion)
    {
        double from = in ? 0.0 : 1.0;
        double to = in ? 1.0 : 0.0;
        double fromY = in ? (this.getTranslateY() + (this.getPrefHeight() * 2.0)) : this.getTranslateY();
        double toY = in ? this.getTranslateY() : (this.getTranslateY() + (this.getPrefHeight() * 2.0));
        double fadeDuration = in ? 300.0 : 150.0;
        double translateDuration = in ? 150 : 300;
        TranslateTransition translate = new TranslateTransition(Duration.millis(translateDuration), this);
        FadeTransition fade = new FadeTransition(Duration.millis(fadeDuration), this);
        translate.setFromY(fromY);
        translate.setToY(toY);
        fade.setFromValue(from);
        fade.setToValue(to);
        translate.play();
        fade.play();
        if (completion != null)
            fade.setOnFinished(__ -> completion.run());
    }

    public void setTimeout(double timeout) { this.timeout = timeout; }
}