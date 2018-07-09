package io.orbit.ui.contextmenu;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.css.converters.PaintConverter;
import javafx.css.*;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday July 07, 2018 at 14:52
 */
public class MUIMenuItem extends JFXButton
{
    private static final String DEFAULT_STYLE_CLASS = "mui-context-item";

    public MUIMenuItem(Ikon iconCode, String text)
    {
        super(text, new FontIcon(iconCode));
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /*

    private val textColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(TEXT_COLOR, this, "textColor", Color.BLACK)
    public fun textColorProperty(): StyleableObjectProperty<Paint> = textColor
    public fun getTextColor(): Paint = textColor.get()
    public fun setTextColor(value: Paint) = textColor.set(value)

    internal companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val RIPPLE_COLOR = object: CssMetaData<MUIButton, Paint>("-fx-ripple-color", PaintConverter.getInstance(), Color.color(0.0, 0.0, 0.0, 0.3)) {
            override fun isSettable(styleable: MUIButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIButton?): StyleableProperty<Paint> = styleable?.rippleColorProperty()!!
        }

        internal val TEXT_COLOR = object: CssMetaData<MUIButton, Paint>("-fx-text-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUIButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIButton?): StyleableProperty<Paint> = styleable?.textColorProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(Control.getClassCssMetaData().size, { index -> Control.getClassCssMetaData()[index]})
            Collections.addAll(styleables, RIPPLE_COLOR, TEXT_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
    * */

    private final StyleableObjectProperty<Paint> hoverColor = new SimpleStyleableObjectProperty<>(HOVER_COLOR, this, "hoverColor");
    public final StyleableObjectProperty<Paint> hoverColorProperty() {  return hoverColor;  }
    public final Paint getHoverColor() {  return hoverColor.get();  }
    public final void setHoverColor(Paint value) {  hoverColor.set(value);  }


    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    private static final Color DEFAULT_HOVER_COLOR = Color.rgb(255, 255, 255, 0.5);
    private static final CssMetaData<MUIMenuItem, Paint> HOVER_COLOR = new CssMetaData<MUIMenuItem, Paint>("-fx-hover-color", PaintConverter.getInstance(), DEFAULT_HOVER_COLOR) {
        @Override
        public boolean isSettable(MUIMenuItem styleable) { return true; }

        @Override
        public StyleableProperty<Paint> getStyleableProperty(MUIMenuItem styleable) {
            return null;
        }
    };

    static {
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
        styleables.add(HOVER_COLOR);
        STYLEABLES = styleables;
    }
}
