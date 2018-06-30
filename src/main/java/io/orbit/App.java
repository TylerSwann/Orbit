package io.orbit;

import io.orbit.controllers.OSplashPageController;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.EventProperty;
import io.orbit.settings.Directory;
import io.orbit.settings.LocalUser;
import io.orbit.settings.UserSetting;
import io.orbit.util.JSON;
import io.orbit.util.SerializableFont;
import io.orbit.util.SyncedObservableList;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Tyler Swann on Friday January 05, 2018 at 16:14
 */
public class App extends Application
{
    public static void main(String[] args){ launch(args); }
    private static ApplicationController controller;
    public static ApplicationController applicationController() { return controller; }
    public static final EventProperty<ApplicationEvent> appEventsProperty = new EventProperty<>();
    public static SyncedObservableList<String> appTheme = new SyncedObservableList<>();
    public static SyncedObservableList<String> syntaxTheme = new SyncedObservableList<>();
    public static SimpleStringProperty editorFontStyle = new SimpleStringProperty();
    public static ReadOnlyObjectProperty<Stage> PRIMARY_STAGE;

    /*
    * API
    *
    *   PluginController:
    *       provides:
    *          Supported File Types
    *          User Defined UI Elements
    *          EditorController:
    *             provides:
    *                Functionality to an editor
    *             receives:
    *                Is notified when a new editor is created and the file that coincides with the editor
    *          LanguageDelegate:
    *             provides:
    *                SyntaxHighlighter
    *                File type of supported language
    *             receives:
    *                None
    *         receives:
    *            Editor Specific Events
    *            Minor Application Events
    * */

    // TODO - restructure api to be better

    public void start(Stage stage)
    {
        stage.setTitle("Orbit");
        performInitialSetup();
        StatusLogger.getLogger().setLevel(Level.OFF);
        Platform.setImplicitExit(false);
        double width = LocalUser.userSettings.getWindowSize().getWidth();
        double height = LocalUser.userSettings.getWindowSize().getHeight();
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        if (width >= screenSize.getWidth() && height >= screenSize.getHeight())
            stage.setMaximized(true);
        AnchorPane root = new AnchorPane();
        InputStream taskbarIcon = getClass().getClassLoader().getResourceAsStream("images/orbit_taskbar_icon.png");
        URL defaultCss = getClass().getClassLoader().getResource("css/Default.css");
        URL defaultSyntax = getClass().getClassLoader().getResource("css/DefaultSyntax.css");
        assert defaultCss != null && defaultSyntax != null && taskbarIcon != null;
        stage.getIcons().add(new Image(taskbarIcon));
        appTheme.append(defaultCss.toExternalForm());
        syntaxTheme.append(defaultSyntax.toExternalForm());
        try
        {
            FXMLLoader loader = new FXMLLoader();
            URL url = App.class.getClassLoader().getResource("Editor.fxml");
            loader.setLocation(url);
            if (url != null)
                root = loader.load();
            controller = loader.getController();
            controller.open();
        }
        catch (Exception ex){ex.printStackTrace();}
        stage.setWidth(width);
        stage.setHeight(height);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        appTheme.sync(scene.getStylesheets());
        LocalUser.applySettings();
        stage.setOnCloseRequest(event -> appEventsProperty.fire(new ApplicationEvent(ApplicationEvent.WILL_CLOSE)));
        if (PRIMARY_STAGE == null)
            PRIMARY_STAGE = new SimpleObjectProperty<>(stage);
        showSplashScreen();
//        App.setApplicationTheme(new File(getClass().getClassLoader().getResource("css/SolarOrbit.css").getFile()));
    }



    private void showSplashScreen()
    {
        OSplashPageController.show();
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
                            appEventsProperty.fire(new ApplicationEvent(ApplicationEvent.WILL_LOAD));
                            PRIMARY_STAGE.get().show();
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


    private void performInitialSetup()
    {
        Directory.checkDefaultDirectories();
        if (!Directory.USER_SETTINGS.exists())
            LocalUser.userSettings = UserSetting.DEFAULT_SETTINGS;
        else
        {

            UserSetting userSetting = JSON.loadFromFile(UserSetting.class, Directory.USER_SETTINGS);
            LocalUser.userSettings = userSetting;
            if (userSetting.getLastModifiedProject() != null)
                LocalUser.project = userSetting.getLastModifiedProject();
        }
    }

    private static String userPreferredAppTheme;
    public static void setApplicationTheme(File file)
    {
        try
        {
            URL url = Paths.get(file.getPath()).toUri().toURL();
            String externalUrl = url.toExternalForm();
            if (userPreferredAppTheme != null)
                appTheme.remove(userPreferredAppTheme);
            userPreferredAppTheme = externalUrl;
            appTheme.append(userPreferredAppTheme);
        }
        catch (MalformedURLException ex)
        {
            System.out.println(String.format("ERROR setting application theme from file %s", file.getPath()));
            ex.printStackTrace();
        }
    }
    private static String userPreferredSyntaxTheme;
    public static void setSyntaxTheme(File file)
    {
        try
        {
            URL url = Paths.get(file.getPath()).toUri().toURL();
            String externalUrl = url.toExternalForm();
            if (userPreferredSyntaxTheme != null)
                syntaxTheme.remove(userPreferredSyntaxTheme);
            userPreferredSyntaxTheme = externalUrl;
            syntaxTheme.append(userPreferredSyntaxTheme);
        }
        catch (MalformedURLException ex)
        {
            System.out.println(String.format("ERROR setting application theme from file %s", file.getPath()));
            ex.printStackTrace();
        }
    }
    public static void setEditorFont(SerializableFont font)
    {
        String style = String.format("-fx-font-family: '%s', monospaced;\n-fx-font-size: %f", font.getFamily(), font.getSize());
        editorFontStyle.setValue(style);
    }
}
