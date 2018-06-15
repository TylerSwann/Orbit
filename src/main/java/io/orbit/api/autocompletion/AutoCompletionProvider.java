package io.orbit.api.autocompletion;

import java.util.List;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 19:44
 */
@Deprecated
public interface AutoCompletionProvider
{
    List<? extends AutoCompletionBase> optionsFor(int from, int to, int line, String word);
}
