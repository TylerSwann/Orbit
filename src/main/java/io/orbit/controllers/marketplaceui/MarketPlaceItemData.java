package io.orbit.controllers.marketplaceui;

import javafx.scene.image.Image;
import java.util.List;


/**
 * Created by Tyler Swann on Sunday May 20, 2018 at 12:33
 */
public class MarketPlaceItemData
{
    String title;
    String description;
    String link;
    List<String> tags;
    int stars;
    int downloads;
    Image userImage;
    Image headerImage;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLink() { return link; }
    public List<String> getTags() { return tags; }
    public int getStars() { return stars; }
    public int getDownloads() { return downloads; }
    public Image getUserImage() { return userImage; }
    public Image getHeaderImage() { return headerImage; }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("");
        builder.append(String.format("title: %s\n", title));
        builder.append(String.format("\t\tdescription: %s\n", description));
        builder.append(String.format("\t\tlink: %s\n", link));
        builder.append(String.format("\t\ttags: %s\n", tags.toString()));
        builder.append(String.format("\t\tstars: %d\n", stars));
        builder.append(String.format("\t\tdownloads: %d\n", downloads));
        return builder.toString();
    }
}

