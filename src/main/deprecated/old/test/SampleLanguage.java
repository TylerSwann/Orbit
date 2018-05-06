package io.orbit.text.test;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.HighlightingProvider;

/**
 * Created by Tyler Swann on Thursday February 01, 2018 at 17:04
 */
@Deprecated
public class SampleLanguage implements LanguageDelegate
{
    private static final String EXAMPLE_CODE = "" +
            "    public func doSomething()\n" +
            "    {\n" +
            "        if (1 == 2)\n" +
            "        {\n" +
            "            let myName = \"Tyler\";\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public func another()\n" +
            "    {\n" +
            "        if (1 == 2)\n" +
            "        {\n" +
            "            let myName = \"Tyler\";\n" +
            "            var me = 23;\n" +
            "            if (1 != 12)\n" +
            "            {\n" +
            "                let yourname = \"Joe\";\n" +
            "            }\n" +
            "        }\n" +
            "    }";


    @Override
    public String getFileNameExtension()
    {
        return "sample";
    }
    @Override
    public HighlightingProvider getHighlightingProvider()
    {
        return new SampleHighlightingProvider();
    }
}
