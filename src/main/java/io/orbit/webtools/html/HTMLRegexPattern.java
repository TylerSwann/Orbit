package io.orbit.webtools.html;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:06
 */
public class HTMLRegexPattern extends RegexStylePattern
{
    private static final String COMMENT_PATTERN = "(<!--)(.|\\\\n)*?(-->)";
    private static final String DOUBLE_QUOTED_STRING = "\"(?:[^\"][a-zA-Z0-9]*(\\\\\")?)*\"";
    private static final String SINGLE_QUOTED_STRING = "\'(?:[^\'][a-zA-Z0-9]*(\\\\\')?)*\'";
                                                      //!?(=)\'(?:[^\'][a-zA-Z0-9]*(\\\\\')?)*\'
//    private static final String SINGLE_QUOTED_STRING = "(?:=)\'(?:[^\'][a-zA-Z0-9]*(\')?)*\'";
//    private static final String SINGLE_QUOTED_STRING = "'(?:[^'][a-zA-Z0-9]*(')?)*'";
    private static final String EQUAL_PATTERN = "=";
    private static final String TAG_PATTERN = "(<[a-zA-Z0-9\\-]+>?|>|</[a-zA-Z0-9\\-]+>)" ;
    private static final String ATTRIBUTE_PATTERN = "(?<ATTRIBUTE>([a-zA-Z\\-]+))(?:=)";
    private static final String DOCTYPE_PATTERN = "<![a-zA-Z0-9\\s]*>";
    private static final Pattern regexPattern;
    private static final Map<String, HighlightType> styleMap;

    static {
        regexPattern = Pattern.compile(
                 "(?<COMMENT>" + COMMENT_PATTERN + ")"
                +"|(?<EQUAL>" + EQUAL_PATTERN + ")"
                +"|(?<TAG>" + TAG_PATTERN + ")"
                +"|" + ATTRIBUTE_PATTERN + ""
                +"|(?<DOC>" + DOCTYPE_PATTERN + ")"
                +"|(?<DBLSTRING>" + DOUBLE_QUOTED_STRING + ")"
                +"|(?<SNGLSTRING>" + SINGLE_QUOTED_STRING + ")"
        );
        styleMap = new HashMap<>();
        styleMap.put("COMMENT", HighlightType.BLOCK_COMMENT);
        styleMap.put("EQUAL", HighlightType.OPERATOR);
        styleMap.put("TAG", HighlightType.ANNOTATION);
        styleMap.put("DOC", HighlightType.ANNOTATION);
        styleMap.put("DBLSTRING", HighlightType.STRING);
        styleMap.put("SNGLSTRING", HighlightType.STRING);
        styleMap.put("ATTRIBUTE", HighlightType.TYPE);
    }
    public HTMLRegexPattern()
    {
        super(regexPattern, styleMap);
    }
}
