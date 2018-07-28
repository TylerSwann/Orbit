package io.orbit.settings;

import com.jfoenix.controls.JFXButton;
import io.orbit.App;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Tyler Swann on Thursday May 03, 2018 at 14:57
 */
public class KeyBindingsPageController
{
    public JFXButton saveAllPrimary;
    public JFXButton saveAllSecondary;
    public JFXButton findAndRPrimary;
    public JFXButton findAndRSecondary;
    public JFXButton savePrimary;
    public JFXButton saveSecondary;
    public JFXButton findPrimary;
    public JFXButton findSecondary;

    public Label listeningLabel;
    public VBox root;
    private KeyBinding currentBinding = KeyBinding.BLANK;
    private JFXButton currentButton;

    private enum KeyBinding
    {
        BLANK,
        SAVE_ALL_PRIMARY,
        SAVE_ALL_SECONDARY,
        SAVE_PRIMARY,
        SAVE_SECONDARY,
        FIND_PRIMARY,
        FIND_SECONDARY,
        FIND_AND_REPLACE_PRIMARY,
        FIND_AND_REPLACE_SECONDARY
    }

    public static VBox load()
    {
        URL fxmlURL = App.class.getClassLoader().getResource("views/KeyBindingsPage.fxml");
        assert fxmlURL != null;
        try
        {
            return FXMLLoader.load(fxmlURL);
        }
        catch (IOException ex)
        {
            System.out.println("Couldn't load KeyBindings Page!!");
            ex.printStackTrace();
        }
        return null;
    }


    public void initialize()
    {
        this.listeningLabel.setOpacity(0.0);
        JFXButton[] buttons = new JFXButton[]{
                this.saveAllPrimary,
                this.saveAllSecondary,
                this.savePrimary,
                this.saveSecondary,
                this.findAndRPrimary,
                this.findAndRSecondary,
                this.findPrimary,
                this.findSecondary
        };
        this.saveAllPrimary.setUserData(KeyBinding.SAVE_ALL_PRIMARY);
        this.saveAllSecondary.setUserData(KeyBinding.SAVE_ALL_SECONDARY);
        this.savePrimary.setUserData(KeyBinding.SAVE_PRIMARY);
        this.saveSecondary.setUserData(KeyBinding.SAVE_SECONDARY);
        this.findAndRPrimary.setUserData(KeyBinding.FIND_AND_REPLACE_PRIMARY);
        this.findAndRSecondary.setUserData(KeyBinding.FIND_AND_REPLACE_SECONDARY);
        this.findPrimary.setUserData(KeyBinding.FIND_PRIMARY);
        this.findSecondary.setUserData(KeyBinding.FIND_SECONDARY);
        Arrays.stream(buttons).forEach(this::addEventsToButton);
        this.root.addEventFilter(KeyEvent.KEY_PRESSED, event -> this.applyNewBinding(event.getCode(), this.currentBinding));
    }

    private void applyNewBinding(KeyCode key, KeyBinding binding)
    {
        this.listeningLabel.setOpacity(0.0);
        this.currentButton.setText(key.getName());
        System.out.println(String.format("Key: %s -> %s", key.getName(), binding.name()));
    }

    private void addEventsToButton(JFXButton button)
    {
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            currentButton = button;
            this.listeningLabel.setOpacity(1.0);
            this.currentBinding = (KeyBinding) button.getUserData();
        });
    }
}











