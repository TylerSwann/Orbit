package io.orbit.controllers.marketplaceui;

import com.jfoenix.controls.JFXTextField;
import io.orbit.App;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 13:37
 */
public class BrowseMarketPlacePage
{


    public VBox marketPlaceItemsContainer;
    public ScrollPane scrollPane;
    public JFXTextField searchMarketplaceField;
    public HBox headerContainer;
    public AnchorPane root;

    public static AnchorPane load()
    {
        try
        {
            URL fxmlURL = App.class.getClassLoader().getResource("marketplace_views/BrowseMarketPlacePage.fxml");
            assert fxmlURL != null;
            return FXMLLoader.load(fxmlURL);
        }
        catch (Exception ex)
        {
            System.out.println("ERROR loading BrowseMarketPlacePage.fxml in BrowseMarketPlacePage.load()");
            ex.printStackTrace();
        }
        return null;
    }

    public void initialize()
    {
        this.scrollPane.widthProperty().addListener(event -> {
            double width = this.scrollPane.getWidth() - this.scrollPane.getViewportBounds().getWidth();
            this.marketPlaceItemsContainer.setPrefWidth(width);
        });
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());
        this.marketPlaceItemsContainer.getChildren().add(new MarketPlaceItem());

    }
}
