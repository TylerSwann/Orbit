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

import com.jfoenix.controls.JFXButton;
import io.orbit.App;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 01, 2018
 * Time: 4:33 PM
 * Website: https://orbiteditor.com
 */
public class NPMProjectPane extends VBox
{
    protected OptionSet location;
    protected OptionSet nodeInterpreter;
    private JFXButton browseLocation;
    private JFXButton browseNode;
    private String defaultLocation;

    public NPMProjectPane(String defaultLocation)
    {
        this.defaultLocation = defaultLocation;
        this.browseLocation = new JFXButton("...");
        this.browseNode = new JFXButton("...");
        this.browseLocation.getStyleClass().add("button-secondary");
        this.browseNode.getStyleClass().add("button-secondary");
        this.location = OptionSet.textfield("Location:", defaultLocation);
        String nodeLocation = nodeInstallLocation();
        this.nodeInterpreter = OptionSet.combobox("Node Interpreter:", Collections.singletonList(nodeLocation));
        if (nodeLocation != null)
            this.nodeInterpreter.getComboBox().setValue(nodeLocation);
        this.location.getChildren().add(this.browseLocation);
        this.nodeInterpreter.getChildren().add(this.browseNode);
        this.browseLocation.setOnAction(__ -> this.browseFolder());
        this.browseNode.setOnAction(__ -> this.browseFile());
        this.getChildren().addAll(this.location, this.nodeInterpreter);
        this.setSpacing(20.0);
    }

    private String nodeInstallLocation()
    {
        String[] paths = System.getenv("PATH").split(Pattern.quote(File.pathSeparator));
        for (String path : paths)
        {
            if (path.contains("nodejs"))
                return String.format("%snode.exe", path);
        }
        return null;
    }

    private void browseFolder()
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(this.defaultLocation));
        chooser.setTitle("Project Location");
        File location = chooser.showDialog(App.stage());
        if (location != null)
            this.location.getTextField().setText(location.getPath());
    }

    private void browseFile()
    {
        FileChooser chooser = new FileChooser();
        String nodeLocation = getNodeLocation();
        if (nodeLocation != null)
            chooser.setInitialDirectory(new File(nodeLocation));
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Executable File", "exe"));
        chooser.setTitle("Node.exe");
        File node = chooser.showOpenDialog(App.stage());
        if (node != null)
            this.nodeInterpreter.getComboBox().setValue(node.getPath());
    }

    public String getLocation() { return this.location.getTextField().getText(); }
    public String getNodeLocation() { return this.nodeInterpreter.getComboBox().getSelectionModel().getSelectedItem(); }
}

