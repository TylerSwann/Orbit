package io.orbit.api;

import javafx.scene.Group;
import afester.javafx.svg.SvgLoader;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import java.net.URL;

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 17:01
 *
 * The SVGIcon class can be used to create icons from SVG Files.
 * SVGIcons can also be created from FontAwesome Font Icons.
 * Files, InputStreams, and URLs are assumed to be located in
 * your resource folder. Otherwise, an exception is thrown.
 *
 */
public class SVGIcon extends Group implements Cloneable
{
    private Object resource;

    /**
     *
     * @param file - A resource file.
     * @throws FileNotFoundException
     */
    public SVGIcon(File file) throws FileNotFoundException
    {
        this(new FileInputStream(file));
    }

    /**
     *
     * @param resource - The URL pointing to an SVG file in your resources folder
     * @throws IOException
     */
    public SVGIcon(URL resource) throws IOException
    {
        this(resource.openStream());
    }

    /**
     *
     * @param inputStream - An InputStream pointing to an SVG file in your resources folder
     */
    public SVGIcon(InputStream inputStream)
    {
        this.resource = inputStream;
        SvgLoader loader = new SvgLoader();
        Group child = loader.loadSvg(inputStream);
        this.getChildren().add(child);
    }

    /**
     * See {@link org.kordamp.ikonli.fontawesome5.FontAwesomeSolid} for solid font icons.
     * See {@link org.kordamp.ikonli.fontawesome5.FontAwesomeBrands} for brand font icons.
     * See {@link org.kordamp.ikonli.fontawesome5.FontAwesomeRegular} for regular font icons.
     * @param iconCode - A FontAwesome Icon Code.
     */
    public SVGIcon(Ikon iconCode)
    {
        this(new FontIcon(iconCode));
    }

    /**
     * See {@link org.kordamp.ikonli.javafx.FontIcon} for documentation pertaining to the FontIcon class.
     * @param fontIcon - A FontAwesome FontIcon
     */
    public SVGIcon(FontIcon fontIcon)
    {
        this.resource = fontIcon.getIconCode();
        this.getChildren().add(fontIcon);
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

    @Override
    public SVGIcon clone()
    {
        if (this.resource instanceof Ikon)
            return new SVGIcon(((Ikon)this.resource));
        else if (this.resource instanceof InputStream)
            return new SVGIcon(((InputStream)this.resource));
        return null;
    }
}