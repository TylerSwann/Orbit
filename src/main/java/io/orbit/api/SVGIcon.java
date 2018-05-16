package io.orbit.api;

import javafx.scene.Group;
import afester.javafx.svg.SvgLoader;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import java.net.URL;

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 17:01
 */
public class SVGIcon extends Group
{
    public SVGIcon(File file) throws FileNotFoundException
    {
        this(new FileInputStream(file));
    }
    public SVGIcon(URL resource) throws IOException
    {
        this(resource.openStream());
    }

    public SVGIcon(InputStream inputStream)
    {
        SvgLoader loader = new SvgLoader();
        Group child = loader.loadSvg(inputStream);
        this.getChildren().add(child);
    }

    public SVGIcon(Ikon iconCode)
    {
        FontIcon icon = new FontIcon(iconCode);
        this.getChildren().add(icon);
    }

    public static SVGIcon fromResource(URL resource)
    {
        SVGIcon icon = null;
        try
        {
            icon = new SVGIcon(resource);
        }
        catch (IOException ex)
        {
            System.out.println(String.format("Unable to load SVGIcon from resources at URL: %s", resource.toExternalForm()));
            ex.printStackTrace();
        }
        return icon;
    }
}