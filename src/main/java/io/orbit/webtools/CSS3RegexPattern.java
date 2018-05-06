package io.orbit.webtools;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 18:55
 */
public class CSS3RegexPattern
{
    private static final String ANNOTATED_PATTERN = "(\\@[a-zA-Z]+[a-zA-Z0-9?\\-?\\_?]*)";
    private static final String SELECTOR_PATTERN = "(?<SELECTOR>([\\.|\\#][a-z|A-Z|0-9|\\_\\\\-]+))(?:[\\n|\\:])(?<PSEUDOCLASS>([a-z|A-Z|0-9|\\-\\_]+))?";
    private static final String BRACKETS_PATTERN = "\\{|\\}";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String SEMI_COLON_PATTERN = ";";
    private static final String PROPERTY_PATTERN = "(?<PROPERTY>[a-zA-Z]+.*)(?:\\:)";
    private static final String STRING_PATTERN = "\\\".*\\\"|\\\'.*\\\'";
    private static final String DOUBLE_PATTERN = "([0-9]+\\.[0-9]+)";
    private static final String HEX_PATTERN = "(\\#[a-zA-Z0-9]+)";
    private static final String INT_PATTERN = "([0-9]+)";
    private static final String MULTICOM_PATTERN = "(\\/\\*)(.|\\n)+?(\\*\\/)";
    private static final String SINGLECOM_PATTERN = "(\\/\\/.*)";
    private static final String FUNC_PATTERN = "(attr|blur|brightness|calc|circle|contrast|counter|counters|cubic-bezier|drop-shadow|ellipse|dropshadow|\n" +
                                               "grayscale|hsl|hsla|hue-rotate|hwb|image|inset|invert|linear-gradient|matrix|matrix3d|opacity|\n" +
                                               "perspective|polygon|radial-gradient|repeating-linear-gradient|repeating-radial-gradient|rgba|rgb|rotate|\n" +
                                               "rotate3d|rotateX|rotateY|rotateZ|saturate|sepia|scale|scale3d|scaleX|scaleY|scaleZ|skew|skewX|\n" +
                                               "skewY|symbols|translate|translate3d|translateX|translateY|translateZ|url)";


    private static final Pattern PATTERN = Pattern.compile(
                      SELECTOR_PATTERN
                    + "|(?<ANNOTATION>" + ANNOTATED_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKETS_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMI_COLON_PATTERN + ")"
                    + "|" + PROPERTY_PATTERN
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<DOUBLE>" + DOUBLE_PATTERN + ")"
                    + "|(?<HEX>" + HEX_PATTERN + ")"
                    + "|(?<INT>" + INT_PATTERN + ")"
                    + "|(?<FUNC>" + FUNC_PATTERN + ")"
                    + "|(?<MULTICOM>" + MULTICOM_PATTERN + ")"
                    + "|(?<SINGLECOM>" + SINGLECOM_PATTERN + ")"

    );
    public static final RegexStylePattern STYLE_PATTERN;

    static {
        Map<String, HighlightType> patternStyleMap = new HashMap<>();
        patternStyleMap.put("PROPERTY", HighlightType.TYPE);
        patternStyleMap.put("ANNOTATION", HighlightType.ANNOTATION);
        patternStyleMap.put("SELECTOR", HighlightType.TYPE);
        patternStyleMap.put("PSEUDOCLASS", HighlightType.KEYWORD);
        patternStyleMap.put("BRACKET", HighlightType.OPERATOR);
        patternStyleMap.put("PAREN", HighlightType.OPERATOR);
        patternStyleMap.put("SEMICOLON", HighlightType.OPERATOR);
        patternStyleMap.put("STRING", HighlightType.STRING);
        patternStyleMap.put("DOUBLE", HighlightType.NUMBER);
        patternStyleMap.put("HEX", HighlightType.NUMBER);
        patternStyleMap.put("INT", HighlightType.NUMBER);
        patternStyleMap.put("FUNC", HighlightType.KEYWORD);
        patternStyleMap.put("MULTICOM", HighlightType.BLOCK_COMMENT);
        patternStyleMap.put("SINGLECOM", HighlightType.LINE_COMMENT);

        STYLE_PATTERN = new RegexStylePattern(PATTERN, patternStyleMap);
    }
}
