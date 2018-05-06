package io.orbit.util;

import javafx.scene.paint.Paint;

/**
 * Created by TylersDesktop on 12/28/2017.
 *
 */
public class Color
{
    public static final Color RED = new Color(255, 0, 0, 1.0);
    public static final Color WHITE = new Color(255, 255, 255, 1.0);
    public static final Color BLUE = new Color(0, 0, 255, 1.0);

    public final int r;
    public final int g;
    public final int b;
    public final double a;

    public Color(int r, int g, int b, double a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public Color(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0;
    }

    public Color(String hexColor)
    {
        if (hexColor.length() == 7 || hexColor.length() == 9)
        {
            this.r =  Integer.valueOf(hexColor.substring(1, 3), 16);
            this.g =  Integer.valueOf(hexColor.substring(3, 5), 16);
            this.b =  Integer.valueOf(hexColor.substring(5, 7), 16);
            this.a = hexColor.length() == 9 ? Integer.valueOf(hexColor.substring(7, 9), 16) : 1.0;
        }
        else
        {
            System.err.println("com.text.Color from hex string must be 7 or 9 characters long. Values entered are ignored");
            this.r = 255;
            this.g = 255;
            this.b = 255;
            this.a = 1.0;
        }
    }

    public Color(javafx.scene.paint.Color color)
    {
        this.r = (int)(color.getRed() * 255.0);
        this.g = (int)(color.getGreen() * 255.0);
        this.b = (int)(color.getBlue() * 255.0);
        this.a = color.getOpacity();
    }

    public Color(Paint paint)
    {
        this(paint.toString());
    }

    public String toRGBString()
    {
        return String.format("rgb(%d, %d, %d)", r, g, b);
    }
    public String toRGBAString()
    {
        return String.format("rgba(%d, %d, %d, %f)", r, g, b, a);
    }
}
