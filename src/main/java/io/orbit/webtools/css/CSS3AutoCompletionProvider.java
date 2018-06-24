package io.orbit.webtools.css;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday March 18, 2018 at 13:31
 */
public class CSS3AutoCompletionProvider
{
    private static CSS3AutoCompletionOption[] CSS_PROPERTIES;

    public CSS3AutoCompletionProvider()
    {
        if (CSS_PROPERTIES == null)
        {
            try
            {
                Gson gson = new Gson();
                CSS_PROPERTIES = new CSS3AutoCompletionOption[0];
                URL cssPropertiesURL = getClass().getClassLoader().getResource("webtools/css_auto_completion_options.json");
                if (cssPropertiesURL == null)
                    throw new RuntimeException("URL to resource file webtools/css_auto_completion_options.json was null in CSS3AutoCompletionProvider");
                File file = new File(cssPropertiesURL.getFile());
                JsonReader reader = new JsonReader(new FileReader(file));
                CSS_PROPERTIES = gson.fromJson(reader, CSS3AutoCompletionOption[].class);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                throw new RuntimeException("Couldn't load CSS Autocompletion properties from resource file...");
            }
        }
    }

    public List<CSS3AutoCompletionOption> optionsForLine(String currentLine)
    {
        List<CSS3AutoCompletionOption> options = new ArrayList<>();
        for (CSS3AutoCompletionOption option : CSS_PROPERTIES)
        {
            if (currentLine == null || option.text.length() < currentLine.length())
                continue;
            currentLine = currentLine.replaceAll("^\\s*", "");
            String splitOption = option.text.substring(0, currentLine.length());

            if (splitOption.equals(currentLine))
            {
                option.insertedTextFragment = option.insertedText.replaceFirst(currentLine, "");
                options.add(option);
            }
        }
        return options;
    }

    public List<CSS3AutoCompletionOption> subOptionsForOption(CSS3AutoCompletionOption option, String currentLine)
    {
        List<CSS3AutoCompletionOption> options = new ArrayList<>();
        for (CSS3AutoCompletionOption subOption : option.subOptions)
        {
            if (currentLine == null || subOption.text.length() < currentLine.length())
                continue;
            System.out.println(currentLine);
            currentLine = currentLine.replaceAll(String.format("^\\s*%s", option.insertedText), "");
            System.out.println(currentLine);
            System.out.println("******");
            String splitOption = subOption.text.substring(0, currentLine.length());

            if (splitOption.equals(currentLine))
            {
                subOption.insertedTextFragment = subOption.insertedText.replaceFirst(currentLine, "");
                options.add(subOption);
            }
        }
        return options;
    }

//    public List<? extends AutoCompletionBase> optionsFor(int from, int to, int line, String word)
//    {
//        List<CSS3AutoCompletionOption> options = new ArrayList<>();
//
//
//        for (CSS3AutoCompletionOption option : CSS_PROPERTIES)
//        {
//            if (word == null || option.getText().length() < word.length())
//                continue;
//            String substring = option.getText().substring(0, word.length());
//            if (substring.equals(word))
//                options.add(option);
//        }
//
//        return options;
//    }

}
