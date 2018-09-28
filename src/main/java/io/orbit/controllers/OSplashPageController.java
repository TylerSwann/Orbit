package io.orbit.controllers;

import com.jfoenix.controls.JFXProgressBar;
import io.orbit.App;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

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

    public static void show(Runnable completion)
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
        playAnimation(completion);
    }

    public static void updateProgress(double progress, String message)
    {
        controller.progressBar.setProgress(progress);
        controller.statusLabel.setText(message);
    }

    private static void playAnimation(Runnable completion)
    {
        PauseTransition part1 = new PauseTransition();
        PauseTransition part2 = new PauseTransition();
        PauseTransition part3 = new PauseTransition();
        PauseTransition part4 = new PauseTransition();
        PauseTransition part5 = new PauseTransition();
        PauseTransition[] transitions = new PauseTransition[] {
                part1,
                part2,
                part3,
                part4,
                part5
        };
        Arrays.stream(transitions).forEach(it -> it.setDuration(Duration.millis(100)));
        part1.setOnFinished(event1 -> {
            Platform.runLater(() -> OSplashPageController.updateProgress(0.2, "Performing initial setup..."));
            part2.setOnFinished(event2 -> {
                Platform.runLater(() -> OSplashPageController.updateProgress(0.4, "Loading Application Files..."));
                part3.setOnFinished(event3 -> {
                    Platform.runLater(() -> OSplashPageController.updateProgress(0.6, "Loading User Settings..."));
                    part4.setOnFinished(event4 -> {
                        Platform.runLater(() -> OSplashPageController.updateProgress(0.8, "Applying User Settings..."));
                        part5.setOnFinished(event5 -> {
                            Platform.runLater(() -> OSplashPageController.updateProgress(1.0, "Done"));
                            OSplashPageController.close();
                            completion.run();
                            App.stage().show();
                        });
                        part5.play();
                    });
                    part4.play();
                });
                part3.play();
            });
            part2.play();
        });
        part1.play();
    }

    public static void close()
    {
        stage.close();
    }

    public void initialize() { }
}
