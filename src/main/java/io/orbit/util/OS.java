package io.orbit.util;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 15:41
 * Help class for information regarding the users operating system
 */
public class OS
{
    private static final String osName = System.getProperty("os.name").toLowerCase();
    public static final boolean isWindows;
    public static final boolean isMacOS;
    public static final boolean isUnix;
    public static final boolean isSolaris;

    static {
        isWindows = osName.contains("win");
        isMacOS = osName.contains("mac");
        isUnix = osName.contains("nix");
        isSolaris = osName.contains("sunos");
    }
}
