package io.orbit.api;

import javafx.scene.Group;
import afester.javafx.svg.SvgLoader;
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
}