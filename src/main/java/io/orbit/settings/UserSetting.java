package io.orbit.settings;

import io.orbit.util.SerializableFont;
import io.orbit.util.Size;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.io.File;

/**
 * Created by Tyler Swann on Thursday March 29, 2018 at 14:59
 */
public class UserSetting
{
    public static transient final UserSetting DEFAULT_SETTINGS;

    static {
        DEFAULT_SETTINGS = new UserSetting();
        DEFAULT_SETTINGS.setThemeFile(Directory.DEFAULT_THEME);
        DEFAULT_SETTINGS.setSyntaxThemeFile(Directory.DEFAULT_SYNTAX_THEME);
        DEFAULT_SETTINGS.setProjects(new ProjectData[0]);
        DEFAULT_SETTINGS.setTerminalDividerPos(0.7572);
        DEFAULT_SETTINGS.setNavigatorDividerPos(0.1928);
        DEFAULT_SETTINGS.setTerminalClosed(false);
        DEFAULT_SETTINGS.setNavigatorClosed(false);
        DEFAULT_SETTINGS.setEditorFont(SerializableFont.fromResources("fonts/SourceCodePro-Regular.otf.woff"));
    }
    private SimpleObjectProperty<File> themeFile = new SimpleObjectProperty<>();
    private SimpleObjectProperty<File> syntaxThemeFile = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SerializableFont> editorFont = new SimpleObjectProperty<>();
    private SimpleObjectProperty<ProjectData> lastModifiedProject = new SimpleObjectProperty<>(new ProjectData());
    private SimpleObjectProperty<Size> windowSize = new SimpleObjectProperty<>(new Size(1781.0, 891.0));
    private ProjectData[] projects;
    private double navigatorDividerPos;
    private double terminalDividerPos;
    private boolean isTerminalClosed;
    private boolean isNavigatorClosed;

    public UserSetting() { }

    public ObjectProperty<Size> windowSizeProperty() { return windowSize; }
    public Size getWindowSize() { return windowSize.get(); }
    public void setWindowSize(Size size) { this.windowSize.set(size); }

    public ObjectProperty<File> syntaxThemeFileProperty() { return syntaxThemeFile; }
    public File getSyntaxThemeFile() { return syntaxThemeFile.get(); }
    public void setSyntaxThemeFile(File syntaxThemeFile) { this.syntaxThemeFile.setValue(syntaxThemeFile); }

    public ObjectProperty<File> themeFileProperty() { return themeFile; }
    public File getThemeFile() { return themeFile.get(); }
    public void setThemeFile(File themeFile) { this.themeFile.setValue(themeFile); }

    public ProjectData[] getProjects() { return projects; }
    public void setProjects(ProjectData[] projects) { this.projects = projects; }

    public ObjectProperty<ProjectData> lastModifiedProjectProperty() { return lastModifiedProject; }
    public ProjectData getLastModifiedProject() { return lastModifiedProject.get(); }
    public void setLastModifiedProject(ProjectData lastModifiedProject) { this.lastModifiedProject.setValue(lastModifiedProject); }

    public double getNavigatorDividerPos() { return navigatorDividerPos; }
    public void setNavigatorDividerPos(double navigatorDividerPos) { this.navigatorDividerPos = navigatorDividerPos; }

    public double getTerminalDividerPos() { return terminalDividerPos; }
    public void setTerminalDividerPos(double terminalDividerPos) { this.terminalDividerPos = terminalDividerPos; }

    public boolean isTerminalClosed() { return isTerminalClosed; }
    public void setTerminalClosed(boolean terminalClosed) { isTerminalClosed = terminalClosed; }

    public boolean isNavigatorClosed() { return isNavigatorClosed; }
    public void setNavigatorClosed(boolean navigatorClosed) { isNavigatorClosed = navigatorClosed; }

    public ObjectProperty<SerializableFont> editorFontProperty() { return editorFont; }
    public SerializableFont getEditorFont() { return editorFont.get(); }
    public void setEditorFont(SerializableFont font) { this.editorFont.setValue(font); }
}
