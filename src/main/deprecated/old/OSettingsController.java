package io.orbit.controllers;


import io.orbit.App;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.controllers.events.SettingsEvent;
import io.orbit.controllers.events.StatelessEventTargetObject;
import io.orbit.controllers.events.menubar.MenuBarFileEvent;
import io.orbit.util.Setting;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;


/**
 * Created by Tyler Swann on Friday February 23, 2018 at 15:17
 */
@Deprecated
public class OSettingsController extends StatelessEventTargetObject
{
    private static SettingsWindow settingsWindow;

    public OSettingsController()
    {
        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_LOAD, appEvent -> {
            App.applicationController().getMenuBarController().addEventHandler(MenuBarFileEvent.SETTINGS, event -> {
                if (settingsWindow == null)
                {
                    Setting themeAndFonts = new Setting("Themes and Fonts", ThemeSettingsPage.load());
                    settingsWindow = new SettingsWindow(new Setting[]{ themeAndFonts });
                    ThemeSettingsPage.CONTROLLER.setOnEditSyntaxTheme(file -> this.fireEvent(new SettingsEvent(file, this, this, SettingsEvent.EDIT_SYNTAX_THEME)));
                    ThemeSettingsPage.CONTROLLER.setOnEditUITheme(file -> this.fireEvent(new SettingsEvent(file, this, this, SettingsEvent.EDIT_UI_THEME)));
                }
                settingsWindow.show();
            });
        });
    }
}
