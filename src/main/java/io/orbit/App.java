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
package io.orbit;

import io.orbit.controllers.AppStage;
import io.orbit.controllers.OSplashPageController;
import io.orbit.plugin.PluginDispatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.ArrayList;

/**
 *
 * Created by Tyler Swann on Friday January 05, 2018 at 16:14
 */
public class App extends Application
{
    public static final String ORBIT_VERSION = "0.1";
    private static final ArrayList<Runnable> onCloseHandlers = new ArrayList<>();
    private static final ArrayList<Runnable> willLoadHandlers = new ArrayList<>();

    public static void main(String[] args){ launch(args); }

    public static ApplicationController controller() { return AppStage.controller(); }
    public static Stage stage() { return AppStage.stage(); }

    public void start(Stage stage)
    {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        stage.setTitle("Orbit");
        StatusLogger.getLogger().setLevel(Level.OFF);
        Platform.setImplicitExit(false);
        PluginDispatch.open();
        AppStage.build(stage);
        stage.setOnCloseRequest(event -> {
            onCloseHandlers.forEach(Runnable::run);
            Platform.runLater(onCloseHandlers::clear);
        });
        OSplashPageController.show(() -> {
            App.willLoadHandlers.forEach(Runnable::run);
            Platform.runLater(willLoadHandlers::clear);
        });
    }

    public static void addOnCloseHandler(Runnable action) { App.onCloseHandlers.add(action); }
    public static void addWillLoadHandler(Runnable action) { App.willLoadHandlers.add(action); }
}
