package io.orbit.util;

import javafx.scene.Node;

/**
 * Created by Tyler Swann on Saturday February 24, 2018 at 17:05
 */
public class Setting
{
    public final boolean isParentSetting;
    public final String title;
    public final Node content;
    public final Setting[] subSettings;

    public Setting(String title, Node content)
    {
        this.title = title;
        this.content = content;
        this.isParentSetting = false;
        this.subSettings = null;
    }

    public Setting(String title, Setting[] subSettings)
    {
        this.title = title;
        this.subSettings = subSettings;
        this.content = null;
        this.isParentSetting = true;
    }
}
