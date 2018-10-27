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
package io.orbit.ui.colorpicker;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.util.Duration;

/**
 * Created by Tyler Swann on Monday September 03, 2018 at 14:13
 */
public class SLColorChooser extends ChooserPane
{
    private static final String DEFAULT_STYLE_CLASS = "sl-color-chooser";
    private static final double maxSaturation = 1.0;
    private static final double maxLightness = 1.0;
    private Color base;

    private DoubleProperty saturation = new SimpleDoubleProperty();
    public double getSaturation() { return saturation.get(); }
    public ReadOnlyDoubleProperty saturationProperty() { return saturation; }

    private DoubleProperty lightness = new SimpleDoubleProperty();
    public double getLightness() { return lightness.get(); }
    public ReadOnlyDoubleProperty lightnessProperty() { return lightness; }

    public SLColorChooser(Color base)
    {
        super(true, true);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.getKnob().getStyleClass().add("sl-chooser-knob");
        this.build();
        setBase(base);
    }

    private void build()
    {
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, __ -> scaleKnob(true));
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, __ -> scaleKnob(false));
        this.getKnob().layoutXProperty().addListener(__ -> {
            double saturation = (this.getKnob().getLayoutX() / maxLayoutX()) * maxSaturation;
            saturation = saturation > 1.0 ? 1.0 : saturation;
            saturation = saturation > 1.0 || saturation < 0.0 ?
                         saturation > 1.0 ? 1.0 : 0.0 : saturation;
            this.saturation.setValue(saturation);
        });
        this.getKnob().layoutYProperty().addListener(__ -> {
            double lightness = (this.getKnob().getLayoutY() / maxLayoutY()) * maxLightness;
            lightness = Math.abs(lightness - 1.0);
            lightness = lightness > 1.0 || lightness < 0.0 ?
                        lightness > 1.0 ? 1.0 : 0.0 : lightness;
            this.lightness.set(lightness);
        });
    }

    private void scaleKnob(boolean in)
    {
        double to = in ? 1.4 : 1.0;
        double from = in ? 1.0 : 1.4;
        ScaleTransition scale = new ScaleTransition(Duration.millis(80.0), this.getKnob());
        scale.setFromY(from);
        scale.setFromX(from);
        scale.setToY(to);
        scale.setToX(to);
        scale.play();
    }

    private void updateValue(double x, double y)
    {
        double realX = Double.isNaN(x) ? 0.0 : x;
        double realY = Double.isNaN(y) ? 0.0 : y;
        Runnable applyValues = () -> {
            this.saturation.set(realX);
            this.lightness.set(realY);
            this.getKnob().relocate((realX * maxLayoutX()), (realY * maxLayoutY()));
        };
        if (this.getWidth() <= 0.0)
        {
            InvalidationListener loaded = new InvalidationListener() {
                @Override
                public void invalidated(Observable observable)
                {
                    Platform.runLater(() -> {
                        applyValues.run();
                        widthProperty().removeListener(this);
                    });
                }
            };
            this.widthProperty().addListener(loaded);
        }
        else
            applyValues.run();
    }

    public Color getBase() { return base; }
    public void setBase(Color base)
    {
        this.base = base;
        BackgroundFill primary = new BackgroundFill(base, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill secondary = new BackgroundFill(LinearGradient.valueOf("linear-gradient(to right, white 0%, rgba(255, 255, 255, 0) 100%)"), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill tertiary = new BackgroundFill(LinearGradient.valueOf("linear-gradient(to bottom, transparent 0%, black 100%)"), CornerRadii.EMPTY, Insets.EMPTY);
        this.setBackground(new Background(primary, secondary, tertiary));
    }
    private double maxLayoutX() { return (this.getWidth() - this.getKnob().getWidth()); }
    private double maxLayoutY() { return (this.getHeight() - this.getKnob().getHeight()); }
}
