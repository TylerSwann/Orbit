/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.orbit.settings;

import com.jfoenix.controls.*;
import io.orbit.Themes;
import io.orbit.util.SerializableFont;
import io.orbit.util.Tuple;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Tyler Swann on Thursday September 20, 2018 at 19:45
 */
public class GeneralSettings
{
    private static final String MACOS_CMD = "⌘";
    private static final String MACOS_OPTION = "⌥";
    private static final String MACOS_CTRL = "⌃";

    @FXML private JFXButton cut;
    @FXML private JFXButton copy;
    @FXML private JFXButton paste;
    @FXML private JFXButton undo;
    @FXML private JFXButton redo;
    @FXML private JFXButton save;
    @FXML private JFXButton saveAll;
    @FXML private JFXButton find;
    @FXML private JFXButton findReplace;
    @FXML private JFXButton findInProject;
    @FXML private JFXButton findReplaceInProject;

    @FXML private JFXComboBox<Tuple<String, SerializableFont>> fontFamilyBox;
    @FXML private JFXSlider fontSizeSlider;
    @FXML private JFXTextField fontSizeField;

    @FXML private JFXComboBox<Tuple<String, File>> syntaxStyleBox;
    @FXML private JFXButton editSyntaxStyle;

    @FXML private JFXButton editAppStyle;
    @FXML private JFXComboBox<Tuple<String, File>> appStyleBox;

    private AnchorPane root;
    private HotKeys hotKeys;
    private boolean listening = false;
    private Map<JFXButton, Consumer<Shortcut>> hotKeyActions = new HashMap<>();
    private JFXButton active;

    public void initialize()
    {
        this.hotKeys = LocalUser.settings.getHotKeys();
        buildHotKeys();
        loadUserSettings();
    }

    private void buildHotKeys()
    {
        cut.setText(this.hotKeys.getCut().getDisplayText());
        copy.setText(this.hotKeys.getCopy().getDisplayText());
        paste.setText(this.hotKeys.getPaste().getDisplayText());
        undo.setText(this.hotKeys.getUndo().getDisplayText());
        redo.setText(this.hotKeys.getRedo().getDisplayText());
        save.setText(this.hotKeys.getSave().getDisplayText());
        saveAll.setText(this.hotKeys.getSaveAll().getDisplayText());
        find.setText(this.hotKeys.getFind().getDisplayText());
        findReplace.setText(this.hotKeys.getFindReplace().getDisplayText());
        findInProject.setText(this.hotKeys.getFindInProject().getDisplayText());
        findReplaceInProject.setText(this.hotKeys.getFindReplaceInProject().getDisplayText());

        this.hotKeyActions.put(this.cut, this.hotKeys::setCut);
        this.hotKeyActions.put(this.copy, this.hotKeys::setCopy);
        this.hotKeyActions.put(this.paste, this.hotKeys::setPaste);
        this.hotKeyActions.put(this.undo, this.hotKeys::setUndo);
        this.hotKeyActions.put(this.redo, this.hotKeys::setRedo);
        this.hotKeyActions.put(this.save, this.hotKeys::setSave);
        this.hotKeyActions.put(this.saveAll, this.hotKeys::setSaveAll);
        this.hotKeyActions.put(this.find, this.hotKeys::setFind);
        this.hotKeyActions.put(this.findReplace, this.hotKeys::setFindReplace);
        this.hotKeyActions.put(this.findInProject, this.hotKeys::setFindInProject);
        this.hotKeyActions.put(this.findReplaceInProject, this.hotKeys::setFindReplaceInProject);
    }

