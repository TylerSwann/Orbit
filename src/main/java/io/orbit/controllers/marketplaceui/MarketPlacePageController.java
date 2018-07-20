package io.orbit.controllers.marketplaceui;

import com.jfoenix.controls.JFXButton;
import io.orbit.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Tyler Swann on Sunday April 01, 2018 at 16:59
 */
public class MarketPlacePageController
{
    public JFXButton installedPluginsButton;
    public JFXButton installedThemesButton;
    public JFXButton updateButton;
    public JFXButton browseButton;
    public AnchorPane root;

    private AnchorPane installedPluginsPage;
    private AnchorPane browsePluginsPage;

    private Pane activePage;


    public static void show()
    {
        AnchorPane root = null;
        try
        {
            URL url = App.class.getClassLoader().getResource("marketplace_views/MarketPlacePage.fxml");
            if (url != null)
                root = FXMLLoader.load(url);
        }
        catch (IOException ex)
        {
            System.out.println("ERROR showing plugins page from PluginsPageController.show");
            ex.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root, 1506.0, 902.0);
        App.appTheme.sync(scene.getStylesheets());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Market Place");
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
    }
    
    public void initialize()
    {
        this.showInstalledPluginsPage();
        installedPluginsButton.setOnAction(event -> this.showInstalledPluginsPage());
        installedThemesButton.setOnAction(event -> this.showInstalledThemesPage());
        updateButton.setOnAction(event -> this.showUpdatesPage());
        browseButton.setOnAction(event -> this.showBrowsePage());
    }
    
    private void showInstalledPluginsPage()
    {
        if (installedPluginsPage == null)
        {
            installedPluginsPage = InstalledPluginsPageController.load();
            applyContentAnchors(installedPluginsPage);
        }
        if (activePage != null)
            root.getChildren().remove(activePage);
        activePage = installedPluginsPage;
        root.getChildren().add(installedPluginsPage);
    }
    private void showInstalledThemesPage()
    {

    }
    private void showUpdatesPage()
    {

    }
    private void showBrowsePage()
    {
//        if (!MarketplaceLoader.SERVER_IS_REACHABLE)
//            return;
        // TODO - CHECK IF SERVER IS AVAILABLE
        if (browsePluginsPage == null)
        {
            browsePluginsPage = BrowseMarketPlacePage.load();
            applyContentAnchors(browsePluginsPage);
        }
        if (activePage != null)
            root.getChildren().remove(activePage);
        activePage = browsePluginsPage;
        root.getChildren().add(browsePluginsPage);
    }

    private void applyContentAnchors(Node node)
    {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 264.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }
}
