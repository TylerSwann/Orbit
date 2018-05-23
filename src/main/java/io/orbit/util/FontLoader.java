package io.orbit.util;

import io.orbit.App;
import javafx.scene.text.Font;

import java.net.URL;

/**
 * Created by Tyler Swann on Saturday May 19, 2018 at 16:56
 */
public class FontLoader
{
    private FontLoader() {}

    public static void loadFonts()
    {
        URL[] ralewayFonts = new URL[]{
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Black.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Bold.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-ExtraBold.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-SemiBold.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Medium.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Regular.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Light.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-ExtraLight.ttf"),
                App.class.getClassLoader().getResource("fonts/raleway/Raleway-Thin.ttf")
        };
        for (URL railwayFontURL : ralewayFonts)
        {
            if (railwayFontURL == null)
            {
                System.out.println("ERROR: Couldn't load raleway font in FontLoader.java");
                continue;
            }
            Font.loadFont(railwayFontURL.toExternalForm(), 12.0);
        }
    }
}
