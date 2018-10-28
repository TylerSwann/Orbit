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
import com.jfoenix.controls.JFXSpinner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.HBox;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 27, 2018
 * Time: 9:40 AM
 * Website: https://orbiteditor.com
 */
public class MUIProgressPane extends PopupControl
{
    private static final String DEFAULT_STYLE_CLASS = "mui-progress-pane";

    private HBox container;
    private Label label;
    private JFXSpinner spinner;
    private JFXButton cancel;
    private Runnable onCancel;

    public MUIProgressPane(String message)
    {
        this.container = new HBox();
        this.label = new Label(message);
        this.spinner = new JFXSpinner();
        this.cancel = new JFXButton("CANCEL");
        this.container.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.build();
    }

    private void build()
    {
        this.container.setMinSize(450.0, 70.0);
        this.spinner.setRadius(30.0);
        this.container.setAlignment(Pos.CENTER);
        this.container.setPrefHeight(40.0);
        this.container.setPadding(new Insets(20.0));
        this.container.setSpacing(50.0);
        this.label.setWrapText(true);
        this.cancel.setDisableVisualFocus(true);
        this.cancel.setOnAction(__ -> {
            if (this.onCancel != null)
                this.onCancel.run();
        });

        this.container.getChildren().addAll(this.spinner, this.label, this.cancel);
        this.getScene().setRoot(this.container);
    }
    public void setOnCancel(Runnable onCancel) { this.onCancel = onCancel; }
    public Runnable getOnCancel() { return this.onCancel; }
}