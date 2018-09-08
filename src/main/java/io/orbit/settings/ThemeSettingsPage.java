package io.orbit.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import io.orbit.App;
import io.orbit.util.SerializableFont;
import io.orbit.util.Tuple;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 16:38
 */
public class ThemeSettingsPage
{
    private static SerializableFont[] fonts;

    public JFXComboBox<Tuple<String, File>> appThemeBox;
    public JFXComboBox<Tuple<String, SerializableFont>> fontFamilyBox;
    public JFXComboBox<Tuple<String, File>> syntaxThemeBox;

    public JFXButton editAppCssButton;
    public JFXButton editSyntaxCssButton;
    public JFXButton cancelButton;
    public JFXButton saveButton;

    public JFXTextField fontSizeField;
    public JFXSlider fontSizeSlider;

    private Runnable onRequestCancel = () -> {};
    private Runnable onRequestClose = () -> {};
    private Consumer<File> onEditSyntaxTheme = file -> {};
    private Consumer<File> onEditUITheme = file -> {};
    static ThemeSettingsPage CONTROLLER;


    public static VBox load()
    {
        try
        {
            URL themePageURL = ThemeSettingsPage.class.getClassLoader().getResource("ThemeSettingsPage.fxml");
            if (themePageURL != null)
                return FXMLLoader.load(themePageURL);
        }
        catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public void initialize()
    {
        CONTROLLER = this;
        if (fonts == null)
            loadFonts();
        addComboBoxOptions();
        registerFontSizeListeners();
        this.saveButton.setOnAction(this::applySettings);
        this.cancelButton.setOnAction(event -> this.onRequestCancel.run());
        loadUserSettings();
        this.editAppCssButton.setOnAction( event -> this.onEditUITheme.accept(this.appThemeBox.getValue().second));
        this.editSyntaxCssButton.setOnAction(event -> this.onEditSyntaxTheme.accept(this.syntaxThemeBox.getValue().second));
    }


    private void loadFonts()
    {
        File[] fontFiles = Directory.FONTS_FOLDER.listFiles();
        if (fontFiles == null)
        {
            fonts = new SerializableFont[0];
            return;
        }
        fonts = new SerializableFont[fontFiles.length];
        for (int i = 0; i < fontFiles.length; i++)
            fonts[i] = SerializableFont.fromFile(fontFiles[i]);
    }

    private void applySettings(ActionEvent event)
    {
        if (this.appThemeBox.getValue() != null)
        {
            App.setApplicationTheme(this.appThemeBox.getValue().second);
        }
        if (this.syntaxThemeBox.getValue() != null)
        {
            App.setSyntaxTheme(this.syntaxThemeBox.getValue().second);
        }
        if (this.fontFamilyBox.getValue() != null)
        {
            SerializableFont font = new SerializableFont(this.fontFamilyBox.getValue().second.getFamily(), this.fontSizeSlider.getValue());
            App.setEditorFont(font);
        }
        saveUserSettings();
        Platform.runLater(() -> this.onRequestClose.run());
    }

    private void loadUserSettings()
    {
        UserSetting userSettings = LocalUser.userSettings;
        if (userSettings == null)
            return;
        File appTheme = userSettings.getThemeFile();
        File syntaxTheme = userSettings.getSyntaxThemeFile();
        SerializableFont font = userSettings.getEditorFont();
        this.appThemeBox.getItems().forEach(item -> {
            if (item.second.equals(appTheme))
                this.appThemeBox.setValue(item);
        });
        this.syntaxThemeBox.getItems().forEach(item -> {
            if (item.second.equals(syntaxTheme))
                this.syntaxThemeBox.setValue(item);
        });
        this.fontFamilyBox.getItems().forEach(item -> {
            if (item.second.getFamily().equals(font.getFamily()))
            {
                this.fontFamilyBox.setValue(item);
                this.fontSizeSlider.setValue(font.getSize());
            }
        });

    }

    private void saveUserSettings()
    {
        UserSetting userSettings = LocalUser.userSettings;
        if (userSettings == null)
            return;
        File appTheme = this.appThemeBox.getValue() == null ? null : this.appThemeBox.getValue().second;
        File syntaxTheme = this.syntaxThemeBox.getValue() == null ? null : this.syntaxThemeBox.getValue().second;
        String fontFamily = this.fontFamilyBox.getValue() == null ? null : this.fontFamilyBox.getValue().second.getFamily();
        SerializableFont font = null;
        if (fontFamily != null)
            font = new SerializableFont(fontFamily, this.fontSizeSlider.getValue());
        SerializableFont oldFont = LocalUser.userSettings.getEditorFont();
        if (appTheme != null && !(appTheme.equals(userSettings.getThemeFile())))
            LocalUser.userSettings.setThemeFile(appTheme);
        if (syntaxTheme != null && !(syntaxTheme.equals(userSettings.getSyntaxThemeFile())))
            LocalUser.userSettings.setSyntaxThemeFile(syntaxTheme);
        if (font != null && !font.equals(oldFont))
            LocalUser.userSettings.setEditorFont(font);
    }

    private void addComboBoxOptions()
    {
        ObservableList<Tuple<String, File>> appThemes = FXCollections.observableArrayList();
        ObservableList<Tuple<String, File>> syntaxThemes = FXCollections.observableArrayList();
        ObservableList<Tuple<String, SerializableFont>> fontFamilies = FXCollections.observableArrayList();
//        File[] syntaxThemeFiles = Directory.SYNTAX_THEMES_FOLDER.listFiles();
//        File[] appThemeFiles = Directory.APP_THEMES_FOLDER.listFiles();
        File[] syntaxThemeFiles = new File(getClass().getClassLoader().getResource("css/Default.css").getFile()).getParentFile().listFiles();
        File[] appThemeFiles = new File(getClass().getClassLoader().getResource("css/Default.css").getFile()).getParentFile().listFiles();
        if (syntaxThemeFiles != null && syntaxThemeFiles.length > 0)
            addFilesToList(syntaxThemeFiles, syntaxThemes);
        if (appThemeFiles != null && appThemeFiles.length > 0)
            addFilesToList(appThemeFiles, appThemes);
        for (SerializableFont serializableFont : ThemeSettingsPage.fonts)
            fontFamilies.add(new Tuple<>(serializableFont.getFamily(), serializableFont));

        this.appThemeBox.setConverter(new TupleConverter<>(true));
        this.syntaxThemeBox.setConverter(new TupleConverter<>(true));
        this.fontFamilyBox.setConverter(new TupleConverter<>(true));

        this.appThemeBox.setItems(appThemes);
        this.syntaxThemeBox.setItems(syntaxThemes);
        this.fontFamilyBox.setItems(fontFamilies);
    }

    private void addFilesToList(File[] files, ObservableList<Tuple<String, File>> list)
    {
        for (File file : files)
        {
            String themeName = getFileName(file);
            list.add(new Tuple<>(themeName, file));
        }
    }

    private String getFileName(File file)
    {
        /* Remove the file extension and add spacing between capital letters */
        return file.getName()
                .substring(0, file.getName().lastIndexOf('.'))
                .replaceAll("\\d+", "")
                .replaceAll("(.)([A-Z])", "$1 $2");
    }

    private void registerFontSizeListeners()
    {
        this.fontSizeSlider.valueProperty().addListener(event -> {
            Double numericValue = this.fontSizeSlider.getValue();
            this.fontSizeField.setText(String.valueOf(numericValue.intValue()));
        });
        this.fontSizeField.textProperty().addListener((obj, oldVal, newVal ) -> {

            try
            {
                Integer value = Double.valueOf(newVal).intValue();
                if (value > 100)
                {
                    this.fontSizeField.setText(oldVal);
                    return;
                }
            }
            catch (NumberFormatException ex) { }
            if (!newVal.matches("\\d*"))
                this.fontSizeField.setText(newVal.replaceAll("[^\\d]", ""));
            else if (!newVal.isEmpty())
            {
                try
                {
                    this.fontSizeSlider.setValue(Double.valueOf(newVal));
                }
                catch (NumberFormatException ex) { ex.printStackTrace(); }
            }
        });
    }

    public Runnable getOnRequestCancel() { return onRequestCancel; }
    public void setOnRequestCancel(Runnable onRequestCancel) { this.onRequestCancel = onRequestCancel; }
    public Runnable getOnRequestClose() { return onRequestClose; }
    public void setOnRequestClose(Runnable onRequestClose) { this.onRequestClose = onRequestClose; }

    public Consumer<File> getOnEditSyntaxTheme() { return onEditSyntaxTheme; }
    public void setOnEditSyntaxTheme(Consumer<File> onEditSyntaxTheme)
    {
        Objects.requireNonNull(onEditSyntaxTheme);
        this.onEditSyntaxTheme = onEditSyntaxTheme;
    }
    public Consumer<File> getOnEditUITheme() { return onEditUITheme; }
    public void setOnEditUITheme(Consumer<File> onEditUITheme)
    {
        Objects.requireNonNull(onEditUITheme);
        this.onEditUITheme = onEditUITheme;
    }

    private final class TupleConverter<T, E> extends StringConverter<Tuple<T, E>>
    {
        private final boolean useFirst;

        public TupleConverter(boolean useFirst)
        {
            this.useFirst = useFirst;
        }

        @Override
        public String toString(Tuple<T, E> object)
        {
            if (object == null)
                return null;
            if (useFirst)
                return object.first.toString();
            else
                return object.second.toString();
        }

        @Override
        public Tuple<T, E> fromString(String string)
        {
            return null;
        }
    }
}











