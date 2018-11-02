package io.orbit.api.language;

import io.orbit.api.Nullable;
import io.orbit.api.SVGIcon;
import javafx.scene.layout.Pane;
import java.io.File;


/**
 * Created by Tyler Swann on Sunday March 04, 2018 at 11:54
 */
public interface Project
{
    /**
     * @return The name of the project type. Example: JavaFX Application
     */
    String getName();

    /**
     * @return The icon that will be displayed along side the project name in the NewProjectDialog
     */
    SVGIcon getIcon();

    /**
     * This pane contains all textfields, buttons, and drop downs needed for configuring the project type.
     * @return - Pane containing configuration form for that project type.
     */
    @Nullable
    default Pane getProjectCreationPane() { return null; }

    /**
     * This method will be called when the user clicks the create button for the
     * project type.
     * @return - true if the form is valid and all fields are completed, otherwise false
     */
    default boolean validate() { return false; }

    /**
     * Called after the form is completed, and validated.
     */
    void create();
}
