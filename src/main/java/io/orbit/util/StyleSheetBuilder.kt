package io.orbit.util

/**
 * Created by Tyler Swann on Sunday January 21, 2018 at 09:28
 */
@Deprecated("No Longer Used")
class StyleSheetBuilder 
{
    private val generator = StyleSheetGenerator()
    
    
    public fun forClass(className: String): StyleSheetBuilder
    {
        this.generator.visitStyleClass(className)
        return this
    }
    public fun fontSize(size: Double): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-font-size", "$size")
        return this
    }

    public fun textFill(color: Color): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-text-fill", color.toRGBAString())
        return this
    }
    public fun padding(vararg pads: Int): StyleSheetBuilder
    {
        var padding = ""
        pads.forEach {
            padding += if (it == pads.lastOrNull()) "$it" else "$it,"
        }
        this.generator.addAttribute("-fx-padding", padding)
        return this
    }
    public fun padding(pads: String): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-padding", pads)
        return this
    }
    public fun backgroundColor(color: Color): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-background-color", color.toRGBAString())
        return this
    }
    public fun fontFamily(fontFamily: String): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-font-family", fontFamily)
        return this
    }
    public fun borderColor(color: Color): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-border-color", color.toRGBAString())
        return this
    }
    public fun borderWidth(width: Double): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-border-width", "$width")
        return this
    }
    public fun opacity(opacity: Double): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-opacity", "$opacity")
        return this
    }
    public fun backgroundRadius(radius: Double): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-background-radius", "$radius")
        return this
    }
    public fun backgroundRadius(radius: String): StyleSheetBuilder
    {
        this.generator.addAttribute("-fx-background-radius", radius)
        return this
    }
    public fun urlSrc(url: String): StyleSheetBuilder
    {
        this.generator.addAttribute("src", "url(\"$url\")")
        return this
    }
}