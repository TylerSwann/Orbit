package io.orbit.controllers;

import io.orbit.App;
import io.orbit.ApplicationController;
import io.orbit.Themes;
import io.orbit.settings.Directory;
import io.orbit.settings.LocalUser;
import io.orbit.settings.UserSetting;
import io.orbit.util.JSON;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.URL;

public class AppStage
{
    private static Stage stage;
    public static Stage stage() { return stage; }
    private static ApplicationController controller;
    public static ApplicationController controller() { return controller; }
    private AppStage() {}

    public static void build(Stage stage)
    {
        AppStage.stage = stage;
        Themes.load();
        loadSettings();
        applySettings();
        applyStyles();
    }

    private static void loadSettings()
    {
        Directory.checkDefaultDirectories();
        if (!Directory.USER_SETTINGS.exists())
            LocalUser.settings = UserSetting.DEFAULT_SETTINGS;
        else
        {

            UserSetting userSetting = JSON.loadFromFile(UserSetting.class, Directory.USER_SETTINGS);
            LocalUser.settings = userSetting;
            if (userSetting.getLastModifiedProject() != null)
                LocalUser.project = userSetting.getLastModifiedProject();
        }
    }

    private static void applySettings()
    {
        AnchorPane root = null;
        double width = LocalUser.settings.getWindowSize().getWidth();
        double height = LocalUser.settings.getWindowSize().getHeight();
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        if (width >= screenSize.getWidth() && height >= screenSize.getHeight())
            stage.setMaximized(true);
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
        Themes.sync(scene);
        stage.setScene(scene);
    }

    private static void applyStyles()
    {
        InputStream taskBarIcon = AppStage.class.getClassLoader().getResourceAsStream("images/orbit_taskbar_icon.png");
        URL defaultCss = AppStage.class.getClassLoader().getResource("css/Default.css");
        URL defaultSyntax = AppStage.class.getClassLoader().getResource("css/DefaultSyntax.css");
        assert defaultCss != null && defaultSyntax != null && taskBarIcon != null;
        stage.getIcons().add(new Image(taskBarIcon));
//        App.appTheme.append(defaultCss.toExternalForm());
//        App.syntaxTheme.append(defaultSyntax.toExternalForm());
//        App.appTheme.sync(stage().getScene().getStylesheets());
        LocalUser.applySettings();
    }
}
