package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.orbit.util.Tuple;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * Created by Tyler Swann on Thursday March 15, 2018 at 15:04
 */
public class NewProjectStep extends VBox
{
    private BiConsumer<String, File> onValueChanged;
    private JFXTextField projectNameField;
    private JFXTextField projectLocationField;
    private JFXButton browseButton;
    private File defaultProjectLocation;

    public NewProjectStep(File defaultProjectLocation)
    {
        this.defaultProjectLocation = defaultProjectLocation;
        HBox nameContainer = new HBox();
        HBox locationContainer = new HBox();
        HBox[] containers = new HBox[]{ nameContainer, locationContainer };
        Label projectNameLabel = new Label("Project Name: ");
        Label projectLocationLabel = new Label("Project Location: ");
        Label[] labels = new Label[] { projectLocationLabel, projectNameLabel };
        this.projectNameField = new JFXTextField();
        this.projectNameField.getStyleClass().add("mui-text-field");
        this.projectLocationField = new JFXTextField(defaultProjectLocation.getPath());
        this.projectLocationField.getStyleClass().add("mui-text-field");
        this.browseButton = new JFXButton("Browse");
        this.browseButton.getStyleClass().add("button-secondary");
        browseButton.setPrefSize(74.0, 32.0);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20.0);
        this.setPadding(new Insets(0.0, 90.0, 0.0, 90.0));
        Arrays.stream(containers).forEach(hContainer -> {
            hContainer.setPrefSize(750.0, 60.0);
            hContainer.setAlignment(Pos.CENTER_LEFT);
        });
        Arrays.stream(labels).forEach(label -> {
            label.setAlignment(Pos.BOTTOM_LEFT);
            label.setPrefSize(120.0, 36.0);
            label.setFont(new Font("Roboto Medium", 14.0));
        });
        projectNameField.setPrefWidth(600.0);
        projectLocationField.setPrefWidth(600.0);
        projectLocationField.setEditable(false);
        nameContainer.getChildren().addAll(projectNameLabel, projectNameField);
        locationContainer.getChildren().addAll(projectLocationLabel, projectLocationField, browseButton);
        this.getChildren().addAll(nameContainer, locationContainer);
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.projectNameField.textProperty().addListener(event -> {
            if (this.projectLocationField.getText() == null)
                return;
            String projectLocation = String.format("%s\\%s", this.defaultProjectLocation.getPath(), this.projectNameField.getText());
            this.projectLocationField.setText(projectLocation);
        });
        this.projectLocationField.textProperty().addListener(event -> {
            if (this.projectNameField.getText() == null || this.projectLocationField.getText() == null)
                return;
            String projectName = this.projectNameField.getText();
            File projectLocation = new File(this.projectLocationField.getText());
            if (this.onValueChanged != null)
                this.onValueChanged.accept(projectName, projectLocation);
        });
        this.browseButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Project Location");
            File file = chooser.showDialog(this.getScene().getWindow());
            if (file != null)
            {
                this.defaultProjectLocation = file;
                String projectName = "";
                if (this.projectNameField.getText() != null)
                    projectName = this.projectNameField.getText();
                this.projectLocationField.setText(String.format("%s\\%s", file.getPath(), projectName));
            }
        });
    }

    public void setOnValueChanged(BiConsumer<String, File> action)
    {
        this.onValueChanged = action;
    }

    public Tuple<String, File> getValues()
    {
        return new Tuple<>(this.projectNameField.getText(), this.defaultProjectLocation);
    }
}
