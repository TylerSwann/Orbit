package io.orbit;

import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.settings.LocalUser;
import io.orbit.settings.UserSetting;
import io.orbit.util.JSON;
import javafx.application.Platform;

import javax.swing.JFileChooser;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 17:22
 */
public class Directory
{
    public static final File ORBIT_PROJECTS = new File(String.format("%s\\OrbitProjects", new JFileChooser().getFileSystemView().getDefaultDirectory().getPath()));
    public static final File APPLICATION_ROOT = new File(String.format("%s\\.Orbit", System.getProperty("user.home")));
    public static final File PLUGINS_FOLDER = new File(String.format("%s\\plugins", APPLICATION_ROOT));
    public static final File USER_SETTINGS = new File(String.format("%s\\user_settings.json", APPLICATION_ROOT));
    public static final File THEMES = new File(String.format("%s\\themes", APPLICATION_ROOT));
    public static final File SYNTAX_THEMES_FOLDER = new File(String.format("%s\\themes\\syntax", APPLICATION_ROOT));
    public static final File APP_THEMES_FOLDER = new File(String.format("%s\\themes\\application", APPLICATION_ROOT));
    public static final File DEFAULT_THEME = new File(String.format("%s\\Default.css", APP_THEMES_FOLDER.getPath()));
    public static final File DEFAULT_SYNTAX_THEME = new File(String.format("%s\\DefaultSyntax.css", SYNTAX_THEMES_FOLDER.getPath()));
    public static final File FONTS_FOLDER = new File(String.format("%s\\fonts", THEMES));

    private static boolean hasInitialized = false;

    /**
     * Checks to see if the required directories have been created
     */
    public static void checkDefaultDirectories()
    {
        if (!hasInitialized)
            App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> Platform.runLater(Directory::saveUserData));
        hasInitialized = true;
        File[] directories = new File[]{
                ORBIT_PROJECTS,
                APPLICATION_ROOT,
                PLUGINS_FOLDER,
                THEMES,
                SYNTAX_THEMES_FOLDER,
                APP_THEMES_FOLDER,
                FONTS_FOLDER
        };
        for (File directory : directories)
        {
            if (!directory.exists() || !directory.isDirectory())
            {
                boolean created = directory.mkdir();
                if (!created)
                    throw new RuntimeException(String.format("Unable to create required directory at %s", directory.getPath()));
            }
        }
        File[] themeFiles = APP_THEMES_FOLDER.listFiles();
        File[] syntaxFiles = SYNTAX_THEMES_FOLDER.listFiles();
        File[] fontFiles = FONTS_FOLDER.listFiles();
        if (themeFiles == null || themeFiles.length <= 0)
        {
            List<URL> appThemeUrls = new ArrayList<>(Arrays.asList(
                    App.class.getClassLoader().getResource("css/Default.css"),
                    App.class.getClassLoader().getResource("css/MaterialDark.css")));
            for (URL stylesheetUrl : appThemeUrls)
                copyFilesToFolder(stylesheetUrl, APP_THEMES_FOLDER);
        }
        if (syntaxFiles == null || syntaxFiles.length <= 0)
        {
            List<URL> syntaxThemeUrls = new ArrayList<>(Arrays.asList(
                    App.class.getClassLoader().getResource("css/MaterialDarkSyntax.css"),
                    App.class.getClassLoader().getResource("css/DefaultSyntax.css")));
            for (URL stylesheetUrl : syntaxThemeUrls)
                copyFilesToFolder(stylesheetUrl, SYNTAX_THEMES_FOLDER);
        }

        if (fontFiles == null || fontFiles.length <= 0)
        {
            URL fontResourceFolder = App.class.getClassLoader().getResource("fonts");
            assert fontResourceFolder != null;
            File[] fontResourceFiles = new File(fontResourceFolder.getFile()).listFiles();
            if (fontResourceFiles != null)
                for (File font : fontResourceFiles)
                    copyFilesToFolder(font, FONTS_FOLDER);
        }
        if (!USER_SETTINGS.exists())
            JSON.writeToFile(UserSetting.DEFAULT_SETTINGS, USER_SETTINGS);
    }

    private static void saveUserData()
    {
        UserSetting userSettings;
        if (LocalUser.userSettings != null)
        {
            userSettings = LocalUser.userSettings;
            JSON.writeToFile(userSettings, USER_SETTINGS);
        }
    }

    private static void copyFilesToFolder(URL fileToCopy, File destination)
    {
        //File source = new File(fileToCopy.getFile());
        copyFilesToFolder(new File(fileToCopy.getFile()), destination);
//        String name = source.getName();
//        if (!destination.isDirectory())
//        {
//            System.out.println(String.format("File located at %s is not a directory", destination.getPath()));
//            return;
//        }
//        try
//        {
//            byte[] fileData = Files.readAllBytes(Paths.get(source.getPath()));
//            Files.write(Paths.get(String.format("%s\\%s", destination.getPath(), name)), fileData);
//        }
//        catch (Exception ex)
//        {
//            System.out.println(String.format("ERROR saving file, %s, to folder %s", name,  destination.getPath()));
//            ex.printStackTrace();
//        }
    }

    private static void copyFilesToFolder(File source, File destination)
    {
        String name = source.getName();
        if (!destination.isDirectory())
        {
            System.out.println(String.format("File located at %s is not a directory", destination.getPath()));
            return;
        }
        try
        {
            byte[] fileData = Files.readAllBytes(Paths.get(source.getPath()));
            Files.write(Paths.get(String.format("%s\\%s", destination.getPath(), name)), fileData);
        }
        catch (Exception ex)
        {
            System.out.println(String.format("ERROR saving file, %s, to folder %s", name,  destination.getPath()));
            ex.printStackTrace();
        }
    }

    private Directory() { }
}