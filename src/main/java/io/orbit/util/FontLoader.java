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
        URL[] robotoFonts = new URL[]{
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Black.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-BlackItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Bold.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-BoldItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Italic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Light.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-LightItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Medium.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-MediumItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Regular.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-Thin.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/Roboto-ThinItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-Bold.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-BoldItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-Italic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-Light.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-LightItalic.ttf"),
                App.class.getClassLoader().getResource("fonts/roboto/RobotoCondensed-Regular.ttf")
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
        for (URL robotoFont : robotoFonts)
        {
            if (robotoFont == null)
            {
                System.out.println("ERROR: Couldn't load roboto font in FontLoader.java");
                continue;
            }
            Font.loadFont(robotoFont.toExternalForm(), 12.0);
        }
    }
}
