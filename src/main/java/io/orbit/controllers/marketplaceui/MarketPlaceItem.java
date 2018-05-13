package io.orbit.controllers.marketplaceui;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.HyperlinkButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Tyler Swann on Saturday May 12, 2018 at 13:46
 */
public class MarketPlaceItem extends VBox
{
    private HBox bottomInfoContainer;
    private Label titleLabel;
    private Label descriptionLabel;
    private Label creatorNameLabel;
    private Label downloadsLabel;
    private HyperlinkButton visitWebsiteButton;
    private URI websiteURL;
    private JFXButton installButton;

    public MarketPlaceItem()
    {
        try
        {
            this.websiteURL = new URI("http://www.google.com");
        }
        catch (URISyntaxException ex)
        {
            System.out.println("ERROR Invalid URI format for MarketPlaceItem");
            ex.printStackTrace();
        }
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30.0);
        this.setPadding(new Insets(20.0));

        this.downloadsLabel = new Label(String.valueOf(123));
        this.downloadsLabel.setFont(new Font("Roboto Light", 15.0));

        this.titleLabel = new Label("Plugin Title");
        this.titleLabel.setFont(new Font("Roboto Black", 23.0));
        this.titleLabel.setAlignment(Pos.CENTER_LEFT);
        this.titleLabel.setTextAlignment(TextAlignment.LEFT);

        this.descriptionLabel = new Label("Description Label");
        this.descriptionLabel.setFont(new Font("Roboto Light Italic", 16.0));
        this.descriptionLabel.setAlignment(Pos.CENTER_LEFT);
        this.descriptionLabel.setTextAlignment(TextAlignment.LEFT);
        this.descriptionLabel.prefWidthProperty().bind(this.widthProperty());

        this.creatorNameLabel = new Label("Tyler Swann");
        this.creatorNameLabel.setFont(new Font("Roboto Regular", 13.0));

        double scale = 1.25;

        this.installButton = new JFXButton("INSTALL", new FontIcon(FontAwesomeSolid.DOWNLOAD));
        this.installButton.getStyleClass().addAll("button-primary", "install-button");
        this.installButton.setPadding(new Insets(8.0, 8.0, 8.0, 8.0));
        this.installButton.setPrefSize(100.0, 36.0);

        this.bottomInfoContainer = new HBox();
        this.bottomInfoContainer.setAlignment(Pos.CENTER);

        FontIcon downloadIcon = new FontIcon(FontAwesomeSolid.CLOUD_DOWNLOAD_ALT);
        downloadIcon.setScaleX(scale);
        downloadIcon.setScaleY(scale);

        FontIcon visitIcon = new FontIcon(FontAwesomeSolid.EXTERNAL_LINK_ALT);
        visitIcon.setScaleX(scale);
        visitIcon.setScaleY(scale);
        this.visitWebsiteButton = new HyperlinkButton("Visit Website", visitIcon, this.websiteURL);
        this.visitWebsiteButton.getStyleClass().add("button-secondary");


        HBox headerInfoContainer = new HBox();
        headerInfoContainer.setAlignment(Pos.CENTER_RIGHT);
        headerInfoContainer.setSpacing(10.0);
        headerInfoContainer.getChildren().addAll(downloadIcon, this.downloadsLabel, this.visitWebsiteButton);

        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER);
        headerContainer.getChildren().addAll(this.titleLabel, headerInfoContainer);

        HBox leftInfoContainer = new HBox();
        leftInfoContainer.setSpacing(20.0);
        leftInfoContainer.getChildren().addAll(this.creatorNameLabel);
        leftInfoContainer.setAlignment(Pos.CENTER_LEFT);

        HBox rightInfoContainer = new HBox();
        rightInfoContainer.setSpacing(20.0);
        rightInfoContainer.setAlignment(Pos.CENTER_RIGHT);
        rightInfoContainer.getChildren().add(installButton);

        this.bottomInfoContainer.widthProperty().addListener(event -> {
            double width = this.bottomInfoContainer.getWidth() / 2.0;
            leftInfoContainer.setPrefWidth(width);
            headerInfoContainer.setPrefWidth(width);
            this.titleLabel.setPrefWidth(width);
            rightInfoContainer.setPrefWidth(width);
        });
        this.bottomInfoContainer.getChildren().addAll(leftInfoContainer, rightInfoContainer);
        this.getChildren().addAll(
                headerContainer,
                this.descriptionLabel,
                this.bottomInfoContainer
        );
        this.setPrefHeight(100.0);
        this.getStyleClass().add("market-place-item");
    }

    public void setTitle(String title) { this.titleLabel.setText(title); }
    public String getTitle() { return this.titleLabel.getText(); }
    public void setDescription(String description) { this.descriptionLabel.setText(description); }
    public String getDescription() { return this.descriptionLabel.getText(); }
    public void setCreatorName(String name) { this.creatorNameLabel.setText(name); }
    public String getCreatorName() { return this.creatorNameLabel.getText(); }
    public void setWebSiteURL(URI url) { this.websiteURL = url; }
    public URI getWebSiteURL() { return this.websiteURL; }
    public void setDownloadsCount(int downloads) {  this.downloadsLabel.setText(String.valueOf(downloads));  }
}
