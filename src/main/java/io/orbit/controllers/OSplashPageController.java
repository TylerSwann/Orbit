package io.orbit.controllers;

import com.jfoenix.controls.JFXProgressBar;
import io.orbit.App;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Tyler Swann on Friday April 06, 2018 at 11:10
 */
public class OSplashPageController
{
    public JFXProgressBar progressBar;
    public Text statusLabel;

    private static Scene scene;
    private static Stage stage;
    private static OSplashPageController controller;
    public ImageView imageView;

    public static void show()
    {
        if (scene == null)
        {
            AnchorPane root;
            URL splashPageURL = App.class.getClassLoader().getResource("views/SplashPage.fxml");

            assert splashPageURL != null;
            FXMLLoader loader = new FXMLLoader(splashPageURL);
            try {  root = loader.load();  }
            catch (IOException ex) { throw new RuntimeException("Couldn't load SplashPage.fxml from file..."); }
            controller = loader.getController();
            scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(scene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - scene.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - scene.getHeight()) / 2);
        }
        stage.show();
        Platform.runLater(() -> controller.statusLabel.setText(""));
    }

    public static void updateProgress(double progress, String message)
    {
        controller.progressBar.setProgress(progress);
        controller.statusLabel.setText(message);
    }

    public static void close()
    {
        stage.close();
    }

    public void initialize() { }
}
