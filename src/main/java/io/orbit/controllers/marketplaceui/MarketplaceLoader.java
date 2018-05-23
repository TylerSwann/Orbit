package io.orbit.controllers.marketplaceui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday May 20, 2018 at 12:56
 */
public class MarketplaceLoader
{
    protected static final URL pluginServerAddress = getPluginsURL();
    protected static final URL themeServerAddress = getThemeURL();
    public static final String SERVER_ADDRESS = "http://127.0.0.1:3000";

    /**
     String title;
     String description;
     String link;
     List<String> tags;
     int stars;
     int downloads;
     Image userImage;
     Image headerImage;
     */
    public static void load(Consumer<List<MarketPlaceItemData>> completion)
    {
        List<MarketPlaceItemData> items = new ArrayList<>();
        getData(pluginServerAddress, pluginResponse -> {
            parseData(pluginResponse, items);
            getData(themeServerAddress, themeResponse -> {
                parseData(themeResponse, items);
                completion.accept(items);
            });
        });
    }

    private static void parseData(String response, List<MarketPlaceItemData> items)
    {
        try
        {
            JsonElement element = new JsonParser().parse(response);
            JsonArray pluginJsonArray = element.getAsJsonArray();
            for (JsonElement jsonElement : pluginJsonArray)
            {
                JsonObject infoObject = jsonElement.getAsJsonObject().get("info").getAsJsonObject();
                MarketPlaceItemData item = new MarketPlaceItemData();
                item.title = infoObject.get("title").getAsString();
                item.description = infoObject.get("description").getAsString();
                item.stars = infoObject.get("stars").getAsInt();
                item.downloads = infoObject.get("downloads").getAsInt();
                List<String> tags = new ArrayList<>();
                JsonArray jsonTags = infoObject.get("tags").getAsJsonArray();
                for (JsonElement tag : jsonTags)
                    tags.add(tag.getAsString());
                item.tags = tags;
                item.userImage = new Image(infoObject.get("profileImage").getAsString());
                items.add(item);
            }
        }
        catch (Exception ex)
        {
            System.out.println("ERROR parsing PluginItems from json received from server in MarketPlaceLoader");
            ex.printStackTrace();
        }
    }

    private static void getData(URL address, Consumer<String> onLoad)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) address.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result;
            StringBuilder builder = new StringBuilder("");
            while ((result = input.readLine()) != null)
                builder.append(result);
            input.close();
            onLoad.accept(builder.toString());
        }
        catch (Exception ex)
        {
            System.out.println(String.format("ERROR retrieving marketplace items from %s", address.toString()));
            ex.printStackTrace();
        }
    }

//    private static Image readImageFromBinaryString(String binaryString)
//    {
//        try
//        {
//            byte[] data = Base64.getEncoder().encode(binaryString.getBytes());
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
//            BufferedImage bufferedImage = ImageIO.read(inputStream);
//            System.out.println(bufferedImage == null);
//        }
//        catch (Exception ex)
//        {
//            System.out.println("ERROR couldn't read buffered image from binary string.");
//            ex.printStackTrace();
//        }
//        return null;
//    }


    private static URL getPluginsURL()
    {
        try
        {
            return new URL(String.format("%s/cdn/info/plugin/all", SERVER_ADDRESS));
        }
        catch (IOException ex)
        {
            System.out.println("ERROR invalid URL for PluginItem request");
            ex.printStackTrace();
        }
        return null;
    }

    private static URL getThemeURL()
    {
        try
        {
            return new URL(String.format("%s/cdn/info/theme/all", SERVER_ADDRESS));
        }
        catch (IOException ex)
        {
            System.out.println("ERROR invalid URL for ThemeItem request");
            ex.printStackTrace();
        }
        return null;
    }
}
