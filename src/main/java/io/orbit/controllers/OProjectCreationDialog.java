package io.orbit.controllers;

import io.orbit.App;
import io.orbit.settings.Directory;
import io.orbit.api.Project;
import io.orbit.api.SVGIcon;
import io.orbit.ui.MUIMenuButton;
import io.orbit.ui.MUIStepDialog;
import io.orbit.ui.NewProjectStep;
import io.orbit.webtools.WebProject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Tyler Swann on Friday May 04, 2018 at 13:04
 */
public class OProjectCreationDialog
{
    @FXML
    private VBox projectTypesContainer;
    @FXML
    private AnchorPane projectDialogContainer;
    private Project project;
    private final Project[] projects = new Project[]{  new WebProject()  };
    private String projectName;
    private File projectLocation;
    private MUIStepDialog stepDialog;

    public static void show()
    {
        URL projectCreationFxml = App.class.getClassLoader().getResource("views/ProjectCreationDialog.fxml");
        assert projectCreationFxml != null;
        try
        {
            AnchorPane root = FXMLLoader.load(projectCreationFxml);
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            App.appTheme.sync(scene.getStylesheets());
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setScene(scene);
            stage.setTitle("New Project");
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex)
        {
            System.out.println("ERROR loading ProjectCreationDialog.fxml");
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        this.stepDialog = new MUIStepDialog();
        AnchorPane.setTopAnchor(this.stepDialog, 0.0);
        AnchorPane.setBottomAnchor(this.stepDialog, 10.0);
        AnchorPane.setLeftAnchor(this.stepDialog, 0.0);
        AnchorPane.setRightAnchor(this.stepDialog, 0.0);
        for (Project project : this.projects)
        {
            HBox container = new HBox();
            container.setAlignment(Pos.CENTER);
            Label label = new Label(project.getName());
            label.setFont(new Font("Roboto Light", 14.0));
            label.setMinWidth(200.0);
            SVGIcon icon = project.getIcon();
            container.setSpacing(15.0);
            MUIMenuButton button = new MUIMenuButton("");
            button.getStyleClass().add("project-type");
            button.setText(null);
            button.setPrefHeight(40.0);
            container.setPrefHeight(40.0);
            if (icon != null)
                container.getChildren().add(icon);
            container.getChildren().add(label);
            button.setGraphic(container);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.project = project;
                Pane[] creationSteps = this.project.getProjectCreationSteps();
                Pane[] steps = creationSteps == null || creationSteps.length <= 0 ? new Pane[1] : new Pane[creationSteps.length + 1];
                NewProjectStep stepOne = new NewProjectStep(Directory.ORBIT_PROJECTS);
                steps[0] = stepOne;
                stepOne.setOnValueChanged((name, loc) -> {
                    this.projectName = name;
                    this.projectLocation = loc;
                });
                if (steps.length > 1)
                {
                    for (int i = 0; i < project.getProjectCreationSteps().length; i++)
                        steps[i + 1] = project.getProjectCreationSteps()[i];
                }
                this.stepDialog.setSteps(steps);
            });
            this.projectTypesContainer.getChildren().add(button);
        }
        this.projectDialogContainer.getChildren().add(this.stepDialog);
    }
}
