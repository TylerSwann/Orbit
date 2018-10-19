package io.orbit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import java.io.IOException;
import java.net.URL;


public class Help
{
    public static void showAboutPage()
    {
        URL aboutFXML = Help.class.getClassLoader().getResource("views/AboutPage.fxml");
        assert aboutFXML != null;
        showPopup(aboutFXML);
    }

    public static void showLicense()
    {
//        URL licenseURL = Help.class.getClassLoader().getResource("views/LicensePage.fxml");
//        assert licenseURL != null;
//        showPopup(licenseURL, true);
    }

    private static void showPopup(URL url)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            AnchorPane root = loader.load();
            PopupControl popup = new PopupControl();
            popup.getScene().setRoot(root);
            popup.setAutoHide(true);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            popup.setX((screenBounds.getWidth() - root.getPrefWidth()) / 2.0);
            popup.setY((screenBounds.getHeight() - root.getPrefHeight()) / 2.0);
            popup.show(AppStage.stage());
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
