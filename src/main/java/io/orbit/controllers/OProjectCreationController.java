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
package io.orbit.controllers;

import com.jfoenix.controls.JFXButton;
import io.orbit.App;
import io.orbit.Themes;
import io.orbit.api.language.Project;
import io.orbit.plugin.PluginDispatch;
import io.orbit.ui.treeview.MUITreeItem;
import io.orbit.ui.treeview.MUITreeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created By: Tyler Swann.
 * Date: Sunday, Oct 28, 2018
 * Time: 2:12 PM
 * Website: https://orbiteditor.com
 */
public class OProjectCreationController
{

    public static void showDialog()
    {
        URL fxmlURL = App.class.getClassLoader().getResource("views/ProjectCreation.fxml");
        assert fxmlURL != null;
        try
        {
            AnchorPane root = FXMLLoader.load(fxmlURL);
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            Themes.sync(scene);
            Stage stage = new Stage();
            stage.getIcons().clear();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private @FXML AnchorPane projectTypesPane;
    private @FXML JFXButton createButton;
    private @FXML AnchorPane formContainer;
    private MUITreeView<Project> treeView;
    private Map<Project, Pane> forms = new HashMap<>();
    private Project currentProject;

    public void initialize()
    {
        List<MUITreeItem<Project>> projectTypes = new ArrayList<>();
        for (Project project : PluginDispatch.getProjectTypes())
        {
            MUITreeItem<Project> item = new MUITreeItem<>(project, project.getIcon());
            projectTypes.add(item);
        }
        this.currentProject = projectTypes.get(0).getValue();
        this.treeView = new MUITreeView<>();
        this.treeView.getBranches().addAll(projectTypes);
        this.treeView.setCellFactory(Project::getName);
        AnchorPane.setTopAnchor(this.treeView, 50.0);
        AnchorPane.setBottomAnchor(this.treeView, 0.0);
        AnchorPane.setLeftAnchor(this.treeView, 0.0);
        AnchorPane.setRightAnchor(this.treeView, 0.0);
        this.build();
    }

    private void build()
    {
        this.projectTypesPane.getChildren().add(this.treeView);
        this.setProject(this.currentProject);

        this.treeView.selectedBranchProperty().addListener((obs, oldV, newV) -> this.setProject(newV.getValue()));
    }

    private void setProject(Project project)
    {
        if (this.currentProject != null)
            this.formContainer.getChildren().remove(this.forms.get(this.currentProject));
        this.currentProject = project;
        if (this.forms.get(project) != null)
        {
            this.formContainer.getChildren().add(this.forms.get(project));
            return;
        }
        Pane form = this.currentProject.getProjectCreationPane();
        if (form != null)
        {
            AnchorPane.setTopAnchor(form, 50.0);
            AnchorPane.setBottomAnchor(form, 60.0);
            AnchorPane.setLeftAnchor(form, 20.0);
            AnchorPane.setRightAnchor(form, 0.0);
            this.forms.put(project, form);
            this.formContainer.getChildren().add(form);
        }
    }
}