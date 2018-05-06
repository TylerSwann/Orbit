package io.orbit.text;

import io.orbit.api.LanguageDelegate;
import io.orbit.text.webtools.CSS3;
import io.orbit.text.test.PlainText;
import io.orbit.text.test.SampleLanguage;
import java.io.File;

/**
 * Created by Tyler Swann on Friday February 16, 2018 at 14:28
 */
@Deprecated
public class LanguageManager
{
    private static final String[] SUPPORTED_FILE_TYPES;
    static {
        SUPPORTED_FILE_TYPES = new String[]{
                "css",
                "txt",
                "sample"
        };
    }

    public static LanguageDelegate languageFromFile(File file)
    {

        int index = file.getName().lastIndexOf('.');
        String extension = "txt";
        if (index > 0)
            extension = file.getName().substring(index + 1);
        switch (extension)
        {
            case "css": return new CSS3();
            case "sample": return new SampleLanguage();
        }
        return new PlainText();
    }
}
