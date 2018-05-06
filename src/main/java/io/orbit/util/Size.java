package io.orbit.util;

/**
 * Created by Tyler Swann on Friday April 13, 2018 at 15:05
 */
public class Size
{
    private double width;
    private double height;

    public Size(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
    public Size() { }

    public double getHeight() { return height; }
    public double getWidth() { return width; }
    public void setWidth(double width) {  this.width = width;  }
    public void setHeight(double height) {  this.height = height;  }
}
