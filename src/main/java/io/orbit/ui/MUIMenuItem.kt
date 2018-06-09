package io.orbit.ui

import javafx.scene.control.ContentDisplay
import org.kordamp.ikonli.Ikon

/**
 * Created by Tyler Swann on Thursday February 08, 2018 at 17:34
 */
@Deprecated("This class is no longer needed as it is being replace with the default javafx MenuItem. This is being done for simplicity")
public class MUIMenuItem: MUIMenuButton
{
    constructor(): super()
    constructor(text: String): super(text)
    constructor(icon: Ikon): super(icon)
    constructor(icon: Ikon, text: String): this(icon, text, ContentDisplay.LEFT)
    constructor(icon: Ikon, text: String, display: ContentDisplay): super(icon, text, display)
    public var isIconHidden: Boolean
        get() = this.iconOpacity <= 0.0
        set(hide) {
            if (hide)
                this.iconOpacity = 0.0
            else
                this.iconOpacity = 1.0
        }

    init
    {
        this.prefWidth = 150.0
        this.prefHeight = 40.0
    }
}