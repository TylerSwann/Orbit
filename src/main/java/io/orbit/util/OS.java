package io.orbit.util;

import com.sun.javafx.PlatformUtil;
import com.sun.jna.Platform;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 15:41
 * Help class for information regarding the users operating system
 */
public enum  OS
{
    WIN_x86,
    WIN_x64,
    LINUX_x86,
    LINUX_x64,
    MAC_OS_x86,
    MAC_OS_x64,
    FREE_BSD_x86,
    FREE_BSD_x64,
    UNSUPPORTED;

    public static final OS current;

    static {

        if (Platform.isWindows() && Platform.is64Bit())
            current = WIN_x64;
        else if (Platform.isWindows())
            current = WIN_x86;
        else if (Platform.isMac() && Platform.is64Bit())
            current = MAC_OS_x64;
        else if (Platform.isMac())
            current = MAC_OS_x86;
        else if (Platform.isLinux() && Platform.is64Bit())
            current = LINUX_x64;
        else if (Platform.isLinux())
            current = LINUX_x86;
        else if (Platform.isFreeBSD() && Platform.is64Bit())
            current = FREE_BSD_x64;
        else if (Platform.isFreeBSD())
            current = FREE_BSD_x86;
        else
            current = UNSUPPORTED;
    }

    public static final boolean isWindows = current == OS.WIN_x86 || current == OS.WIN_x64;
    public static final boolean isMacOS = current == OS.MAC_OS_x86 || current == OS.MAC_OS_x64;
    public static final boolean isLinux = current == OS.LINUX_x86 || current == OS.LINUX_x64;
    public static final boolean isFreeBSD = current == OS.FREE_BSD_x86 || current == OS.FREE_BSD_x64;
    public static final boolean isUnsupported = (!isWindows && !isMacOS && !isFreeBSD && !isLinux);
}
