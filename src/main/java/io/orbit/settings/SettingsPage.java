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

import io.orbit.App;
import io.orbit.Themes;
import io.orbit.ui.tabs.MUITab;
import io.orbit.ui.tabs.MUITabBar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;


/**
 * Created by Tyler Swann on Thursday September 20, 2018 at 17:27
 */
public class SettingsPage
{

    public static void show()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SettingsPage.class.getClassLoader().getResource("views/SettingsPage.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            Stage stage = new Stage(StageStyle.UTILITY);
            Themes.sync(scene);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ex) {  ex.printStackTrace();  }
    }

    private MUITabBar tabBar;
    private AnchorPane generalSettings;
    @FXML private VBox headerContainer;
    @FXML private HBox titleContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane root;

    public void initialize()
    {
        try
        {
            this.tabBar = new MUITabBar();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("views/GeneralSettingsPage.fxml"));
            generalSettings = loader.load();
            GeneralSettings controller = loader.getController();
            controller.setRoot(root);
            this.build();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void build()
    {
        FontIcon cog = new FontIcon(FontAwesomeSolid.COG);
        MUITab general = new MUITab("GENERAL");
        MUITab advanced = new MUITab("ADVANCED");
        this.generalSettings.prefWidthProperty().bind(this.scrollPane.widthProperty());
        this.tabBar.getTabs().addAll(general, advanced);
        this.tabBar.select(general);
        this.tabBar.setPrefWidth(146.0);
        this.tabBar.setMaxHeight(AnchorPane.USE_PREF_SIZE);
        this.tabBar.setMaxWidth(AnchorPane.USE_PREF_SIZE);
        this.titleContainer.getChildren().add(0, cog);
        this.headerContainer.getChildren().add(this.tabBar);
        this.scrollPane.setContent(this.generalSettings);
    }
}
