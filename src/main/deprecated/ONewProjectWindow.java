package io.orbit.controllers;

import io.orbit.App;
import io.orbit.Directory;
import io.orbit.api.Project;
import io.orbit.api.SVGIcon;
import io.orbit.controllers.events.menubar.MenuBarFileEvent;
import io.orbit.ui.MUIMenuButton;
import io.orbit.ui.MUIStepDialog;
import io.orbit.ui.MUIStepDialogEvent;
import io.orbit.ui.NewProjectStep;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

/**
 * Created by Tyler Swann on Wednesday February 07, 2018 at 15:02
 */
@Deprecated
public class ONewProjectWindow
{
    public AnchorPane projectDialogContainer;
    private VBox projectTypesContainer;
    private ScrollPane scrollPane;
    private MUIStepDialog stepDialog;
    private static Scene scene;
    private static Stage stage;
    private String projectName;
    private File projectLocation;
    private Project project;

    private final Project[] projects = new Project[]{  new JavaConsoleProject()  };

    public static void show()
    {
        if (scene == null)
        {
            double width = 1225;
            double height = 700;
            AnchorPane root = new AnchorPane();
            try
            {
                FXMLLoader loader = new FXMLLoader();
                URL url = ONewProjectWindow.class.getClassLoader().getResource("NewProjectWindow.fxml");
                if (url != null)
                {
                    loader.setLocation(url);
                    root = loader.load();
                }
            }
            catch (Exception ex){  ex.printStackTrace();  }
            root.setPrefSize(width, height);
            root.getStyleClass().add("new-project-container");
            scene = new Scene(root, width, height);
            stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            App.appTheme.sync(scene.getStylesheets());
            //App.bindThemesToStage(stage);
        }
        stage.show();
    }

    public void initialize()
    {
        this.stepDialog = new MUIStepDialog();
        applyAnchors(this.stepDialog, 0.0, 0.0, 254.0, 0.0);
        this.projectDialogContainer.getChildren().add(this.stepDialog);
        this.projectTypesContainer = new VBox();
        this.projectTypesContainer.setAlignment(Pos.CENTER);
        this.projectTypesContainer.setPadding(new Insets(20.0, 0.0, 0.0, 0.0));
        this.projectTypesContainer.setSpacing(5.0);
        this.scrollPane = new ScrollPane(this.projectTypesContainer);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        applyAnchors(this.scrollPane, 0.0, 0.0, 0.0, 970.0);
        applyAnchors(this.projectTypesContainer, 0.0, 0.0, 0.0, 0.0);
        Platform.runLater(() -> this.projectTypesContainer.setPrefWidth(this.scrollPane.getWidth()));
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
            Platform.runLater(() -> container.setPrefWidth(this.scrollPane.getWidth()));
            button.setGraphic(container);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.project = project;
                Pane[] steps = new Pane[project.getDialogSteps().length + 1];
                NewProjectStep stepOne = new NewProjectStep(Directory.ORBIT_PROJECTS);
                steps[0] = stepOne;
                stepOne.setOnValueChanged((name, loc) -> {
                    this.projectName = name;
                    this.projectLocation = loc;
                });
                for (int i = 0; i < project.getDialogSteps().length; i++)
                    steps[i + 1] = project.getDialogSteps()[i];
                this.stepDialog.setSteps(steps);
            });
            this.projectTypesContainer.getChildren().add(button);
        }
        this.projectDialogContainer.getChildren().add(this.scrollPane);
        this.stepDialog.addEventHandler(MUIStepDialogEvent.COMPLETION, event -> {
            //this.projectLocation.mkdir();
            this.project.start(this.projectName, this.projectLocation);
        });
    }
    private void applyAnchors(Node node, double top, double bottom, double left, double right)
    {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
    }

    /********************************
     *                              *
     *      Test Project Types      *
     *                              *
     ********************************/
    class JavaConsoleProject implements Project
    {
        @Override
        public String getName() { return "Java Console Application"; }

        @Override
        public SVGIcon getIcon()
        {
            try
            {
                URL url = getClass().getClassLoader().getResource("icons/java_project_icon.svg");
                if (url != null)
                    return new SVGIcon(url);
            }
            catch (Exception ex) { ex.printStackTrace(); }
            return null;
        }

        @Override
        public void start(String name, File rootDir)
        {

        }
        @Override
        public Pane[] getDialogSteps()
        {
            Pane one = new Pane();
            one.setStyle("-fx-background-color: yellow");
            Pane two = new Pane();
            two.setStyle("-fx-background-color: pink");
            Pane three = new Pane();
            three.setStyle("-fx-background-color: black");
            Pane four = new Pane();
            four.setStyle("-fx-background-color: purple");
            return new Pane[]{ one, two, three, four };
        }
    }
}






















