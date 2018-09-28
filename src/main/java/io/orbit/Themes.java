package io.orbit;

import io.orbit.api.text.CodeEditor;
import io.orbit.controllers.AppStage;
import io.orbit.util.SerializableFont;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

public class Themes
{
    private static String defaultAppTheme;
    private static String defaultSyntaxTheme;
    private static final ObjectProperty<String> appTheme = new SimpleObjectProperty<>();
    private static final ObjectProperty<String> syntaxTheme = new SimpleObjectProperty<>();
    private static final ObjectProperty<String> editorFontStyle = new SimpleObjectProperty<>();
    private static final ObservableList<Scene> scenes = FXCollections.observableArrayList();
    private static final ObservableList<CodeEditor> editors = FXCollections.observableArrayList();

    public static final File MATERIAL_DARK;
    public static final File MATERIAL_LIGHT;
    public static final File MATERIAL_DARK_SYNTAX;
    public static final File MATERIAL_LIGHT_SYNTAX;

    static {
        URL darkURL = Themes.class.getClassLoader().getResource("css/app_themes/MaterialDark.css");
        URL lightURL = Themes.class.getClassLoader().getResource("css/app_themes/MaterialLight.css");
        URL darkSyntaxURL = Themes.class.getClassLoader().getResource("css/syntax_themes/MaterialDarkSyntax.css");
        URL lightSyntaxURL = Themes.class.getClassLoader().getResource("css/syntax_themes/MaterialLightSyntax.css");
        assert darkURL != null && lightURL != null && darkSyntaxURL != null && lightSyntaxURL != null;
        MATERIAL_DARK = new File(darkURL.getFile());
        MATERIAL_LIGHT = new File(lightURL.getFile());
        MATERIAL_DARK_SYNTAX = new File(darkSyntaxURL.getFile());
        MATERIAL_LIGHT_SYNTAX = new File(lightSyntaxURL.getFile());
    }

    private Themes() {}

    public static void load()
    {
        URL defaultCss = AppStage.class.getClassLoader().getResource("css/Default.css");
        URL defaultSyntax = AppStage.class.getClassLoader().getResource("css/DefaultSyntax.css");
        assert defaultCss != null && defaultSyntax != null;
        defaultAppTheme = defaultCss.toExternalForm();
        defaultSyntaxTheme = defaultSyntax.toExternalForm();
    }

    public static void sync(Scene scene)
    {
        scenes.add(scene);
        update(scene);
    }
    public static void sync(Stage stage)
    {
        Objects.requireNonNull(stage.getScene());
        scenes.add(stage.getScene());
        update(stage.getScene());
    }

    public static void sync(CodeEditor editor)
    {
        editors.add(editor);
        update(editor);
    }

    public static void setApplicationTheme(File file)
    {
        try
        {
            URL url = Paths.get(file.getPath()).toUri().toURL();
            String externalForm = url.toExternalForm();
            appTheme.setValue(externalForm);
            update();
        }
        catch (MalformedURLException ex)
        {
            System.out.println(String.format("ERROR setting application theme from file %s", file.getPath()));
            ex.printStackTrace();
        }
    }
    public static void setSyntaxTheme(File file)
    {
        try
        {
            URL url = Paths.get(file.getPath()).toUri().toURL();
            String externalForm = url.toExternalForm();
            syntaxTheme.setValue(externalForm);
            update();
        }
        catch (MalformedURLException ex)
        {
            System.out.println(String.format("ERROR setting syntax theme from file %s", file.getPath()));
            ex.printStackTrace();
        }
    }
    public static void setEditorFont(SerializableFont font)
    {
        String style = String.format("-fx-font-family: '%s', monospaced;\n-fx-font-size: %f", font.getFamily(), font.getSize());
        editorFontStyle.setValue(style);
        update();
    }

    private static void update()
    {
        editors.forEach(Themes::update);
        scenes.forEach(Themes::update);
    }

    private static void update(CodeEditor editor)
    {
        editor.getStylesheets().setAll(defaultSyntaxTheme, syntaxTheme.get());
        editor.setStyle(editorFontStyle.getValue());
    }

    private static void update(Scene scene)
    {
        scene.getStylesheets().setAll(defaultAppTheme, appTheme.get());
    }
}
