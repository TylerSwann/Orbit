package io.orbit.util

/**
 * Created by Tyler Swann on Sunday January 21, 2018 at 09:07
 */
@Deprecated("No Longer Used")
class StyleSheetGenerator
{
    private val classes: HashMap<String, StyleClass> = HashMap()
    private var currentClass: StyleClass? = null

    public fun visitStyleClass(className: String)
    {
        if (this.classes[className] != null)
        {
            this.currentClass = this.classes[className]
            return
        }
        this.classes.put(className, StyleClass(className, HashMap()))
    }

    public fun endStyleClassVisit()
    {
        this.currentClass = null
    }

    public fun addAttribute(key: String, value: String)
    {
        this.currentClass?.attributes?.put(key, value)
    }

    public fun generate(): String
    {
        val builder = StringBuilder("")
        for ((className, styleClass) in this.classes)
        {
            builder.append("$className\n")
            builder.append("{\n")
            for ((key, value) in styleClass.attributes)
                builder.append("$key: $value;\n")
            builder.append("}\n")
        }
        return builder.toString()
    }

    private data class StyleClass(val name: String, val attributes: HashMap<String, String>)
}








