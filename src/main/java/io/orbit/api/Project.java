package io.orbit.api;

import javafx.scene.layout.Pane;
import java.io.File;


/**
 * Created by Tyler Swann on Sunday March 04, 2018 at 11:54
 */
public interface Project
{
    /**
     *
     * @return The name of the project type. Example: JavaFX Application
     */
    String getName();

    /**
     *
     * @return The icon that will be displayed along side the project name in the NewProjectDialog
     */
    SVGIcon getIcon();


    /**
     *
     * These panes will be included in the project creation dialog. Each pane is a step,
     * after all of the steps are completed, the start(String projectName, File rootDir) method will be called.
     *
     * @return - An array of Pane or null
     */
    @Nullable
    default Pane[] getProjectCreationSteps() { return null; }


    /**
     * This method is called when a user creates a new project. Here you should create
     * the directory structure for the project as well as any template files for the project.
     * The rootDir folder has already been created for you. Once the user clicks the Done button,
     * this method is called before the New Project Dialog is closed. Get the values out of whatever
     * text fields or other controls you added to the config pane. Check if these values are valid,
     * if they are, return true, if not return false and the New Project Dialog wont close but rather display
     * an error message
     * @param rootDir The root directory the user has chosen for the new project.
     * @param projectName The user defined name of the project
     * @return A boolean indicating whether or not to proceed with the creation of the project, or inform the user
     *          their input or configuration is invalid.
     */
    boolean start(String projectName, File rootDir);
}
