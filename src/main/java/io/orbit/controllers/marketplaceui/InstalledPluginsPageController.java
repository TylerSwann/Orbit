package io.orbit.controllers.marketplaceui;

import io.orbit.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 17:28
 */
public class InstalledPluginsPageController
{
    public static AnchorPane load()
    {
        AnchorPane root = null;
        try
        {
            URL url = App.class.getClassLoader().getResource("marketplace_views/InstalledPluginsPage.fxml");
            if (url != null)
                root = FXMLLoader.load(url);
        }
        catch (IOException ex)
        {
            System.out.println("ERROR loading InstalledPluginsPageController.fxml from file");
            ex.printStackTrace();
        }
        assert root != null;
        return root;
    }

}
