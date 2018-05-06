package io.orbit.api.highlighting;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:28
 */
@Deprecated
public interface RegexHighlightingProvider extends HighlightingProvider
{
    RegexStylePattern getStylePattern();
}
