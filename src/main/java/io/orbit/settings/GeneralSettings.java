package io.orbit.settings;

import com.jfoenix.controls.*;
import io.orbit.util.SerializableFont;
import io.orbit.util.Tuple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

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

    private SerializableFont[] fonts;
    private AnchorPane root;
    private HotKeys hotKeys;
    private boolean listening = false;
    private Map<JFXButton, Consumer<KeyCombination>> hotKeyActions = new HashMap<>();
    private JFXButton active;

    public void initialize()
    {
        this.hotKeys = LocalUser.userSettings.getHotKeys();
        addHotKeyText();
        loadUserSettings();
    }

    private void addHotKeyText()
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
    }

    private void loadUserSettings()
    {
        UserSetting settings = LocalUser.userSettings;
        if (settings == null)
            return;
        File appTheme = settings.getThemeFile();
        File syntaxTheme = settings.getSyntaxThemeFile();
        SerializableFont font = settings.getEditorFont();
        File[] fontFiles = Directory.FONTS_FOLDER.listFiles();
        if (fontFiles == null)
            fonts = new SerializableFont[0];
        else
        {
            fonts = new SerializableFont[fontFiles.length];
            for (int i = 0; i < fontFiles.length; i++)
                fonts[i] = SerializableFont.fromFile(fontFiles[i]);
        }
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
            if (item.second.getFamily().equals(font.getFamily()))
            {
                this.fontFamilyBox.setValue(item);
                this.fontSizeSlider.setValue(font.getSize());
                this.fontSizeField.setText(String.valueOf(font.getSize()));
            }
        });
    }
    private void saveUserSettings()
    {

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
        this.root.sceneProperty().addListener(__ -> {
            if (this.root.getScene() != null && this.root.getScene().getWindow() != null)
                this.root.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> this.saveUserSettings());
        });
        this.fontSizeSlider.valueProperty().addListener(__ -> {
            int size = (int) this.fontSizeSlider.getValue();
            this.fontSizeField.setText(String.valueOf(size));
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

        this.root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!listening)
                return;
            KeyCodeCombination combination = combinationFromEvent(event);
            if (combination == null)
                return;
            this.active.setText(combination.getDisplayText());
            this.hotKeyActions.get(this.active).accept(combination);
            listening = false;
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
    private KeyCodeCombination combinationFromEvent(KeyEvent event)
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
        return new KeyCodeCombination(event.getCode(), modifiers.toArray(new KeyCombination.Modifier[modifiers.size()]));
    }
}
