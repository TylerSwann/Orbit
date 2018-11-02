package io.orbit.api;


import io.orbit.api.language.LanguageDelegate;
import io.orbit.api.language.Project;
import io.orbit.api.text.FileType;
import java.io.File;
import java.util.List;

/**
 * Created by Tyler Swann on Friday March 09, 2018 at 15:03
 */
public interface PluginController
{
    /**************************************************************************
     *                                                                        *
     *              Registering Project Types and Language Support            *
     *                                                                        *
     **************************************************************************/

    /**
     * This method creates instances of classes that implement the LanguageDelegate interface.
     * This method will be called if the user opens a file with an extension specified in the
     * getFileTypes() method. You can provide different LanguageDelegates based on the file type
     * that is being opened.
     * @param file - File that is being opened
     * @param fileExtension - Extension of file that is being opened
     * @return Classes that implement the LanguageDelegate interface
     */
    @Nullable
    default LanguageDelegate getLanguageDelegate(File file, String fileExtension) { return null; }

    /**
     * This returns an array in case you want to provide multiple
     * project types. If you only plan to provide one project type,
     * return an array with a single item in it.
     * @return Classes that implement the Project interface
     */
    @Nullable
    default List<Project> getProjectTypes() { return null; }

    /**
     *
     * This method specifies what file types your plugin uses. When the user opens a file with an extension
     * your plugin supports, the documentWasOpened method will be called along with all of the other document
     * events. If your plugin is designed for HTML and CSS file types, and the user opens a java file, then the
     * various document events, documentWasOpened, documentWasClosed, etc, wont be called.
     *
     * @return - The file type extensions supported by your plugin. ex: java, html, webtools. DO NOT INCLUDE THE DOT IN THE EXTENSION!
     */
    @NotNullable
    List<FileType> getFileTypes();

    /**
     *
     * This method creates instances of EditorController. This method will be called
     * if the user opens a file with an extension specified in the getFileTypes() method.
     * Based on the file type, return an instance of a class that implements the EditorController
     * interface. This method will be called every time the user opens a file with an extension
     * this PluginController specified. After that, the EditorController.start method will be called.
     * You can return different EditorControllers based on the fileExtension
     *
     * @param file - File that is being opened
     * @param fileExtension - Extension of file that is being opened
     * @return
     */
    @Nullable
    default EditorController getEditorController(File file, String fileExtension) {  return null;  }
}
