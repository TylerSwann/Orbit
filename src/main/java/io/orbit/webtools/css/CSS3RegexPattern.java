package io.orbit.webtools.css;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 18:55
 */
public class CSS3RegexPattern extends RegexStylePattern
{
    //    private static final String STRING_PATTERN = "\".*\"|'.*'";
    private static final String ANNOTATED_PATTERN = "(@[a-zA-Z]+[a-zA-Z0-9?\\-?_?]*)";
    private static final String SELECTOR_PATTERN = "(?<SELECTOR>([.#][a-zA-Z]+[a-zA-Z0-9\\-_\\[\\]]+))(?<PSEUDOCLASS>((?::)([a-zA-Z]+)))*[\\n\\s,](?!(;))";
    private static final String BRACKETS_PATTERN = "[{}]";
    private static final String PAREN_PATTERN = "[()]";
    private static final String SEMI_COLON_PATTERN = ";";
    private static final String PROPERTY_PATTERN = "(?<PROPERTY>([a-zA-Z\\-]+.*))(?::)";
    private static final String STRING_PATTERN = "(\\\\.*?\\\\)";
    private static final String DOUBLE_PATTERN = "([0-9]+\\.[0-9]+)";
    private static final String HEX_PATTERN = "(#[a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9])";
    private static final String INT_PATTERN = "([0-9]+)";
    private static final String COMMENT_PATTERN = "(/\\*)(.|\\n)+?(\\*/)";
    private static final String FUNC_PATTERN = "(attr|blur|brightness|calc|circle|contrast|counter|counters|cubic-bezier|drop-shadow|ellipse|dropshadow|\n" +
                                               "grayscale|hsl|hsla|hue-rotate|hwb|image|inset|invert|linear-gradient|matrix|matrix3d|opacity|\n" +
                                               "perspective|polygon|radial-gradient|repeating-linear-gradient|repeating-radial-gradient|rgba|rgb|rotate|\n" +
                                               "rotate3d|rotateX|rotateY|rotateZ|saturate|sepia|scale|scale3d|scaleX|scaleY|scaleZ|skew|skewX|\n" +
                                               "skewY|symbols|translate|translate3d|translateX|translateY|translateZ|url|!important)";


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
                    + "|(?<MULTICOM>" + COMMENT_PATTERN + ")"
                    + "|(?<COLON>(:))"
    );
    private static final Map<String, HighlightType> patternStyleMap;

    static {
        patternStyleMap = new HashMap<>();
        patternStyleMap.put("PROPERTY", HighlightType.TYPE);
        patternStyleMap.put("ANNOTATION", HighlightType.ANNOTATION);
        patternStyleMap.put("SELECTOR", HighlightType.TYPE);
        patternStyleMap.put("PSEUDOCLASS", HighlightType.KEYWORD);
        patternStyleMap.put("BRACKET", HighlightType.OPERATOR);
        patternStyleMap.put("PAREN", HighlightType.OPERATOR);
        patternStyleMap.put("SEMICOLON", HighlightType.SEMI_COLON);
        patternStyleMap.put("STRING", HighlightType.STRING);
        patternStyleMap.put("DOUBLE", HighlightType.NUMBER);
        patternStyleMap.put("HEX", HighlightType.NUMBER);
        patternStyleMap.put("INT", HighlightType.NUMBER);
        patternStyleMap.put("FUNC", HighlightType.KEYWORD);
        patternStyleMap.put("MULTICOM", HighlightType.BLOCK_COMMENT);
        patternStyleMap.put("COLON", HighlightType.EMPTY);
    }

    public CSS3RegexPattern()
    {
        super(PATTERN, patternStyleMap);
    }
}