    private void loadUserSettings()
    {
        UserSetting settings = LocalUser.settings;
        if (settings == null)
            return;
        File appTheme = settings.getThemeFile();
        File syntaxTheme = settings.getSyntaxThemeFile();
        SerializableFont editorFont = settings.getEditorFont();
        ArrayList<File> fontFiles = new ArrayList<>(Arrays.asList(Directory.APP_FONTS.listFiles()));
        fontFiles.addAll(Arrays.asList(Directory.USER_FONTS.listFiles()));
        fontFiles = fontFiles.stream().filter(fontFile -> !fontFile.isDirectory() && fontFile != null).collect(Collectors.toCollection(ArrayList::new));

        SerializableFont[] fonts;
        if (fontFiles == null)
            fonts = new SerializableFont[0];
        else
        {
            fonts = new SerializableFont[fontFiles.size()];
            for (int i = 0; i < fontFiles.size(); i++)
                fonts[i] = SerializableFont.fromFile(fontFiles.get(i));
        }
        ObservableList<Tuple<String, File>> appThemes = FXCollections.observableArrayList();
        ObservableList<Tuple<String, File>> syntaxThemes = FXCollections.observableArrayList();
        ObservableList<Tuple<String, SerializableFont>> fontFamilies = FXCollections.observableArrayList();
        addFilesToList(new File[]{ Themes.MATERIAL_DARK, Themes.MATERIAL_LIGHT, Themes.LUNAR }, appThemes);
        addFilesToList(Directory.USER_APP_THEMES.listFiles(), appThemes);
        addFilesToList(new File[]{ Themes.MATERIAL_DARK_SYNTAX, Themes.MATERIAL_LIGHT_SYNTAX, Themes.LUNAR_SYNTAX }, syntaxThemes);
        addFilesToList(Directory.USER_SYNTAX_THEMES.listFiles(), syntaxThemes);
        for (SerializableFont serializableFont : fonts)
            fontFamilies.add(new Tuple<>(serializableFont.getFamily(), serializableFont));

        this.appStyleBox.setConverter(new TupleConverter<>(true));
        this.syntaxStyleBox.setConverter(new TupleConverter<>(true));
        this.fontFamilyBox.setConverter(new TupleConverter<>(true));

        this.appStyleBox.setItems(appThemes);
        this.syntaxStyleBox.setItems(syntaxThemes);
        this.fontFamilyBox.setItems(fontFamilies);

        this.appStyleBox.getItems().forEach(item -> {
            if (item.second.equals(appTheme))
                this.appStyleBox.setValue(item);
        });
        this.syntaxStyleBox.getItems().forEach(item -> {
            if (item.second.equals(syntaxTheme))
                this.syntaxStyleBox.setValue(item);
        });
        this.fontFamilyBox.getItems().forEach(item -> {
            if (item.second.getFamily().equals(editorFont.getFamily()))
            {
                this.fontFamilyBox.setValue(item);
                this.fontSizeSlider.setValue(editorFont.getSize());
                this.fontSizeField.setText(String.valueOf(editorFont.getSize()));
            }
        });
    }
    private void saveUserSettings()
    {
        UserSetting settings = LocalUser.settings;
        settings.setThemeFile(this.appStyleBox.getValue().second);
        settings.setSyntaxThemeFile(this.syntaxStyleBox.getValue().second);
        settings.setEditorFont(new SerializableFont(this.fontFamilyBox.getValue().second.getFamily(), this.fontSizeSlider.getValue()));
        settings.setHotKeys(this.hotKeys);
    }

    private void registerListeners()
    {
        JFXButton[] buttons = new JFXButton[] { cut, copy, paste, undo, redo, save, saveAll, find, findReplace, findInProject, findReplaceInProject };
        Arrays.stream(buttons).forEach(button -> button.addEventHandler(MouseEvent.MOUSE_CLICKED, __ -> {
            JFXSnackbar snackBar = new JFXSnackbar(root);
            snackBar.show("Listening for keys...", 3000);
            listening = true;
            this.active = button;
        }));
        Platform.runLater(() -> {
            if (this.root.getScene() == null || this.root.getScene().getWindow() == null)
                return;
            this.root.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, __ -> this.saveUserSettings());
        });
        this.fontSizeSlider.valueProperty().addListener(__ -> {
            int size = (int) this.fontSizeSlider.getValue();
            this.fontSizeField.setText(String.valueOf(size));
            this.fontFamilyBox.getValue().second.setSize(this.fontSizeSlider.getValue());
            Themes.setEditorFont(this.fontFamilyBox.getValue().second);
        });
        this.fontSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(newValue))
                return;
            if (newValue.isEmpty())
            {
                this.fontSizeField.setText("0");
                this.fontSizeSlider.setValue(0.0);
                return;
            }
            int size;
            try { size = Integer.parseInt(newValue); }
            catch (NumberFormatException ex)
            {
                this.fontSizeField.setText(oldValue);
                return;
            }
            this.fontSizeField.setText(String.valueOf(size));
            this.fontSizeSlider.setValue(size);
        });
        this.root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!listening)
                return;
            Shortcut combination = combinationFromEvent(event);
            if (combination == null)
                return;
            this.active.setText(combination.getDisplayText());
            this.hotKeyActions.get(this.active).accept(combination);
            listening = false;
        });
        appStyleBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (oldV.second.equals(newV.second))
                return;
            Themes.setApplicationTheme(newV.second);
        });
        syntaxStyleBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (oldV.second.equals(newV.second))
                return;
            Themes.setSyntaxTheme(newV.second);
        });
        this.fontFamilyBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (oldV.second.equals(newV.second))
                return;
            SerializableFont font = newV.second;
            font.setSize(this.fontSizeSlider.getValue());
            Themes.setEditorFont(font);
        });
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

    private final class TupleConverter<T, E> extends StringConverter<Tuple<T, E>>
    {
        private final boolean useFirst;

        public TupleConverter(boolean useFirst) { this.useFirst = useFirst; }

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
        public Tuple<T, E> fromString(String string) { return null; }
    }

    protected void setRoot(AnchorPane root)
    {
        Objects.requireNonNull(root);
        this.root = root;
        registerListeners();
    }
    private Shortcut combinationFromEvent(KeyEvent event)
    {
        ArrayList<KeyCombination.Modifier> modifiers = new ArrayList<>();
        if (event.getCode().isModifierKey())
            return null;
        if (event.isShortcutDown())
            modifiers.add(KeyCombination.SHORTCUT_DOWN);
        if (event.isShiftDown())
            modifiers.add(KeyCodeCombination.SHIFT_DOWN);
        if (event.isAltDown())
            modifiers.add(KeyCodeCombination.ALT_DOWN);
        if (event.isMetaDown())
            modifiers.add(KeyCodeCombination.META_DOWN);
        return new Shortcut(event.getCode(), modifiers.toArray(new KeyCombination.Modifier[modifiers.size()]));
    }
}
