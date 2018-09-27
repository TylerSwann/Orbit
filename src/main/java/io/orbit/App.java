
package io.orbit;

import io.orbit.api.OrbitApplication;
import io.orbit.controllers.OSplashPageController;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.EventProperty;
import io.orbit.settings.Directory;
import io.orbit.settings.LocalUser;
import io.orbit.settings.UserSetting;
import io.orbit.util.JSON;
import io.orbit.util.SerializableFont;
import io.orbit.util.SyncedObservableList;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by Tyler Swann on Friday January 05, 2018 at 16:14
 */
public class App extends Application
{
    public static void main(String[] args){ launch(args); }
    private static ApplicationController controller;
    public static ApplicationController controller() { return controller; }
    public static final EventProperty<ApplicationEvent> appEventsProperty = new EventProperty<>();
    public static SyncedObservableList<String> appTheme = new SyncedObservableList<>();
    public static SyncedObservableList<String> syntaxTheme = new SyncedObservableList<>();
    public static SimpleStringProperty editorFontStyle = new SimpleStringProperty();
    public static ReadOnlyObjectProperty<Stage> PRIMARY_STAGE;

    public void start(Stage stage)
    {
        System.setProperty("prism.lcdtext", "false");
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
        {
            PRIMARY_STAGE = new SimpleObjectProperty<>(stage);
            ((SimpleObjectProperty<Stage>)OrbitApplication.PRIMARY_STAGE).set(stage);
        }
        OSplashPageController.show();
        App.setApplicationTheme(new File(getClass().getClassLoader().getResource("css/MaterialDark.css").getFile()));
        App.setSyntaxTheme(new File(getClass().getClassLoader().getResource("css/MaterialDarkSyntax.css").getFile()));
//        App.setApplicationTheme(new File(getClass().getClassLoader().getResource("css/MaterialDark.css").getFile()));
//        App.setSyntaxTheme(new File(getClass().getClassLoader().getResource("css/MaterialDarkSyntax.css").getFile()));
//        App.setApplicationTheme(new File(getClass().getClassLoader().getResource("css/SolarOrbit.css").getFile()));
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
            appTheme.append(externalUrl);
            if (userPreferredAppTheme != null)
                appTheme.remove(userPreferredAppTheme);
            userPreferredAppTheme = externalUrl;
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
