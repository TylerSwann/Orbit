package io.orbit.ui.terminal;

import com.sun.javafx.css.converters.PaintConverter;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.css.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MUITerminal extends EmbeddedTerminal
{
    private static final String DEFAULT_STYLE_CLASS = "mui-terminal";
    private InvalidationListener fontListener;
    private InvalidationListener textFillListener;
    private InvalidationListener backgroundColorListener;

    public MUITerminal()
    {
        this.setPrefSize(600.0, 400.0);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setPreference("background-color", "white");
        this.setPreference("foreground-color", "black");
        this.setPreference("font-family", "Consolas, monospaced");
        this.setPreference("scrollbar-visible", "false");
        this.registerListeners();
    }

    @Override
    public void open()
    {
        super.open();
        this.fontProperty().addListener(this.fontListener);
        this.textFillProperty().addListener(this.textFillListener);
        this.backgroundProperty().addListener(this.backgroundColorListener);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        this.fontProperty().removeListener(this.fontListener);
        this.textFillProperty().removeListener(this.textFillListener);
        this.backgroundProperty().removeListener(this.backgroundColorListener);
    }

    private void registerListeners()
    {
        this.fontListener = (o) -> {
            Font font = this.getFont();
            this.setPreference("font-family", font.getFamily());
            this.setPreference("font-size", String.format("%dpx", ((int)font.getSize())));
        };
        this.textFillListener = (o) -> {
            Color fill = this.getTextFill();
            String color = String.format("rgba(%d, %d, %d, %f)",
                    (int)(fill.getRed() * 255.0),
                    (int)(fill.getGreen() * 255.0),
                    (int)(fill.getBlue() * 255.0),
                    fill.getOpacity()
            );
            this.setPreference("foreground-color", color);
        };
        this.backgroundColorListener = (o) -> {
            if (this.getBackground() == null || this.getBackground().getFills().size() <= 0)
                return;
            Color fill = (Color) this.getBackground().getFills().get(0).getFill();
            String color = String.format("rgba(%d, %d, %d, %f)",
                    (int)(fill.getRed() * 255.0),
                    (int)(fill.getGreen() * 255.0),
                    (int)(fill.getBlue() * 255.0),
                    fill.getOpacity()
            );
            this.setPreference("background-color", color);
        };
    }

    public Font getFont() { return font.get(); }
    public void setFont(Font font) { this.font.set(font); }
    private ObjectProperty<Font> fontProperty() { return font; }
    private final ObjectProperty<Font> font = new StyleableObjectProperty<Font>(Font.getDefault()) {
        @Override public Object getBean() { return MUITerminal.this; }
        @Override public String getName() { return "font"; }
        @Override public CssMetaData<? extends Styleable, Font> getCssMetaData() { return StyleableProperties.FONT; }
    };

    public Color getTextFill() { return (Color) textFill.get(); }
    public void setTextFill(Color fill) { this.textFill.set(fill); }
    public ObjectProperty<Paint> textFillProperty() { return textFill; }
    private final ObjectProperty<Paint> textFill = new StyleableObjectProperty<Paint>(Color.BLACK) {
        @Override public Object getBean() { return MUITerminal.this; }
        @Override public String getName() { return "text-fill"; }
        @Override public CssMetaData<? extends Styleable, Paint> getCssMetaData() { return StyleableProperties.TEXT_FILL; }
    };

    private static class StyleableProperties
    {
        private static final CssMetaData<MUITerminal, Paint> TEXT_FILL = new CssMetaData<MUITerminal, Paint>("-fx-text-fill", PaintConverter.getInstance()) {
            @Override public boolean isSettable(MUITerminal styleable) { return !styleable.textFill.isBound(); }
            @Override public StyleableProperty<Paint> getStyleableProperty(MUITerminal styleable) { return (StyleableProperty<Paint>) styleable.textFill; }
        };
        private static final FontCssMetaData<MUITerminal> FONT = new FontCssMetaData<MUITerminal>("-fx-font", Font.getDefault()) {
            @Override public boolean isSettable(MUITerminal styleable) { return !styleable.font.isBound(); }
            @Override public StyleableProperty<Font> getStyleableProperty(MUITerminal styleable) { return (StyleableProperty<Font>) styleable.font; }
        };
    }

    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    static {
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(AnchorPane.getClassCssMetaData());
        styleables.add(StyleableProperties.TEXT_FILL);
        styleables.add(StyleableProperties.FONT);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() { return STYLEABLES; }
}
