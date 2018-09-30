
package io.orbit;

import io.orbit.controllers.AppStage;
import io.orbit.controllers.OSplashPageController;
import io.orbit.plugin.PluginDispatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.ArrayList;

/**
 * Created by Tyler Swann on Friday January 05, 2018 at 16:14
 */
public class App extends Application
{
    public static void main(String[] args){ launch(args); }

    private static final ArrayList<Runnable> onCloseHandlers = new ArrayList<>();
    private static final ArrayList<Runnable> willLoadHandlers = new ArrayList<>();


    public static ApplicationController controller() { return AppStage.controller(); }
    public static Stage stage() { return AppStage.stage(); }

    public void start(Stage stage)
    {
        System.setProperty("prism.lcdtext", "false");
        stage.setTitle("Orbit");
        StatusLogger.getLogger().setLevel(Level.OFF);
        Platform.setImplicitExit(false);
        PluginDispatch.open();
        AppStage.build(stage);
        stage.setOnCloseRequest(event -> {
            onCloseHandlers.forEach(Runnable::run);
            Platform.runLater(onCloseHandlers::clear);
        });
        OSplashPageController.show(() -> {
            App.willLoadHandlers.forEach(Runnable::run);
            Platform.runLater(willLoadHandlers::clear);
        });
    }

    public static void addOnCloseHandler(Runnable action) { App.onCloseHandlers.add(action); }
    public static void addWillLoadHandler(Runnable action) { App.willLoadHandlers.add(action); }
}
