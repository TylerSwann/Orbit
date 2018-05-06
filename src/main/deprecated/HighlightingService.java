package io.orbit.text;


import java.util.concurrent.ExecutorService;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 17:06
 */
@Deprecated
public interface HighlightingService<T>
{
    void highlight(ExecutorService service);
}
