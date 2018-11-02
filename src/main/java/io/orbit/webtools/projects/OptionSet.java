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
package io.orbit.webtools.projects;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 01, 2018
 * Time: 4:41 PM
 * Website: https://orbiteditor.com
 */
public class OptionSet extends HBox
{
    private Label label;
    private JFXTextField textField;
    private JFXComboBox<String> comboBox;

    static OptionSet textfield(String text, String prompt) { return new OptionSet(text, prompt); }
    static OptionSet combobox(String text, List<String> options) { return new OptionSet(text, options); }

    private OptionSet(String text, String prompt)
    {
        this.label = new Label(text);
        this.textField = new JFXTextField(prompt);
        this.textField.getStyleClass().add("mui-text-field");
        this.getChildren().addAll(this.label, this.textField);
        this.init();
    }

    private OptionSet(String text, List<String> options)
    {
        this.label = new Label(text);
        this.comboBox = new JFXComboBox<>(FXCollections.observableArrayList(options));
        this.getChildren().addAll(this.label, this.comboBox);
        this.init();
    }

    private void init()
    {
        this.label.setPrefWidth(150.0);
        this.setAlignment(Pos.BOTTOM_LEFT);
        this.setSpacing(20.0);
        this.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));
        this.widthProperty().addListener(__ -> {
            if (this.textField != null)
                this.textField.setPrefWidth(this.getWidth() / 1.7);
            if (this.comboBox != null)
                this.comboBox.setPrefWidth(this.getWidth() / 1.7);
        });
    }

    public Label getLabel() { return label; }
    public JFXTextField getTextField() { return textField; }
    public JFXComboBox<String> getComboBox() { return comboBox; }
}