package io.orbit.webtools;

import io.orbit.api.Project;
import io.orbit.api.SVGIcon;
import javafx.scene.layout.Pane;
import java.io.File;
import java.net.URL;

/**
 * Created by Tyler Swann on Friday May 04, 2018 at 14:03
 */
public class WebProject implements Project
{
    @Override
    public String getName()
    {
        return "Web Project";
    }

    @Override
    public SVGIcon getIcon()
    {
        try
        {
            URL url = getClass().getClassLoader().getResource("icons/web_project_icon.svg");
            if (url != null)
                return new SVGIcon(url);
        }
        catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    @Override
    public boolean start(String projectName, File rootDir)
    {
        return true;
    }
    @Override
    public Pane[] getProjectCreationSteps()
    {
        Pane one = new Pane();
        Pane two = new Pane();
        Pane three = new Pane();
        Pane four = new Pane();
        Pane five = new Pane();
        one.setStyle("-fx-background-color: red;");
        two.setStyle("-fx-background-color: blue;");
        three.setStyle("-fx-background-color: green;");
        four.setStyle("-fx-background-color: cornflowerblue;");
        five.setStyle("-fx-background-color: yellow;");
        return new Pane[]{ one, two, three, four, five };
    }
}
